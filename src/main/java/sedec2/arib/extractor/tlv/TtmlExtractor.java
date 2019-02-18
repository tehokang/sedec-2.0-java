package sedec2.arib.extractor.tlv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.mmtp.MMTP_Packet;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.util.Logger;

/**
 * Class to extract TTML as MFU from MPU.
 * It has inherited from BaseExtractor which has already implementations to get MPU-MFU.
 * {@link BaseExtractor}
 *
 * <p>
 * User can receive TTML via
 * {@link TtmlExtractor.ITtmlExtractorListener#onReceivedTtml(int, byte[])}
 */
public class TtmlExtractor extends BaseExtractor {
    protected static final String TAG = TtmlExtractor.class.getSimpleName();

    /**
     * Listener to receive TTML of chapter 9 of ARIB B60
     */
    public interface ITtmlExtractorListener extends BaseExtractor.Listener {
        /**
         * Receives TTML MFU which already gathered from fragmentation.
         * @param packet_id MMT packet id
         * @param buffer MFU_data_byte as non-timed data of Table 6-1, 9-1 MMTP_payload
         */
        public void onReceivedTtml(int packet_id, byte[] buffer);
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
    public TtmlExtractor() {
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
                                ((ITtmlExtractorListener)m_listeners.get(i)).
                                        onReceivedTtml(data.packet_id, data.data);
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
     * Chapter 9 of ARIB-B60v1-12
     * Processes to put QueueData with TTML into event queue
     *
     * @param one TLV packet
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
                        List<ByteArrayOutputStream> samples = getMFU(mmtp_packet);
                        for ( int i=0; i<samples.size(); i++ ) {
                            ByteArrayOutputStream sample = samples.get(i);
                            putOut(new QueueData(
                                    mmtp_packet.getPacketId(),
                                    mmtp_packet.getMPU().getMPUSequenceNumber(),
                                    i,
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
