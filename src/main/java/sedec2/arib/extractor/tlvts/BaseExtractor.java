package sedec2.arib.extractor.tlvts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlvts.container.PacketFactory;
import sedec2.arib.tlvts.container.packets.TlvTransportStream;
import sedec2.util.Logger;

/**
 * BaseExtractor has implementations which are able to parse TLV, MMTP packet.
 * This is a common functionality which can be extend to each extractor for other purpose like
 * adding NAL prefix into video or syncword into audio and so on.
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
     * Filter as byte like table_id of table(AKS section) of ISO-13818
     */
    protected List<Byte> m_byte_id_filter = new ArrayList<>();

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
     * Extracting thread which can pull out a TS packet from TS packets raw queue
     */
    protected Thread m_tlvts_raw_extractor_thread;

    /**
     * Extracting thread which can pull out a TS packet from TS packets formatted queue
     */
    protected Thread m_tlvts_formatted_extractor_thread;

    /**
     * TLV packets queue as raw
     */
    protected BlockingQueue<byte[]> m_tlvts_raw_packets = null;

    /**
     * TLV packets queue as formatted
     */
    protected BlockingQueue<TlvTransportStream> m_tlvts_formatted_packets = null;

    /**
     * Event queue which can sent to user
     */
    protected BlockingQueue<QueueData> m_event_queue = null;

    protected Map<Integer, ByteArrayOutputStream> m_fragmented_tlvts = new HashMap<>();

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
     * Constructor which initialize queue buffer of TLV input packet, Event and
     * start running thread with blocking queue which only run when queue obtain an input.
     */
    public BaseExtractor() {
        m_tlvts_raw_packets = new ArrayBlockingQueue<byte[]>(100);
        m_tlvts_formatted_packets = new ArrayBlockingQueue<TlvTransportStream>(100);
        m_event_queue = new ArrayBlockingQueue<QueueData>(100);

        m_tlvts_raw_extractor_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TlvTransportStream ts = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_tlvts_raw_packets ) {
                            byte[] tlvts_raw = m_tlvts_raw_packets.take();
                            ts = PacketFactory.createPacket(tlvts_raw);

                            if ( ts != null ) process(ts);
                        }

                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG, String.format("Error while parsing TS \n"));
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

        m_tlvts_formatted_extractor_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TlvTransportStream ts = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_tlvts_formatted_packets ) {
                            ts = m_tlvts_formatted_packets.take();
                            if ( ts != null ) process(ts);
                        }

                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG, String.format("Error while parsing TS \n"));
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

        m_tlvts_formatted_extractor_thread.start();
        m_tlvts_raw_extractor_thread.start();
    }

    /**
     * Clear all of queue containing TLV, Event and MMTP fragmented packets.
     */
    public void clearQueue() {
        m_event_queue.clear();
        m_tlvts_raw_packets.clear();
        m_tlvts_formatted_packets.clear();
        m_fragmented_tlvts.clear();
    }

    /**
     * Clean up all of resource under BaseExtractor.
     * User must call this after their job finished.
     */
    public void destroy() {
        m_is_running = false;

        m_tlvts_formatted_extractor_thread.interrupt();
        m_tlvts_formatted_extractor_thread = null;

        m_tlvts_raw_extractor_thread.interrupt();
        m_tlvts_raw_extractor_thread = null;

        clearQueue();

        m_event_queue = null;
        m_tlvts_raw_packets = null;
        m_tlvts_formatted_packets = null;
        m_fragmented_tlvts = null;

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
     * User should put a TLV packet into extractor, the packet will be collected as kinds of them
     * @param tlvts one TLV packet
     * @throws InterruptedException occur when thread interrupted
     */
    public void putIn(byte[] tlvts) throws InterruptedException {
        if ( m_is_running == true && m_tlvts_raw_packets != null && tlvts != null ) {
            m_tlvts_raw_packets.put(tlvts);
        }
    }

    public void putIn(TlvTransportStream tlvts) throws InterruptedException {
        if ( m_is_running == true && m_tlvts_raw_packets != null && tlvts != null ) {
            m_tlvts_formatted_packets.put(tlvts);
        }
    }

    /**
     * Child extractor of BaseExtractor must implement process function to control TLV packets
     * as kinds of them. TLV can be IPv4, IPv6, CompressedPacket, SignallingPacket, NullPacket.
     * @param tlv one TLV packet already decoded
     * @throws InterruptedException occur when thread interrupted
     * @throws IOException occur when ByteBuffer has problem
     */
    protected abstract void process(TlvTransportStream tlv)
            throws InterruptedException, IOException;

    /**
     * Internal function to make a result after a TLV
     * @param event to emit to user
     * @throws InterruptedException occur when event thread has interrupted
     */
    protected void putOut(QueueData event) throws InterruptedException {
        if ( event == null ) return;

        m_event_queue.put(event);
    }

    /**
     * Add a filter to get MFU corresponding only to packet_id of MMTP.
     * @param id packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void addPidFilter(int id) {
        if ( m_int_id_filter.contains(id) == false ) {
            m_int_id_filter.add(id);
        }
    }

    /**
     * Add a filter to get SI corresponding only to table_id of Private Section.
     * @param id specific table id which user wants to get
     *
     * <p>
     * Table ID refers to 2.4.4.10 Syntax of the Private section in ISO13838-1
     */
    public void addPidFilter(byte id) {
        if ( m_byte_id_filter.contains(id) == false ) {
            m_byte_id_filter.add(id);
        }
    }

    /**
     * Remove a filter which user added corresponding only to packet_id of MMTP.
     * @param id packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void removePidFilter(int id) {
        if ( m_int_id_filter.contains(id) == true ) {
            m_int_id_filter.remove(id);
        }
    }

    /**
     * Remove a filter as table id of Private Section.
     * @param id which user doesn't want to receive via
     */
    public void removePidFilter(byte id) {
        if ( m_byte_id_filter.contains(id) == true ) {
            m_byte_id_filter.remove(id);
        }
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
