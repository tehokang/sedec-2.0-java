package sedec2.arib.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.mmtp.MMTP_Packet;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.base.BitReadWriter;

public class AudioExtractor extends BaseExtractor {
    protected final String TAG = "AudioExtractor";

    public interface IAudioExtractorListener extends BaseExtractor.Listener {
        public void onReceivedAudio(int packet_id, byte[] buffer);
    }

    public AudioExtractor() {
        super();

        m_event_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                QueueData data = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_event_queue && ( data = m_event_queue.take()) != null) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((IAudioExtractorListener)m_listeners.get(i)).
                                        onReceivedAudio(data.packet_id, data.data);
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
    @Override
    public void destroy() {
        super.destroy();

        m_event_thread.interrupt();
        m_event_thread = null;
    }

    /**
     * Chapter 8 of ARIB-B60v1-12
     * process function send QueueData with audio data having syncword as prefix
     */
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
                        BitReadWriter syncword = null;
                        ByteArrayOutputStream out = null;
                        List<ByteArrayOutputStream> samples = getMFU(mmtp_packet);

                        for ( int i=0; i<samples.size(); i++ ) {
                            ByteArrayOutputStream sample = samples.get(i);
                            byte[] sample_binary = sample.toByteArray();
                            out = new ByteArrayOutputStream();

                            if ( m_enable_pre_modification == true ) {
                                syncword = new BitReadWriter(new byte[3]);
                                syncword.writeOnBuffer(0x2b7, 11);
                                syncword.writeOnBuffer(sample_binary.length, 13);
                                out.write(syncword.getBuffer());
                            }
                            out.write(sample_binary);
                            putOut(new QueueData(
                                    mmtp_packet.getPacketId(),
                                    out.toByteArray()));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
