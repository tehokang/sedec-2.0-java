package com.sedec.dvb.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.sedec.dvb.ts.container.PacketFactory;
import com.sedec.dvb.ts.container.packets.TransportStream;
import com.sedec.util.Logger;

/**
 * BaseExtractor has implementations which are able to parse TS packet.
 */
public abstract class BaseExtractor {
    protected static final String TAG = BaseExtractor.class.getSimpleName();

    /**
     * Flag which can stop thread or run.
     */
    protected boolean m_is_running = true;

    /**
     * Flag which can show logging of extractors
     */
    protected boolean m_enable_logging = false;

    /**
     * Flag which can modify MFU before getting from extactors.
     * Implementor should implement scenario to modify when the flag set as true.
     */
    protected boolean m_enable_pre_modification = true;

    /**
     * Event listener list
     */
    protected List<Listener> m_listeners = new ArrayList<>();

    /**
     * Filter as int like packet_id of MMTP Packet
     */
    protected List<Integer> m_int_id_filter = new ArrayList<>();

    /**
     * Event thread to emit event as asynchronous to user.
     * Child should put Thread instance into this member variable.
     */
    protected Thread m_event_thread;

    /**
     * Extracting thread which can pull out a TS packet from TS packets queue
     */
    protected Thread m_ts_raw_extractor_thread;

    /**
     * Extracting thread which can pull out a TS packet from TS packets formatted queue
     */
    protected Thread m_ts_formatted_extractor_thread;

    /**
     * TS packets queue
     */
    protected BlockingQueue<byte[]> m_ts_raw_packets = null;

    /**
     * TS packets queue as being formatted
     */
    protected BlockingQueue<TransportStream> m_ts_formatted_packets = null;

    /**
     * Event queue which can sent to user
     */
    protected BlockingQueue<QueueData> m_event_queue = null;

    protected Map<Integer, ByteArrayOutputStream> m_fragmented_transport_stream = new HashMap<>();

    /**
     * Every Extractor has to inherit from this so that user can use unified listener.
     */
    public interface Listener {

    }

    protected class QueueData {
        public int packet_id;
        public byte[] data;

        public QueueData() {

        }

        public QueueData(int pid, byte[] data) {
            this.packet_id = pid;
            this.data = data;
        }
    }

