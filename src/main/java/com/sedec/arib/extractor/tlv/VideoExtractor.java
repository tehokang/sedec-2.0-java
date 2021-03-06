package com.sedec.arib.extractor.tlv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.sedec.arib.tlv.container.PacketFactory;
import com.sedec.arib.tlv.container.mmtp.MMTP_Packet;
import com.sedec.arib.tlv.container.packets.CompressedIpPacket;
import com.sedec.arib.tlv.container.packets.TypeLengthValue;
import com.sedec.util.Logger;

/**
 * Class to extract Video as MFU from MPU.
 * It has inherited from BaseExtractor which has already implementations to get MPU-MFU.
 * {@link BaseExtractor}
 *
 * <p>
 * Video MFU can automatically include NAL prefix by {@link BaseExtractor#enablePreModification()}.
 * Then user can get video having prefix as NAL bytes.
 * User can receive video via {@link VideoExtractor.IVideoExtractorListener#onReceivedVideo(int, int, int, byte[])}
 */
public class VideoExtractor extends BaseExtractor {
    protected static final String TAG = VideoExtractor.class.getSimpleName();

    /**
     * Listener to receive MFU of video
     */
    public interface IVideoExtractorListener extends BaseExtractor.Listener {
        /**
         * Receives video MFU which already gathered from fragmentation.
         * @param packet_id MMT packet id
         * @param buffer MFU_data_byte as timed data of Table 6-1 MMTP_payload
         */
        public void onReceivedVideo(int packet_id, int mpu_sequence_number, int sample_number, byte[] buffer);
    }

    protected class QueueData extends BaseExtractor.QueueData {
        public int sample_number;
        public int mpu_sequence_number;

        public QueueData(int pid, int mpu_sequence_number, int sample_number, byte[] data) {
            super(pid, data);

            this.sample_number = sample_number;
            this.mpu_sequence_number = mpu_sequence_number;
        }
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
                                (data = (QueueData) m_event_queue.take()) != null ) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((IVideoExtractorListener)m_listeners.get(i)).
                                        onReceivedVideo(data.packet_id,
                                                data.mpu_sequence_number,
                                                data.sample_number, data.data);
                            }
                        }
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG, "Error while emitting events \n");
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * Nothing to do
                         */
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        }, TAG);
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
                 * MPU-MFU
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
                            putOut(new QueueData(mmtp_packet.getPacketId(),
                                    mmtp_packet.getMPU().getMPUSequenceNumber(),
                                    ((mmtp_packet.getMPU().getAggregationFlag() == 0) ? -1 : i),
                                    sample_binary));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
