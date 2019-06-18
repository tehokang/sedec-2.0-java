package sedec2.arib.extractor.ts;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Table;

public class TsDemultiplexer implements
        SiExtractor.ITableExtractorListener,
        AudioExtractor.IAudioExtractorListener,
        VideoExtractor.IVideoExtractorListener {
    protected static final String TAG = TsDemultiplexer.class.getSimpleName();
    protected BaseExtractor m_si_extractor = null;
    protected BaseExtractor m_audio_extractor = null;
    protected BaseExtractor m_video_extractor = null;

    protected List<Listener> m_listeners = new ArrayList<>();
    protected boolean m_enable_si_filter = false;
    protected boolean m_enable_audio_filter = false;
    protected boolean m_enable_video_filter = false;

    /**
     * Listener to get information after putting TS into here
     * The listener can be added or removed via <br>
     * {@link TsDemultiplexer#addEventListener(Listener)} <br>
     * {@link TsDemultiplexer#removeEventListener(Listener)}
     */
    public interface Listener {
        public void onReceivedTable(int packet_id, Table table);
        public void onReceivedAudio(int packet_id, byte[] buffer);
        public void onReceivedVideo(int packet_id, byte[] buffer);
    }

    public TsDemultiplexer() {
        m_si_extractor = new SiExtractor();
        m_si_extractor.addEventListener(this);

        m_audio_extractor = new AudioExtractor();
        m_audio_extractor.addEventListener(this);

        m_video_extractor = new VideoExtractor();
        m_video_extractor.addEventListener(this);
    }

    /**
     * Clear all of data which Extractor has at this moment,
     * User should clear queue since their queue can have packets fragmented
     * when resource like channel or file is changed.
     */
    public void clearQueue() {
        m_si_extractor.clearQueue();
        m_audio_extractor.clearQueue();
        m_video_extractor.clearQueue();
    }

    /**
     * Destroy all of Extractor and clean up resources
     */
    public void destroy() {
        m_si_extractor.removeEventListener(this);
        m_si_extractor.destroy();
        m_si_extractor = null;

        m_audio_extractor.removeEventListener(this);
        m_audio_extractor.destroy();
        m_audio_extractor = null;

        m_video_extractor.removeEventListener(this);
        m_video_extractor.destroy();
        m_video_extractor = null;
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
     * Add a filter to get SI corresponding only to PID by user setting.
     * @param pid PID of TS which user wants to get
     *
     * Table ID refers to 2.4.4.10 Syntax of the Private section in ISO13838-1
     * User can receive Table via {@link TsDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void addSiFilter(int pid) {
        if ( m_si_extractor != null ) m_si_extractor.addPidFilter(pid);
    }

    /**
     * Remove a filter as table id of Private Section.
     * @param pid of TS which user doesn't want to receive via
     * {@link TsDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void removeSiFilter(int pid) {
        if ( m_si_extractor != null ) m_si_extractor.removePidFilter(pid);
    }

    /**
     * Add a filter to get audio corresponding only to PID by user setting.
     * @param pid PID of TS which user wants to get
     */
    public void addAudioFilter(int pid) {
        if ( m_audio_extractor != null ) m_audio_extractor.addPidFilter(pid);
    }

    /**
     * Removes a filter of audio PID
     * @param pid packet id of TS
     */
    public void removeAudioFilter(int pid) {
        if ( m_audio_extractor != null ) m_audio_extractor.removePidFilter(pid);
    }

    /**
     * Add a filter to get video corresponding only to PID by user setting.
     * @param pid PID of TS which user wants to get
     */
    public void addVideoFilter(int pid) {
        if ( m_video_extractor != null ) m_video_extractor.addPidFilter(pid);
    }

    /**
     * Removes a filter of video PID
     * @param pid packet id of TS
     */
    public void removeVideoFilter(int pid) {
        if ( m_video_extractor != null ) m_video_extractor.removePidFilter(pid);
    }

    /**
     * Remove all of filter which user added.
     * User can't receive any table information via
     * {@link TsDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void removeAllFilter() {
        if ( m_si_extractor != null ) {
            for ( int i=0; i<0x2000; i++ ) {
                m_si_extractor.removePidFilter(i & 0x1fff);
            }
        }

        if ( m_audio_extractor != null ) {
            for ( int i=0; i<0x2000; i++ ) {
                m_audio_extractor.removePidFilter(i & 0x1fff);
            }
        }

        if ( m_video_extractor != null ) {
            for ( int i=0; i<0x2000; i++ ) {
                m_video_extractor.removePidFilter(i & 0x1fff);
            }
        }
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
     * {@link TsDemultiplexer#disableSiFilter()}
     */
    public void enableSiFilter() {
        m_enable_si_filter = true;
    }

    /**
     * Enable or disable table_id filter, user can get table which delivered
     * {@link TsDemultiplexer#enableSiFilter()}
     */
    public void disableSiFilter() {
        m_enable_si_filter = false;
    }

    /**
     * Enable audio filter by PID of TS
     */
    public void enableAudioFilter() {
        m_enable_audio_filter = true;
    }

    /**
     * Disable audio filter by PID of TS
     */
    public void disableAudioFilter() {
        m_enable_audio_filter = false;
    }

    /**
     * Enable video filter by PID of TS
     */
    public void enableVideoFilter() {
        m_enable_video_filter = true;
    }

    /**
     * Disable video filter by PID of TS
     */
    public void disableVideoFilter() {
        m_enable_video_filter = false;
    }

    /**
     * Put a TS packet into and the packet will be decoded by each Extractor.
     * The packet can be 188 bytes as maximum
     * @param ts_raw one TS packet
     * @return return true if putting succeed or return false
     *
     * <p>
     * Every Extractor has thread running but the thread is always paused unless user put a TS.
     * This is for better performance, general thread having sleep of user make burden of performance.
     */
    public boolean put(byte[] ts_raw) {
        try {
            if ( m_enable_si_filter == true &&
                    m_si_extractor != null ) {
                m_si_extractor.putIn(ts_raw);
            }

            if ( m_enable_audio_filter == true &&
                    m_audio_extractor != null ) {
                m_audio_extractor.putIn(ts_raw);
            }

            if ( m_enable_video_filter == true &&
                    m_video_extractor != null ) {
                m_video_extractor.putIn(ts_raw);
            }

        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onReceivedTable(int packet_id, Table table) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedTable(packet_id, table);
        }
    }

    @Override
    public void onReceivedAudio(int packet_id, byte[] data) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedAudio(packet_id, data);
        }
    }

    @Override
    public void onReceivedVideo(int packet_id, byte[] data) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedVideo(packet_id, data);
        }

    }
}