    /**
     * Constructor which initialize queue buffer of TS input packet, Event and
     * start running thread with blocking queue which only run when queue obtain an input.
     */
    public BaseExtractor() {
        m_ts_raw_packets = new ArrayBlockingQueue<byte[]>(100);
        m_ts_formatted_packets = new ArrayBlockingQueue<TransportStream>(100);
        m_event_queue = new ArrayBlockingQueue<QueueData>(100);

        m_ts_raw_extractor_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TransportStream ts = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_ts_raw_packets ) {
                            byte[] ts_raw = m_ts_raw_packets.take();
                            ts = PacketFactory.createPacket(ts_raw);

                            if ( ts != null ) process(ts);
                        }

                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG,
                                String.format("Error while parsing TS \n"));
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * Nothing to do
                         */
                    } catch ( Exception e ) {
                        /**
                         * You should remove a line below like break, exit statement,
                         * because TsExtractor has to keep alive even though
                         * TsExtractor get any wrong packets.
                         */
                        e.printStackTrace();
                    }
                }
            }
        }, TAG);

        m_ts_formatted_extractor_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TransportStream ts = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_ts_formatted_packets ) {
                            ts = m_ts_formatted_packets.take();

                            if ( ts != null ) process(ts);
                        }

                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG,
                                String.format("Error while parsing TS \n"));
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * Nothing to do
                         */
                    } catch ( Exception e ) {
                        /**
                         * You should remove a line below like break, exit statement,
                         * because TLVExtractor has to keep alive even though
                         * TLVExtractor get any wrong packets.
                         */
                        e.printStackTrace();
                    }
                }
            }
        }, TAG);

        m_ts_formatted_extractor_thread.start();
        m_ts_raw_extractor_thread.start();
    }

    /**
     * Clear all of queue containing TS, and fragmented packets.
     */
    public void clearQueue() {
        m_ts_formatted_packets.clear();
        m_ts_raw_packets.clear();
        m_event_queue.clear();

        m_fragmented_transport_stream.clear();
    }

    /**
     * Clean up all of resource under BaseExtractor.
     * User must call this after their job finished.
     */
    public void destroy() {
        m_is_running = false;
        m_int_id_filter.clear();

        m_ts_formatted_extractor_thread.interrupt();
        m_ts_formatted_extractor_thread = null;

        m_ts_raw_extractor_thread.interrupt();
        m_ts_raw_extractor_thread = null;

        clearQueue();

        m_event_queue = null;
        m_ts_raw_packets = null;
        m_ts_formatted_packets = null;
        m_fragmented_transport_stream = null;

        m_listeners.clear();
        m_listeners = null;
    }

    /**
     * User can add event-listener to get information from each extractor
     * @param listener to get information from Extractor
     */
    public void addEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }

    /**
     * User can remove event-listener not to get information
     * @param listener which added
     */
    public void removeEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == true ) {
            m_listeners.remove(listener);
        }
    }

    /**
     * User can get size of remaining packets.
     * @return size of remaining packets
     */
    public int getRemainingPackets() {
        if ( m_is_running == true && m_ts_raw_packets != null) {
            return m_ts_raw_packets.size();
        }
        return 0;
    }

    /**
     * User should put a TS packet into extractor, the packet will be collected as kinds of them
     * @param ts one TS packet as being raw
     * @throws InterruptedException occur when thread interrupted
     */
    public void putIn(byte[] ts) throws InterruptedException {
        if ( m_is_running == true && m_ts_raw_packets != null && ts != null ) {
            m_ts_raw_packets.put(ts);
        }
    }

    /**
     * User should put a TS packet into extractor, the packet will be collected as kinds of them
     * @param ts one TS packet
     * @throws InterruptedException occur when thread interrupted
     */
    public void putIn(TransportStream ts) throws InterruptedException {
        if ( m_is_running == true && m_ts_raw_packets != null && ts != null ) {
            m_ts_formatted_packets.put(ts);
        }
    }

    /**
     * Child extractor of BaseExtractor must implement process function to control TS packets
     * @param ts one TS packet already decoded
     * @throws InterruptedException occur when thread interrupted
     * @throws IOException occur when ByteBuffer has problem
     */
    protected abstract void process(TransportStream ts)
            throws InterruptedException, IOException;

    /**
     * Internal function to make a result after a TS
     * @param event to emit to user
     * @throws InterruptedException occur when event thread has interrupted
     */
    protected void putOut(QueueData event) throws InterruptedException {
        if ( event == null ) return;

        m_event_queue.put(event);
    }

    /**
     * Add a filter to get TS corresponding only to PID of TS.
     * @param id PID of TS
     *
     * <p>
     * Packet ID refers to PID of TS
     */
    public void addPidFilter(int id) {
        if ( m_int_id_filter.contains(id) == false ) {
            m_int_id_filter.add(id);
        }
    }

    public List<Integer> getPidFilters() {
        return m_int_id_filter;
    }

    /**
     * Remove a filter which user added corresponding only to PID of TS.
     * @param id PID of TS
     *
     */
    public void removePidFilter(int id) {
        m_int_id_filter.removeAll(Collections.singleton(id));
    }

    /**
     * Enable logging while extracting.
     * {@link BaseExtractor#disableLogging()}
     */
    public void enableLogging() {
        m_enable_logging = true;
    }

    /**
     * Enable logging while extracting.
     * {@link BaseExtractor#enableLogging()}
     */
    public void disableLogging() {
        m_enable_logging = false;
    }

    /**
     * Enable modification of which can be added.
     * The child of BaseExtractor can get a chance to modify before final event emit.
     * {@link BaseExtractor#disablePreModification()}
     */
    public void enablePreModification() {
        m_enable_pre_modification = true;
    }

    /**
     * Disable modification of which can be added.
     * {@link BaseExtractor#enablePreModification()}
     */
    public void disablePreModification() {
        m_enable_pre_modification = false;
    }
}
