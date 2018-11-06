package sedec2.arib.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.SignallingPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.arib.tlv.mmt.MessageFactory;
import sedec2.arib.tlv.mmt.messages.Message;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Payload_SignallingMessage;
import sedec2.base.Table;
import sedec2.util.Logger;

public class SiExtractor extends BaseExtractor {
    public interface ITableExtractorListener extends BaseExtractor.Listener {
        public void onReceivedTable(Table table);    
    }
    
    protected final String TAG = "SiExtractor";
    protected boolean m_is_running = true;
    protected Thread m_table_event_thread;
    protected Thread m_tlv_extractor_thread;
    
    protected BlockingQueue<Table> m_tables = new ArrayBlockingQueue<Table>(100);
    protected BlockingQueue<byte[]> m_tlv_packets = new ArrayBlockingQueue<byte[]>(100);
    protected List<MMTP_Packet> m_fragmented01_mmtp_with_signal_message = new ArrayList<>();
    protected List<MMTP_Packet> m_fragmented02_mmtp_with_signal_message = new ArrayList<>();
            
    public SiExtractor() {
        m_tlv_extractor_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_tlv_packets ) {
                            byte[] tlv_raw = (byte[])m_tlv_packets.take();
                            TypeLengthValue tlv = 
                                    sedec2.arib.tlv.container.PacketFactory.createPacket(tlv_raw);
                            process(tlv); 
                        }
                        
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * @note Nothing to do
                         */
                    } catch ( Exception e ) {
                        /**
                         * @todo You should remove a line below, because TLVExtractor \n
                         * has to keep alive even though TLVExtractor get any wrong packets.
                         */
                        e.printStackTrace();
                    }
                }        
            }
        });
        
        m_table_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Table table = null;
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_tables && (table = m_tables.take()) != null ) {
                            if ( enable_logging == true ) table.print();
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((ITableExtractorListener)m_listeners.get(i)).
                                        onReceivedTable(table);
                            }
                        }
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /** 
                         * @note Nothing to do
                         */
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        m_tlv_extractor_thread.start();
        m_table_event_thread.start();
    }
    
    /**
     * User should use this function when they don't use TLVExtractor any more.
     */
    public void destroy() {
        m_is_running = false;
        
        m_table_event_thread.interrupt();
        m_table_event_thread = null;
        
        m_tlv_extractor_thread.interrupt();
        m_tlv_extractor_thread = null;
        
        m_fragmented01_mmtp_with_signal_message.clear();
        m_fragmented01_mmtp_with_signal_message = null;
                
        m_fragmented02_mmtp_with_signal_message.clear();
        m_fragmented02_mmtp_with_signal_message = null;
        
        m_listeners.clear();
        m_listeners = null;
                
        m_tlv_packets.clear();
        m_tlv_packets = null;
        
        m_tables.clear();
        m_tables = null;

    }
    
    /**
     * User can put a TLV packet to get the results as Table of MMT-SI, TLV-SI and more.
     * @param tlv a variable TLV packet
     * @return Return false if TLVExtractor has situation which can't parse like overflow.
     */
    @Override
    public void putIn(byte[] tlv) throws InterruptedException {
        if ( m_is_running == true && m_tlv_packets != null && tlv != null ) {
            m_tlv_packets.put(tlv);
        }
    }

    @Override
    protected void putOut(Object table) throws InterruptedException {
        if ( table == null ) return;

        if ( enable_logging == true ) ((Table)table).print();
        if ( m_byte_id_filter.contains(((Table)table).getTableId()) == true ) {
            m_tables.put((Table)table);
        }
    }
    
    protected synchronized void process(TypeLengthValue tlv) 
            throws InterruptedException, IOException {
        switch ( tlv.getPacketType() ) {
            case PacketFactory.IPV4_PACKET:
            case PacketFactory.IPV6_PACKET:
                break;
            case PacketFactory.SIGNALLING_PACKET:
                putOut( ((SignallingPacket)tlv).getTable() );
                break;
            case PacketFactory.COMPRESSED_IP_PACKET:
                CompressedIpPacket cip = (CompressedIpPacket) tlv;
                MMTP_Packet mmtp_packet = cip.getPacketData().mmtp_packet;
                
                if ( mmtp_packet == null ) break;
                
                /**
                 * @note Signaling Message
                 */
                if ( 0x02 == mmtp_packet.getPayloadType() ) {
                    processMmtpSignallingMessage(mmtp_packet);
                } 
                break;
        }
    }
    
    /**
     * Processing signaling message which is payload_type(0x02) of MMTP.
     * A table which signaling message has will be sent to application unless the table has fragmented
     * ARIB B60 6.3.2 Configuration of MMTP payload
     * @param mmtp MMTP_Packet
     * @return tables of MMT-SI
     * @throws IOException
     * @throws InterruptedException 
     */
    protected void processMmtpSignallingMessage(MMTP_Packet mmtp) 
            throws IOException, InterruptedException {
        int packet_id = mmtp.getPacketId();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MMTP_Payload_SignallingMessage signal_message = mmtp.getSignallingMessage();
        
        if ( enable_logging == true ) {
            Logger.d(String.format("[S] pid : 0x%04x, f_i : 0x%x, a_f : 0x%x \n", 
                    packet_id, signal_message.getFragmentationIndicator(),
                    signal_message.getAggregationFlag()));
        }
        
        if ( signal_message != null ) {
            Message message = null;
            int fragmentaion_indicator = 
                    signal_message.getFragmentationIndicator() & 0xff;
            switch ( fragmentaion_indicator ) {
                case 0x00:
                    byte[] buffer = signal_message.getMessageByte();
                    message = MessageFactory.createMessage(buffer);
                  
                    if ( message != null ) {
                        List<Table> tables = message.getTables();
                        for ( int i=0; i<tables.size(); i++ ) {
                            putOut(tables.get(i));
                        }
                    }
                    break;
                case 0x01:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves header part of divided data.
                     */
                    m_fragmented01_mmtp_with_signal_message.add(mmtp);
                    break;
                case 0x02:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves a part of divided data which is \n
                     * neither header part nor last part.
                     */
                    m_fragmented02_mmtp_with_signal_message.add(mmtp);
                    break;
                case 0x03:
                    boolean found_01_fragmentation_indicator = false;
                    /**
                     * @note ARIB B60 Table 6-2, This involves last part of divided data.
                     * In other words, it's a timing to make whole section table since 0x03 is last packet
                     * after gathering fragmented data of tables.
                     * Case.1 0x01 of fragmentation_indicator will be only one thing.  
                     */
                    for ( Iterator<MMTP_Packet> it = 
                            m_fragmented01_mmtp_with_signal_message.iterator() ; 
                            it.hasNext() ; ) {
                        MMTP_Packet mmtp01 = it.next();
                        MMTP_Payload_SignallingMessage sm = mmtp01.getSignallingMessage();
                        if( mmtp01.getPacketId() == mmtp.getPacketId() && 
                                sm != null &&
                                sm.getFragmentationIndicator() == 0x01 ) {
                            outputStream.write(sm.getMessageByte());
                            it.remove();
                            found_01_fragmentation_indicator = true;
                            break;
                        }
                    }
              
                    /**
                     * Case.2 0x02 of fragmentation_indicator will be multiple things  
                     */
                    if ( found_01_fragmentation_indicator == true ) {
                        for(Iterator<MMTP_Packet> it = 
                                m_fragmented02_mmtp_with_signal_message.iterator() ; 
                                it.hasNext() ; ) {
                            MMTP_Packet mmtp02 = it.next();
                            MMTP_Payload_SignallingMessage sm = mmtp02.getSignallingMessage();
                            if( mmtp02.getPacketId() == mmtp.getPacketId() &&
                                    sm != null && 
                                    sm.getFragmentationIndicator() == 0x02 ) {
                                outputStream.write(sm.getMessageByte());
                                it.remove();
                            }
                        }
                    
                        /**
                         * Case.2 0x03 of fragmentation_indicator will be only one thing as last
                         */
                        outputStream.write(signal_message.getMessageByte());
                        
                        /**
                         * As a result, outputStream has whole data of tables
                         */
                        message = MessageFactory.createMessage(outputStream.toByteArray());
                        if ( message != null ) {
                            List<Table> tables = message.getTables();
                            for ( int i=0; i<tables.size(); i++ ) {
                                putOut(tables.get(i));
                            }
                        }
                    }
                    break;
            }
        }
    }
}
