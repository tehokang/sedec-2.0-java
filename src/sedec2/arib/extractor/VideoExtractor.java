package sedec2.arib.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;

public class VideoExtractor extends BaseExtractor {
    protected final String TAG = "VideoExtractor";

    public interface IVideoExtractorListener extends BaseExtractor.Listener {
        public void onReceivedVideo(int packet_id, byte[] buffer);
    }
    
    public VideoExtractor() {
        super();
        
        m_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                QueueData data = null;
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(0, m_sleep_micro_interval);
                        if ( null != m_event_queue && 
                                (data = m_event_queue.take()) != null ) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((IVideoExtractorListener)m_listeners.get(i)).
                                        onReceivedVideo(data.packet_id, data.data);
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
    
    @Override
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
                        showMMTPInfo("VIDEO", mmtp_packet);
                        
                        byte[] nal_prefix = {0x00, 0x00, 0x00, 0x01};
                        List<ByteArrayOutputStream> samples = getMFU(mmtp_packet);
                        for ( int i=0; i<samples.size(); i++ ) {
                            ByteArrayOutputStream sample = samples.get(i);
                            byte[] sample_binary = sample.toByteArray();
                            if ( m_enable_pre_modification == true ) { 
                                System.arraycopy(nal_prefix, 0, 
                                        sample_binary, 0, nal_prefix.length);
                            }
                            putOut(new QueueData(mmtp_packet.getPacketId(), sample_binary));
                        }
                    } 
                }
                break;
            default:
                break;
        }
    }
}
