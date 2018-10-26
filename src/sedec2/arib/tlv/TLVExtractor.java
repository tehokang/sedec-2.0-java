package sedec2.arib.tlv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.IPv4Packet;
import sedec2.arib.tlv.container.packets.IPv6Packet;
import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.arib.tlv.container.packets.SignallingPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.arib.tlv.mmt.MessageFactory;
import sedec2.arib.tlv.mmt.messages.Message;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Payload_SignallingMessage;
import sedec2.base.Table;
import sedec2.util.Logger;

public class TLVExtractor {
    protected final String TAG = "TLVExtractor";
    
    boolean m_enable_ntp_filter = false;
    boolean m_enable_table_filter = false;
    boolean m_enable_all_of_table_filter = false;
    List<Byte> m_table_id_filters = new ArrayList<>();
    
    protected Thread m_ntp_event_thread;
    protected Thread m_table_event_thread;
    protected Thread m_tlv_extractor_thread;
    protected boolean m_is_running = true;
    
    protected List<ITLVExtractorListener> m_listeners = new ArrayList<>();

    protected BlockingQueue<NetworkTimeProtocolData> m_ntps = 
            new ArrayBlockingQueue<NetworkTimeProtocolData>(100);
    protected BlockingQueue<Table> m_tables = new ArrayBlockingQueue<Table>(100);
    protected BlockingQueue<byte[]> m_tlv_packets = new ArrayBlockingQueue<byte[]>(100);
    
    protected Map<Integer, MMTP_Packet> m_fragmented01_mmtp = 
            new HashMap<Integer, MMTP_Packet>();
    protected Map<Integer, MMTP_Packet> m_fragmented02_mmtp = 
            new HashMap<Integer, MMTP_Packet>();
    
    /**
     * @note sample_counter and print function is only for testing
     */
    protected long tlv_sample_counter = 0;
    protected long table_sample_counter = 0;
    
    public TLVExtractor() {
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
                            processTLV(tlv); 
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
        
        m_ntp_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_ntps ) emitNtp(m_ntps.take());
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
        
