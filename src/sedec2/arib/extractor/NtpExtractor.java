package sedec2.arib.extractor;

import java.io.IOException;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.IPv4Packet;
import sedec2.arib.tlv.container.packets.IPv6Packet;
import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.arib.tlv.container.packets.TypeLengthValue;

public class NtpExtractor extends BaseExtractor {
    protected final String TAG = "NtpExtractor";

    public interface INtpExtractorListener extends BaseExtractor.Listener {
        public void onReceivedNtp(NetworkTimeProtocolData ntp);
    }
    
    public class QueueData extends BaseExtractor.QueueData {
        public NetworkTimeProtocolData ntp;
        
        public QueueData(NetworkTimeProtocolData ntp) {
            this.ntp = ntp;
        }
    }
    
    public NtpExtractor() {
        super();
        
        m_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                QueueData data = null;
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_event_queue && 
                                (data = (QueueData) m_event_queue.take()) != null ) {
                            
                            NetworkTimeProtocolData ntp = data.ntp;
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((INtpExtractorListener)m_listeners.get(i)).
                                        onReceivedNtp(ntp);
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
        m_event_thread.start();
    }
    
    /**
     * User should use this function when they don't use TLVExtractor any more.
     */
    public void destroy() {
        super.destroy();
        
        m_event_thread.interrupt();
        m_event_thread = null;
    }
    
    protected synchronized void process(TypeLengthValue tlv) 
            throws InterruptedException, IOException {
        switch ( tlv.getPacketType() ) {
            case PacketFactory.IPV4_PACKET:
                NetworkTimeProtocolData ipv4_ntp = ((IPv4Packet)tlv).getNtp();
                if ( ipv4_ntp != null ) {
                    putOut(new QueueData(ipv4_ntp));
                    if ( m_enable_logging == true ) {
                        ipv4_ntp.print();
                    }
                }
                
                break;
            case PacketFactory.IPV6_PACKET:
                NetworkTimeProtocolData ipv6_ntp = ((IPv6Packet)tlv).getNtp();
                if ( ipv6_ntp != null ) {
                    putOut(new QueueData(ipv6_ntp));
                    if ( m_enable_logging == true ) ipv6_ntp.print();
                }
                break;
            default:
                break;
        }
    }
    
}
