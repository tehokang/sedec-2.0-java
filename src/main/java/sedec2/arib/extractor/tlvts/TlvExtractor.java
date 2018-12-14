package sedec2.arib.extractor.ts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.mmtp.MMTP_Packet;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.util.Logger;

public class TlvExtractor extends BaseExtractor {
    protected static final String TAG = TlvExtractor.class.getSimpleName();

    /**
     * Listener to receive a TLV packet
     */
    public interface ITlvExtractorListener extends BaseExtractor.Listener {
        /**
         * Receives a TLV packet which already gathered from fragmentation.
         * @param PID of TS which's including the TLV
         * @param buffer TLV packet
         */
        public void onReceivedTlv(int packet_id, byte[] buffer);
    }

    /**
     * Constructor which start running thread to emit Event to user.
     */
    public TlvExtractor() {
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
                                ((ITlvExtractorListener)m_listeners.get(i)).
                                        onReceivedTlv(data.packet_id, data.data);
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
