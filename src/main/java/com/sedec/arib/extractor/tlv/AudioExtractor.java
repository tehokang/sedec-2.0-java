package com.sedec.arib.extractor.tlv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.sedec.arib.tlv.container.PacketFactory;
import com.sedec.arib.tlv.container.mmtp.MMTP_Packet;
import com.sedec.arib.tlv.container.packets.CompressedIpPacket;
import com.sedec.arib.tlv.container.packets.TypeLengthValue;
import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

/**
 * Class to extract Audio as MFU from MPU.
 * It has inherited from BaseExtractor which has already implementations to get MPU-MFU.
 * {@link BaseExtractor}
 *
 * <p>
 * Audio MFU can automatically include prefix like sync bytes by {@link BaseExtractor#enablePreModification()}.
 * Then user can get audio having prefix as synchronization byte and length in AudioSyncStream.
 * User can receive audio via {@link AudioExtractor.IAudioExtractorListener#onReceivedAudio(int, int, int, byte[])}
 */
public class AudioExtractor extends BaseExtractor {
    protected static final String TAG = AudioExtractor.class.getSimpleName();

    /**
     * Listener to receive MFU of audio
     */
    public interface IAudioExtractorListener extends BaseExtractor.Listener {
        /**
         * Receives audio MFU which already gathered from fragmentation.
         * @param packet_id MMT packet id
         * @param buffer MFU_data_byte as timed data of Table 6-1 MMTP_payload
         */
        public void onReceivedAudio(int packet_id, int mpu_sequence_number, int sample_number, byte[] buffer);
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
    public AudioExtractor() {
        super();

        m_event_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                QueueData data = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_event_queue && ( data = (QueueData) m_event_queue.take()) != null) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((IAudioExtractorListener)m_listeners.get(i)).
                                        onReceivedAudio(data.packet_id, data.mpu_sequence_number, data.sample_number, data.data);
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

    /**
     * User should use this function when they don't use TLVExtractor any more.
     */
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
                        BitReadWriter syncword = null;
                        ByteArrayOutputStream out = null;
                        List<ByteArrayOutputStream> samples = getMFU(mmtp_packet);
                        int sample_number = -1;
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
                            if(mmtp_packet.getMPU().getAggregationFlag() == 0 ) {
                                int indicator = mmtp_packet.getMPU().getFragmentationIndicator();
                                if(indicator == 0 || indicator == 1) {
                                    sample_number = 0;
                                }
                            } else {
                                sample_number = 0;
                            }
                            putOut(new QueueData(
                                    mmtp_packet.getPacketId(),
                                    mmtp_packet.getMPU().getMPUSequenceNumber(),
                                    sample_number,
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
