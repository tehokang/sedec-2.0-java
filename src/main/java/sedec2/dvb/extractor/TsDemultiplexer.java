package sedec2.dvb.extractor;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Table;

public class TsDemultiplexer implements SiExtractor.ITableExtractorListener {
    protected static final String TAG = TsDemultiplexer.class.getSimpleName();
    protected BaseExtractor m_si_extractor = null;
    protected List<Listener> m_listeners = new ArrayList<>();
    protected boolean m_enable_si_filter = false;

    /**
     * Listener to get information after putting TLV into here
     * The listener can be added or removed via <br>
     * {@link TsDemultiplexer#addEventListener(Listener)} <br>
     * {@link TsDemultiplexer#removeEventListener(Listener)}
     */
    public interface Listener {
        public void onReceivedTable(Table table);
    }

    public TsDemultiplexer() {
        m_si_extractor = new SiExtractor();
        m_si_extractor.addEventListener(this);
    }

    /**
     * Clear all of data which Extractor has at this moment,
     * User should clear queue since their queue can have packets fragmented
     * when resource like channel or file is changed.
     */
    public void clearQueue() {
        m_si_extractor.clearQueue();
    }

    /**
     * Destroy all of Extractor and clean up resources
     */
    public void destroy() {
        m_si_extractor.removeEventListener(this);
        m_si_extractor.destroy();
        m_si_extractor = null;
    }

    /**
     * Add event listener to get informations from here.
     * @param listener {@link TsDemultiplexer.Listener}
     */
    public void addEventListener(Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }

    /**
     * Remove event listener which user added.
     * @param listener {@link TsDemultiplexer.Listener}
     */
    public void removeEventListener(Listener listener) {
        m_listeners.remove(listener);
    }

    /**
     * Add a filter to get SI corresponding only to table_id of Private Section.
     * @param table_id specific table id which user wants to get
     *
     * Table ID refers to 2.4.4.10 Syntax of the Private section in ISO13838-1
     * User can receive Table via {@link TsDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void addSiFilter(byte table_id) {
        if ( m_si_extractor != null ) m_si_extractor.addPidFilter(table_id);
    }

    /**
     * Add all of filter to get SI which can be SI.
     * User can receive all of tables which sedec can extract via
     * {@link TsDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void addSiAllFilter() {
        if ( m_si_extractor != null ) {
            for ( int i=0; i<256; i++ ) {
                m_si_extractor.addPidFilter((byte)(i & 0xff));
            }
        }
    }

    /**
     * Remove all of filter which user added.
     * User can't receive any table information via
     * {@link TsDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void removeSiAllFilter() {
        if ( m_si_extractor != null ) {
            for ( int i=0; i<256; i++ ) {
                m_si_extractor.removePidFilter((byte)(i & 0xff));
            }
        }
    }

    /**
     * Remove a filter as table id of Private Section.
     * @param table_id which user doesn't want to receive via
     * {@link TsDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void removeSiFilter(byte table_id) {
        if ( m_si_extractor != null ) m_si_extractor.removePidFilter(table_id);
    }

    /**
     * Enable logging while extracting SI.
     */
    public void enableSiLogging() {
        if ( m_si_extractor != null ) m_si_extractor.enableLogging();
    }

    /**
     * Disable logging while extracting SI.
     */
    public void disableSiLogging() {
        if ( m_si_extractor != null ) m_si_extractor.disableLogging();
    }

    /**
     * Enable or disable table_id filter, user can get table which delivered
     * in Compressed Packet, Signaling Packet of TLV.
     * {@link TsDemultiplexer#disableSiFilter()}
     */
    public void enableSiFilter() {
        m_enable_si_filter = true;
    }

    /**
     * Enable or disable table_id filter, user can get table which delivered
     * in Compressed Packet, Signaling Packet of TLV.
     * {@link TsDemultiplexer#enableSiFilter()}
     */
    public void disableSiFilter() {
        m_enable_si_filter = false;
    }

    /**
     * Put a TLV packet into and the packet will be decoded by each Extractor.
     * The packet can be 188 bytes as maximum
     * @param ts_raw one TS packet
     * @return return true if putting succeed or return false
     *
     * <p>
     * Every Extractor has thread running but the thread is always paused unless user put a TLV.
     * This is for better performance, general thread having sleep of user make burden of performance.
     */
    public boolean put(byte[] ts_raw) {
        try {
            if ( m_enable_si_filter == true &&
                    m_si_extractor != null ) {
                m_si_extractor.putIn(ts_raw);
            }
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onReceivedTable(Table table) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedTable(table);
        }
    }
}