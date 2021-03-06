package com.sedec.arib.extractor.ts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sedec.arib.ts.container.packets.TransportStream;
import com.sedec.util.Logger;

/**
 * Class to extract audio from Transport Stream of ISO-13818
 * It has inherited from BaseExtractor which has already implementations of Extractor interfaces.
 * {@link BaseExtractor}
 */
public class AudioExtractor extends BaseExtractor {
    protected static final String TAG = AudioExtractor.class.getSimpleName();

    /**
     * Listener to receive stream as audio type of Table 2-36 of ISO 13818
     */
    public interface IAudioExtractorListener extends BaseExtractor.Listener {
        /**
         * Receives audio raw data
         * @param packet_id PID of TS
         * @param data audio raw data
         */
        public void onReceivedAudio(int packet_id, byte[] data);
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
                        if ( null != m_event_queue &&
                                ( data = m_event_queue.take()) != null ) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((IAudioExtractorListener)m_listeners.get(i)).
                                        onReceivedAudio(data.packet_id, data.data);
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
     * Chapter 2 of ISO-13818
     * Processes to put QueueData with audio as raw
     *
     * @param ts TS packet
     */
    protected synchronized void process(TransportStream ts)
            throws InterruptedException, IOException {
        /**
         * PID filter by TS PID
         */
        if ( m_int_id_filter.contains(ts.getPID()) == false ) return;

        ByteArrayOutputStream audio_buffer = m_fragmented_transport_stream.get(ts.getPID());
        if ( ts.getPayloadUnitStartIndicator() == 0x01 ) {
            if ( null == audio_buffer ) {
                /**
                 * Put begin of audio
                 */
                audio_buffer = new ByteArrayOutputStream();
                audio_buffer.write(ts.getDataByte(), 0, ts.getDataByte().length);
                m_fragmented_transport_stream.put(ts.getPID(), audio_buffer);
            } else {
                putOut(new QueueData(ts.getPID(), audio_buffer.toByteArray()));

                /**
                 * Clear buffer
                 */
                audio_buffer.reset();
                m_fragmented_transport_stream.remove(ts.getPID());

                /**
                 * Put new audio into buffer
                 */
                audio_buffer.write(ts.getDataByte(), 0, ts.getDataByte().length);
                m_fragmented_transport_stream.put(ts.getPID(), audio_buffer);
            }
        } else {
            /**
             * Put middle of audio
             */
            if ( audio_buffer != null ) {
                audio_buffer.write(ts.getDataByte());
            }
        }
    }
}
