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
    
    protected Thread m_table_event_thread;
    protected Thread m_ntp_event_thread;
    protected Thread m_tlv_extractor_thread;
    protected boolean m_is_running = true;
    
    protected List<ITLVExtractorListener> m_listeners = new ArrayList<>();

    protected BlockingQueue<NetworkTimeProtocolData> m_ntps = 
            new ArrayBlockingQueue<NetworkTimeProtocolData>(100);
    protected BlockingQueue<Table> m_tables = new ArrayBlockingQueue<Table>(100);
    protected BlockingQueue<byte[]> m_tlv_packets = new ArrayBlockingQueue<byte[]>(100);
    protected Map<Integer, MMTP_Packet> fragmented01_mmtp = 
            new HashMap<Integer, MMTP_Packet>();
    protected Map<Integer, MMTP_Packet> fragmented02_mmtp = 
            new HashMap<Integer, MMTP_Packet>();
    
    /**
     * @note sample_counter and print function is only for testing
     */
    long sample_counter = 0;
    public void print(TypeLengthValue tlv) {
        Logger.p(String.format("[%d] TLV \n", sample_counter++));
        tlv.print();
    }
    
    public void print(Table table) {
        Logger.p(String.format("[%d] TLV (%d) \n", sample_counter++, table.getTableId()));
        table.print();
    }
    
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
                                    
                                    if ( mmtp_packet == null ) continue;
                                    
                                    /**
                                     * @note Signaling Message
                                     */
                                    if ( 0x02 == mmtp_packet.getPayloadType() ) {
                                        List<Table> tables = 
                                                processMmtp_SignallingMessage(mmtp_packet);
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
                        m_is_running = false;
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
                        if ( null != m_ntps ) {                           
                            NetworkTimeProtocolData ntp = m_ntps.take();
                            
                            if ( ntp != null ) {
                                for ( int i=0; i<m_listeners.size(); i++ ) {
                                    m_listeners.get(i).onNtpUpdated(ntp);
                                }
                            }
                        }
                    } catch ( Exception e ) {
                        /**
                         * @todo You should remove a line below, because TLVExtractor \n
                         * has to keep alive even though TLVExtractor get any wrong packets.
                         */
                        m_is_running = false;
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
                        if ( null != m_tables ) {                           
                            Table table = m_tables.take();
                            
                            if ( table != null ) {
                                for ( int i=0; i<m_listeners.size(); i++ ) {
                                    m_listeners.get(i).onReceivedTable(table);
                                }
                            }
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
                        m_is_running = false;
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
    }
    
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
    public synchronized boolean put(byte[] tlv) {
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
     * ARIB B60 6.3.2 Configuration of MMTP Payload
     * @param mmtp MMTP_Packet
     * @return tables of MMT-SI
     * @throws IOException
     */
    protected List<Table> processMmtp_SignallingMessage(MMTP_Packet mmtp) throws IOException {
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
                    fragmented01_mmtp.put(mmtp.getPacketSequenceNumber(), mmtp);
                    break;
                case 0x02:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves a part of divided data which is \n
                     * neither header part nor last part.
                     */
                    fragmented02_mmtp.put(mmtp.getPacketSequenceNumber(), mmtp);
                    break;
                case 0x03:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves last part of divided data.
                     */
                    MMTP_Packet mmtp01 = fragmented01_mmtp.get(mmtp.getPacketSequenceNumber()-2);
                    MMTP_Packet mmtp02 = fragmented02_mmtp.get(mmtp.getPacketSequenceNumber()-1);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    
                    if ( mmtp01 != null ) {
                        outputStream.write(mmtp01.getSignallingMessage().getMessageByte());
                        fragmented01_mmtp.remove(mmtp.getPacketSequenceNumber()-2);
                    }
                    
                    if ( mmtp02 != null ) {
                        outputStream.write(mmtp02.getSignallingMessage().getMessageByte());
                        fragmented02_mmtp.remove(mmtp.getPacketSequenceNumber()-1);
                    }
                    outputStream.write(signal_message.getMessageByte());
                    message = MessageFactory.createMessage(outputStream.toByteArray());
                    if (message != null ) return message.getTables();
                    break;
            }
        }
        return new ArrayList<>();
    }
}