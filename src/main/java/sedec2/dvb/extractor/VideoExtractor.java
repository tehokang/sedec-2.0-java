package sedec2.dvb.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import sedec2.dvb.ts.container.packets.TransportStream;
import sedec2.util.Logger;
import sedec2.util.TimeStampUtil;

/**
 * Class to extract video from Transport Stream of ISO-13818
 * It has inherited from BaseExtractor which has already implementations of Extractor interfaces.
 * {@link BaseExtractor}
 */
public class VideoExtractor extends BaseExtractor {
    protected static final String TAG = VideoExtractor.class.getSimpleName();

    private long pts;

    /**
     * Listener to receive stream as video type of Table 2-36 of ISO 13818
     */
    public interface IVideoExtractorListener extends BaseExtractor.Listener {
        /**
         * Receives video raw data
         * @param packet_id PID of TS
         * @param data video raw data
         */
        public void onReceivedVideo(int packet_id, byte[] data, long pts);
    }

    public class QueueData extends BaseExtractor.QueueData {
        public long pts;

        public QueueData(int pid, byte[] data, long pts) {
            this.packet_id = pid;
            this.data = data;
            this.pts = pts;
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
                                ( data = (QueueData)m_event_queue.take()) != null ) {
                             for ( int i=0; i<m_listeners.size(); i++ ) {
                                 ((IVideoExtractorListener)m_listeners.get(i)).
                                         onReceivedVideo(data.packet_id, data.data, data.pts);
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
     * Processes to put QueueData with video as raw
     *
     * @param ts TS packet
     */
    protected synchronized void process(TransportStream ts)
            throws InterruptedException, IOException {
        /**
         * PID filter by TS PID
         */
        if ( m_int_id_filter.contains(ts.getPID()) == false ) return;

        ByteArrayOutputStream video_buffer = m_fragmented_transport_stream.get(ts.getPID());
        if ( ts.getPayloadUnitStartIndicator() == 0x01 ) {
            if ( null == video_buffer ) {
                video_buffer = new ByteArrayOutputStream();
            }
            else {
                putOut(new QueueData(ts.getPID(), video_buffer.toByteArray(), pts));

                /**
                 * Clear buffer
                 */
                video_buffer.reset();
                m_fragmented_transport_stream.remove(ts.getPID());
            }

            /**
             * Get PTS
             */
            pts = TimeStampUtil.getPtsFromTS(ts);

            /**
             * Put new video into buffer without PES header 6 bytes especially only for video
             */
            video_buffer.write(ts.getDataByte(), 6, ts.getDataByte().length-6);
            m_fragmented_transport_stream.put(ts.getPID(), video_buffer);
        } else {
            /**
             * Put middle of video
             */
            if ( video_buffer != null ) {
                video_buffer.write(ts.getDataByte());
            }
        }
    }
}
