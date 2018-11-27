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
 * Class to extract TTML as MFU from MPU.
 * It has inherited from BaseExtractor which has already implementations to get MPU-MFU.
 * {@link BaseExtractor}
 *
 * <p>
 * User can receive TTML via
 * {@link TtmlExtractor.ITtmlExtractorListener#onReceivedTtml(int, byte[])}
 */
public class TtmlExtractor extends BaseExtractor {
    protected final String TAG = "TtmlExtractor";

    public interface ITtmlExtractorListener extends BaseExtractor.Listener {
        public void onReceivedTtml(int packet_id, byte[] buffer);
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
                                (data = m_event_queue.take()) != null ) {
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
                 * @note MPU-MFU
                 */
                if ( 0x00 == mmtp_packet.getPayloadType() ) {
                    if ( m_int_id_filter.contains(mmtp_packet.getPacketId()) ) {
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
