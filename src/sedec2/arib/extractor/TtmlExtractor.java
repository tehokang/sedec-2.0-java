package sedec2.arib.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;

public class TtmlExtractor extends BaseExtractor {
    public interface ITtmlExtractorListener extends BaseExtractor.Listener {
        public void onReceivedTtml(int packet_id, byte[] buffer);
    }

    protected final String TAG = "TtmlExtractor";
    protected Thread m_mfu_ttml_event_thread;
    
    public TtmlExtractor() {
        super();
        
        m_mfu_ttml_event_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                QueueData data = null;
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_event_queue &&  (data = m_event_queue.take()) != null ) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((ITtmlExtractorListener)m_listeners.get(i)).
                                        onReceivedTtml(data.packet_id, data.data);
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
        m_mfu_ttml_event_thread.start();
    }
    
    /**
     * User should use this function when they don't use TLVExtractor any more.
     */
    public void destroy() {
        super.destroy();
        
        m_mfu_ttml_event_thread.interrupt();
        m_mfu_ttml_event_thread = null;
    }
    
    protected synchronized void process(TypeLengthValue tlv) 
            throws InterruptedException, IOException {
        switch ( tlv.getPacketType() ) {
            case PacketFactory.COMPRESSED_IP_PACKET:
                CompressedIpPacket cip = (CompressedIpPacket) tlv;
                MMTP_Packet mmtp_packet = cip.getPacketData().mmtp_packet;
                
                if ( mmtp_packet == null ) break;
                
                /**
                 * @note MPU-MFU
                 */
                if ( 0x00 == mmtp_packet.getPayloadType() ) {
                    if ( m_int_id_filter.contains(mmtp_packet.getPacketId()) ) {
                        showMMTPInfo("TTML", mmtp_packet);

                        
                        List<ByteArrayOutputStream> samples = getMFU(mmtp_packet);
                        for ( int i=0; i<samples.size(); i++ ) {
                            ByteArrayOutputStream sample = samples.get(i);
                            putOut(new QueueData(
                                    mmtp_packet.getPacketId(), 
                                    sample.toByteArray()));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
