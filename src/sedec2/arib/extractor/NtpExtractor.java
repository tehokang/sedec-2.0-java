package sedec2.arib.extractor;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.IPv4Packet;
import sedec2.arib.tlv.container.packets.IPv6Packet;
import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.arib.tlv.container.packets.TypeLengthValue;

public class NtpExtractor extends Extractor {
    public interface INtpExtractorListener extends Extractor.Listener {
        public void onReceivedNtp(NetworkTimeProtocolData ntp);
    }

    protected final String TAG = "TlvNtpExtractor";
    
    protected boolean m_is_running = true;
    protected Thread m_ntp_event_thread;
    protected Thread m_tlv_extractor_thread;
    
    protected BlockingQueue<NetworkTimeProtocolData> m_ntps = 
            new ArrayBlockingQueue<NetworkTimeProtocolData>(100);
    protected BlockingQueue<byte[]> m_tlv_packets = new ArrayBlockingQueue<byte[]>(100);
    
    public NtpExtractor() {
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
        
        m_ntp_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkTimeProtocolData ntp = null;
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_ntps && 
                                (ntp = m_ntps.take()) != null && 
                                m_enable_filter == true ) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((INtpExtractorListener)m_listeners.get(i)).onReceivedNtp(ntp);
                            }
                        }
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
        m_ntp_event_thread.start();
    }
    
    /**
     * User should use this function when they don't use TLVExtractor any more.
     */
    public void destroy() {
        m_is_running = false;
        
        m_tlv_extractor_thread.interrupt();
        m_tlv_extractor_thread = null;
        
        m_ntp_event_thread.interrupt();
        m_ntp_event_thread = null;
        
        m_listeners.clear();
        m_listeners = null;
                
        m_tlv_packets.clear();
        m_tlv_packets = null;
        
        m_ntps.clear();
        m_ntps = null;
    }
    
    /**
     * User can put a TLV packet to get the results as Table of MMT-SI, TLV-SI and more.
     * @param tlv a variable TLV packet
     * @return Return false if TLVExtractor has situation which can't parse like overflow.
     */
    public void putIn(byte[] tlv) throws InterruptedException {
        if ( m_is_running == true && m_tlv_packets != null && tlv != null ) {
            m_tlv_packets.put(tlv);
        }
    }

    /**
     * Extractor can send NTP via this function as asynchronous event
     * @param ntp
     * @throws InterruptedException
     */

    @Override
    protected void putOut(Object object) throws InterruptedException {
        m_ntps.put((NetworkTimeProtocolData)object);
    }
    
    protected synchronized void process(TypeLengthValue tlv) 
            throws InterruptedException, IOException {
        switch ( tlv.getPacketType() ) {
            case PacketFactory.IPV4_PACKET:
                NetworkTimeProtocolData ipv4_ntp = ((IPv4Packet)tlv).getNtp();
                if ( ipv4_ntp != null ) putOut(ipv4_ntp);
                break;
            case PacketFactory.IPV6_PACKET:
                NetworkTimeProtocolData ipv6_ntp = ((IPv6Packet)tlv).getNtp();
                if ( ipv6_ntp != null ) putOut(ipv6_ntp);
                break;
            case PacketFactory.SIGNALLING_PACKET:
            case PacketFactory.COMPRESSED_IP_PACKET:
            default:
                break;
        }
    }
    
}