        m_table_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_tables ) emitTable(m_tables.take());
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
        m_ntp_event_thread.start();
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
        
        m_ntp_event_thread.interrupt();
        m_ntp_event_thread = null;
        
        m_listeners.clear();
        m_listeners = null;
                
        m_tlv_packets.clear();
        m_tlv_packets = null;
        
        m_tables.clear();
        m_tables = null;

        m_ntps.clear();
        m_ntps = null;
    }
    
    /**
     * Application should add their own listener to recieve tables, ntp, and so on which
     * SDK can send
     * @param listener
     */
    public void addEventListener(ITLVExtractorListener listener) {
        m_listeners.add(listener);
    }
    
    public void removeEventListener(ITLVExtractorListener listener) {
        m_listeners.remove(listener);
    }
    
    /**
     * User can put a TLV packet to get the results as Table of MMT-SI, TLV-SI and more.
     * @param tlv a variable TLV packet
     * @return Return false if TLVExtractor has situation which can't parse like overflow.
     */
    public boolean put(byte[] tlv) {
        try {
            if ( m_is_running == true && m_tlv_packets != null && tlv != null ) {
                m_tlv_packets.put(tlv);
            } else {
                return false;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Sending a NTP to application
     * @param ntp
     */
    protected synchronized void emitNtp(NetworkTimeProtocolData ntp) {
        if ( ntp != null && m_enable_ntp_filter == true ) {
            for ( int i=0; i<m_listeners.size(); i++ ) {
                m_listeners.get(i).onUpdatedNtp(ntp);
            }
        }
    }
    
    /**
     * Sending a table to application
     * @param table
     */
    protected synchronized void emitTable(Table table) {
        if ( table != null ) {
            if ( m_enable_all_of_table_filter == true ) {
                for ( int i=0; i<m_listeners.size(); i++ ) {
                    m_listeners.get(i).onReceivedTable(table);
                }
            } else if ( m_enable_table_filter == true ) {
                for ( int i=0; i<m_listeners.size(); i++ ) {
                    if ( m_table_id_filters.contains(table.getTableId()) == true ) {
                        m_listeners.get(i).onReceivedTable(table);
                    }
                }
            }
        }
    }
    
    protected synchronized void processTLV(TypeLengthValue tlv) 
            throws InterruptedException, IOException {
        switch ( tlv.getPacketType() & 0xff ) {
            case PacketFactory.SIGNALLING_PACKET:
                m_tables.put( ((SignallingPacket)tlv).getTable() );
                break;
            case PacketFactory.IPV4_PACKET:
                NetworkTimeProtocolData ipv4_ntp = ((IPv4Packet)tlv).getNtp();
                if ( ipv4_ntp != null ) m_ntps.put(ipv4_ntp);
                break;
            case PacketFactory.IPV6_PACKET:
                NetworkTimeProtocolData ipv6_ntp = ((IPv6Packet)tlv).getNtp();
                if ( ipv6_ntp != null ) m_ntps.put(ipv6_ntp);
                break;
            case PacketFactory.COMPRESSED_IP_PACKET:
                CompressedIpPacket cip = (CompressedIpPacket) tlv;
                MMTP_Packet mmtp_packet = cip.getPacketData().mmtp_packet;
                
                if ( mmtp_packet == null ) break;
                
                /**
                 * @note Signaling Message
                 */
                if ( 0x02 == mmtp_packet.getPayloadType() ) {
                    List<Table> tables = 
                            processMmtpSignallingMessage(mmtp_packet);
                    
                    for ( int i=0; i<tables.size(); i++ ) {
                        m_tables.put(tables.get(i));
                    }
                } 
                
                /**
                 * @note MPU-MFU
                 */
                if ( 0x00 == mmtp_packet.getPayloadType() ) {
                    
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * Processing signaling message which is payload_type(0x02) of MMTP.
     * A table which signaling message has will be sent to application unless the table has fragmented
     * ARIB B60 6.3.2 Configuration of MMTP Payload
     * @param mmtp MMTP_Packet
     * @return tables of MMT-SI
     * @throws IOException
     */
    protected List<Table> processMmtpSignallingMessage(MMTP_Packet mmtp) throws IOException {
        MMTP_Payload_SignallingMessage signal_message = mmtp.getSignallingMessage();
        
        if ( signal_message != null ) {
            Message message = null;
            int fragmentaion_indicator = 
                    signal_message.getFragmentationIndicator() & 0xff;
            switch ( fragmentaion_indicator ) {
                case 0x00:
                    byte[] buffer = signal_message.getMessageByte();
                    message = MessageFactory.createMessage(buffer);
                  
                    if ( message != null ) return message.getTables();
                    break;
                case 0x01:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves header part of divided data.
                     */
                    m_fragmented01_mmtp.put(mmtp.getPacketSequenceNumber(), mmtp);
                    break;
                case 0x02:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves a part of divided data which is \n
                     * neither header part nor last part.
                     */
                    m_fragmented02_mmtp.put(mmtp.getPacketSequenceNumber(), mmtp);
                    break;
                case 0x03:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves last part of divided data.
                     */
                    MMTP_Packet mmtp01 = m_fragmented01_mmtp.get(mmtp.getPacketSequenceNumber()-2);
                    MMTP_Packet mmtp02 = m_fragmented02_mmtp.get(mmtp.getPacketSequenceNumber()-1);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    
                    if ( mmtp01 != null ) {
                        outputStream.write(mmtp01.getSignallingMessage().getMessageByte());
                        m_fragmented01_mmtp.remove(mmtp.getPacketSequenceNumber()-2);
                    }
                    
                    if ( mmtp02 != null ) {
                        outputStream.write(mmtp02.getSignallingMessage().getMessageByte());
                        m_fragmented02_mmtp.remove(mmtp.getPacketSequenceNumber()-1);
                    }
                    outputStream.write(signal_message.getMessageByte());
                    message = MessageFactory.createMessage(outputStream.toByteArray());
                    if (message != null ) return message.getTables();
                    break;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Only for debugging to print all of a TLV
     * @param table
     */
    protected void print(TypeLengthValue tlv) {
        Logger.p(String.format("[%d] TLV \n", tlv_sample_counter++));
        tlv.print();
    }
    
    /**
     * Only for debugging to print all of table fields
     * @param table
     */
    protected void print(Table table) {
        Logger.p(String.format("[%d] Table (id : 0x%x) \n", 
                table_sample_counter++, table.getTableId()));
        table.print();
    }
    
    /**
     * Enable all of table of filters which application want to receive 
     */
    public void enableAllOfTableFilter() {
        m_enable_all_of_table_filter = true;
    }
    
    /**
     * Enable table filters which application only want to receive
     * @param table_ids
     */
    public void enableTableFilter(List<Byte> table_ids) {
        m_enable_table_filter = true;
        m_table_id_filters = table_ids;
    }

    /**
     * Disable table filters if application doesn't want to receive
     * @param table_ids
     */
    public void disableTableFilter() {
        m_enable_all_of_table_filter = false;
        m_enable_table_filter = false;
    }
    
    /**
     * Enable NTP data if application want to receive
     */
    public void enableNtpFilter() {
        m_enable_ntp_filter = true;
    }
    
    /**
     * Disable NTP data if application doesn't want to receive 
     */
    public void disableNtpFilter() {
        m_enable_ntp_filter = false;
    }

}