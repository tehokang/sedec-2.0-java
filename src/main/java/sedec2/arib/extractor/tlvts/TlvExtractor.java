package sedec2.arib.extractor.tlvts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import sedec2.arib.tlvts.container.packets.TlvTransportStream;
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
     * Processes to get TLV and put it into output queue to be sent user
     *
     * @param ts one TS packet
     */
    protected synchronized void process(TlvTransportStream tlvts)
            throws InterruptedException, IOException {
        /**
         * PID filter by TS PID
         */
        if ( m_int_id_filter.contains(tlvts.getPID()) == false ) return;

        ByteArrayOutputStream tlv_buffer = m_fragmented_tlvts.get(tlvts.getPID());
        if ( tlvts.getTLVStartIndicator() == 0x01 ) {
            if ( null == tlv_buffer ) {
                /**
                 * Put begin of TLV
                 */
                tlv_buffer = new ByteArrayOutputStream();
                tlv_buffer.write(tlvts.getFragmentedTlvPacket(), 1,
                        tlvts.getFragmentedTlvPacket().length-1);
                m_fragmented_tlvts.put(tlvts.getPID(), tlv_buffer);
            } else {
                /**
                 * Put previous buffered sections out since new TLV come here
                 * with being careful whether current package has pointer field which
                 * refers to previous TLV data or not.
                 */
                int remaining = 0xff & tlvts.getPointerField();
                if ( remaining > 0x00 ) {
                    tlv_buffer.write(tlvts.getFragmentedTlvPacket(), 1, remaining);
                }
                putOut(new QueueData(tlvts.getPID(), tlv_buffer.toByteArray()));

                /**
                 * Clear buffer
                 */
                tlv_buffer.reset();
                m_fragmented_tlvts.remove(tlvts.getPID());

                /**
                 * Put new TLV into buffer
                 */
                tlv_buffer.write(tlvts.getFragmentedTlvPacket(), 1 + remaining,
                        tlvts.getFragmentedTlvPacket().length-1-remaining);
                m_fragmented_tlvts.put(tlvts.getPID(), tlv_buffer);
            }
        } else {
            /**
             * Put middle of TLV
             */
            if ( tlv_buffer != null ) {
                tlv_buffer.write(tlvts.getFragmentedTlvPacket());
            }
        }
    }
}
