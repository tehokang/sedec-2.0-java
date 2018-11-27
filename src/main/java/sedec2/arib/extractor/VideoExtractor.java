package sedec2.arib.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.mmtp.MMTP_Packet;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.util.Logger;

/**
 * Class to extract Video as MFU from MPU.
 * It has inherited from BaseExtractor which has already implementations to get MPU-MFU.
 * {@link BaseExtractor}
 *
 * <p>
 * Video MFU can automatically include NAL prefix by {@link BaseExtractor#enablePreModification()}.
 * Then user can get video having prefix as NAL bytes.
 * User can receive video via {@link VideoExtractor.IVideoExtractorListener#onReceivedVideo(int, byte[])}
 */
public class VideoExtractor extends BaseExtractor {
    protected final String TAG = "VideoExtractor";

    /**
     * Listener to receive MFU of video
     */
    public interface IVideoExtractorListener extends BaseExtractor.Listener {
        /**
         * Receives video MFU which already gathered from fragmentation.
         * @param packet_id MMT packet id
         * @param buffer MFU_data_byte as timed data of Table 6-1 MMTP_payload
         */
        public void onReceivedVideo(int packet_id, byte[] buffer);
    }

    /**
     * Constructor which start running thread to emit Event to user.
     */
    public VideoExtractor() {
        super();

        m_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                QueueData data = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_event_queue &&
                                (data = m_event_queue.take()) != null ) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((IVideoExtractorListener)m_listeners.get(i)).
                                        onReceivedVideo(data.packet_id, data.data);
                            }
                        }
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG, "Error while emitting events \n");
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

    @Override
    public void destroy() {
        super.destroy();

        m_event_thread.interrupt();
        m_event_thread = null;
    }

    @Override
    /**
     * Chapter 8 of ARIB-B60v1-12
     * Processes to get MPU-MFU and put it into output queue to be sent user
     *
     * @param tlv one TLV packet
     */
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
