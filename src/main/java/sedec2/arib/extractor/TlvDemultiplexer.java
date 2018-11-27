package sedec2.arib.extractor;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.base.Table;

/**
 * Wrapper class containing each Extractor like Si, Ntp, Ttml, Video, Audio, Application
 * who can be extracted.
 * <p>
 * This is able to control all of Extractor with public interfaces and even
 * user can directly use each Extractor
 */
public class TlvDemultiplexer implements
        SiExtractor.ITableExtractorListener, NtpExtractor.INtpExtractorListener,
        TtmlExtractor.ITtmlExtractorListener, VideoExtractor.IVideoExtractorListener,
        AudioExtractor.IAudioExtractorListener, ApplicationExtractor.IAppExtractorListener,
        GeneralPurposeDataExtractor.IGeneralPurposeDataExtractorListener {
    /**
     * Listener to get information after putting TLV into here
     * The listener can be added or removed via <br>
     * {@link TlvDemultiplexer#addEventListener(Listener)} <br>
     * {@link TlvDemultiplexer#removeEventListener(Listener)}
     */
    public interface Listener {
        public void onReceivedTable(Table table);
        public void onReceivedNtp(NetworkTimeProtocolData ntp);
        public void onReceivedTtml(int packet_id, byte[] buffer);
        public void onReceivedVideo(int packet_id, byte[] buffer);
        public void onReceivedAudio(int packet_id, byte[] buffer);
        public void onReceivedApplication(int packet_id, int item_id,
                int mpu_sequence_number, byte[] buffer);
        public void onReceivedIndexItem(int packet_id, int item_id,
                int mpu_sequence_number, byte[] buffer);
        public void onReceivedGeneralData(int packet_id, byte[] buffer);
    }

    protected boolean m_enable_si_filter = false;
    protected boolean m_enable_ntp_filter = false;
    protected boolean m_enable_ttml_filter = false;
    protected boolean m_enable_video_filter = false;
    protected boolean m_enable_audio_filter = false;
    protected boolean m_enable_application_filter = false;
    protected boolean m_enable_general_data_filter = false;

    protected BaseExtractor m_si_extractor = null;
    protected BaseExtractor m_ntp_extractor = null;
    protected BaseExtractor m_ttml_extractor = null;
    protected BaseExtractor m_video_extractor = null;
    protected BaseExtractor m_audio_extractor = null;
    protected BaseExtractor m_application_extractor = null;
    protected BaseExtractor m_generaldata_extractor = null;

    protected List<Listener> m_listeners = new ArrayList<>();

    /**
     * Constructor which is including each Extractor's creation and running task
     *
     * <ul>
     * <li> {@link SiExtractor}
     * <li> {@link NtpExtractor}
     * <li> {@link TtmlExtractor}
     * <li> {@link VideoExtractor}
     * <li> {@link AudioExtractor}
     * <li> {@link ApplicationExtractor}
     * <li> {@link GeneralPurposeDataExtractor}
     * </ul>
     *
     * <p>
     * Each Extractor could start their own thread which's putting TLV into and popping Event out
     */
    public TlvDemultiplexer() {
        m_si_extractor = new SiExtractor();
        m_ntp_extractor = new NtpExtractor();
        m_ttml_extractor = new TtmlExtractor();
        m_video_extractor = new VideoExtractor();
        m_audio_extractor = new AudioExtractor();
        m_application_extractor = new ApplicationExtractor();
        m_generaldata_extractor = new GeneralPurposeDataExtractor();

        m_si_extractor.addEventListener(this);
        m_ntp_extractor.addEventListener(this);
        m_ttml_extractor.addEventListener(this);
        m_video_extractor.addEventListener(this);
        m_audio_extractor.addEventListener(this);
        m_application_extractor.addEventListener(this);
        m_generaldata_extractor.addEventListener(this);
    }

    /**
     * Clear all of data which Extractor has at this moment,
     * User should clear queue since their queue can have packets fragmented
     * when resource like channel or file is changed.
     */
    public void clearQueue() {
        m_si_extractor.clearQueue();
        m_ntp_extractor.clearQueue();
        m_ttml_extractor.clearQueue();
        m_video_extractor.clearQueue();
        m_audio_extractor.clearQueue();
        m_application_extractor.clearQueue();
        m_generaldata_extractor.clearQueue();
    }

    /**
     * Destroy all of Extractor and clean up resources
     */
    public void destroy() {
        m_si_extractor.removeEventListener(this);
        m_ntp_extractor.removeEventListener(this);
        m_ttml_extractor.removeEventListener(this);
        m_video_extractor.removeEventListener(this);
        m_audio_extractor.removeEventListener(this);
        m_application_extractor.removeEventListener(this);
        m_generaldata_extractor.removeEventListener(this);

        m_si_extractor.destroy();
        m_ntp_extractor.destroy();
        m_ttml_extractor.destroy();
        m_video_extractor.destroy();
        m_audio_extractor.destroy();
        m_application_extractor.destroy();
        m_generaldata_extractor.destroy();

        m_si_extractor = null;
        m_ntp_extractor = null;
        m_ttml_extractor = null;
        m_video_extractor = null;
        m_audio_extractor = null;
        m_application_extractor = null;
        m_generaldata_extractor = null;
    }

    /**
     * Add event listener to get informations from here.
     * @param listener {@link TlvDemultiplexer.Listener}
     */
    public void addEventListener(Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }

    /**
     * Remove event listener which user added.
     * @param listener {@link TlvDemultiplexer.Listener}
     */
    public void removeEventListener(Listener listener) {
        m_listeners.remove(listener);
    }

    /**
     * Add a filter to get video as MFU corresponding only to packet_id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void addVideoFilter(int pid) {
        if ( m_video_extractor != null ) m_video_extractor.addPidFilter(pid);
    }

    /**
     * Remove a filter which user added corresponding only to packet_id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void removeVideoFilter(int pid) {
        if ( m_video_extractor != null ) m_video_extractor.removePidFilter(pid);
    }

    /**
     * Add a filter to get audio as MFU corresponding only to packet_id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void addAudioFilter(int pid) {
        if ( m_audio_extractor != null ) m_audio_extractor.addPidFilter(pid);
    }

    /**
     * Remove a filter which user added corresponding only to packet_id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void removeAudioFilter(int pid) {
        if ( m_audio_extractor != null ) m_audio_extractor.removePidFilter(pid);
    }

    /**
     * Add a filter to get TTML as MFU corresponding only to packet_id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void addTtmlFilter(int pid) {
        if ( m_ttml_extractor != null ) m_ttml_extractor.addPidFilter(pid);
    }

    /**
     * Remove a filter which user added corresponding only to packet_id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void removeTtmlFilter(int pid) {
        if ( m_ttml_extractor != null ) m_ttml_extractor.removePidFilter(pid);
    }

    /**
     * Add a filter to get SI corresponding only to table_id of Private Section.
     * @param table_id specific table id which user wants to get
     *
     * Table ID refers to 2.4.4.10 Syntax of the Private section in ISO13838-1
     * User can receive Table via {@link TlvDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void addSiFilter(byte table_id) {
        if ( m_si_extractor != null ) m_si_extractor.addPidFilter(table_id);
    }

    /**
     * Add all of filter to get SI which can be TLV-SI, MMT-SI.
     * User can receive all of tables which sedec can extract via
     * {@link TlvDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void addSiAllFilter() {
        if ( m_si_extractor != null ) {
            for ( int i=0; i<256; i++ ) {
                m_si_extractor.addPidFilter((byte)(i & 0xff));
            }
        }
    }

    /**
     * Remove all of filter which user added for TLV-SI, MMT-SI.
     * User can't receive any table information via
     * {@link TlvDemultiplexer.Listener#onReceivedTable(Table)}
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
     * {@link TlvDemultiplexer.Listener#onReceivedTable(Table)}
     */
    public void removeSiFilter(byte table_id) {
        if ( m_si_extractor != null ) m_si_extractor.removePidFilter(table_id);
    }

    /**
     * Add a filter to get Ntp corresponding only to packet id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void addNtpFilter(int pid) {
        if ( m_ntp_extractor != null ) m_ntp_extractor.addPidFilter(pid);
    }

    /**
     * Remove a filter added as corresponding to packet id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void removeNtpFilter(int pid) {
        if ( m_ntp_extractor != null ) m_ntp_extractor.removePidFilter(pid);
    }

    /**
     * Add a filter to get Application corresponding only to packet id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void addApplicationFilter(int pid) {
        if ( m_application_extractor != null ) m_application_extractor.addPidFilter(pid);
    }

    /**
     * Remove a filter added as corresponding to packet id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void removeApplicationFilter(int pid) {
        if ( m_application_extractor != null ) m_application_extractor.removePidFilter(pid);
    }

    /**
     * Add a filter to get General Purpose Data corresponding only to packet id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void addGeneralPurposeDataFilter(int pid) {
        if ( m_generaldata_extractor != null ) m_generaldata_extractor.addPidFilter(pid);
    }

    /**
     * Remove a filter added as corresponding to packet id of MMTP.
     * @param pid packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void removeGeneralPurposeDataFilter(int pid) {
        if ( m_generaldata_extractor != null ) m_generaldata_extractor.removePidFilter(pid);
    }

    /**
     * Enable logging while extracting of video.
     * {@link TlvDemultiplexer#disableVideoLogging()}
     */
    public void enableVideoLogging() {
        if ( m_video_extractor != null ) m_video_extractor.enableLogging();
    }

    /**
     * Disable logging while extracting of audio.
     * {@link TlvDemultiplexer#enableVideoLogging()}
     */
    public void disableVideoLogging() {
        if ( m_video_extractor != null ) m_video_extractor.disableLogging();
    }

    /**
     * Enable modification of video which can add NAL prefix.
     * After this user will get MFU of video containing NAL prefix.
     * {@link TlvDemultiplexer#disableVideoPreModification()}
     */
    public void enableVideoPreModification() {
        if ( m_video_extractor != null ) m_video_extractor.enablePreModification();
    }

    /**
     * Disable modification of video which can add NAL prefix.
     * After this user will get MFU of video without NAL prefix.
     * {@link TlvDemultiplexer#enableVideoPreModification()}
     */
    public void disableVideoPreModification() {
        if ( m_video_extractor != null ) m_video_extractor.disablePreModification();
    }

    /**
     * Enable logging while extracting audio.
     * {@link TlvDemultiplexer#disableAudioLogging()}
     */
    public void enableAudioLogging() {
        if ( m_audio_extractor != null ) m_audio_extractor.enableLogging();
    }

    /**
     * Disable logging while extracting audio.
     * {@link TlvDemultiplexer#enableAudioLogging()}
     */
    public void disableAudioLogging() {
        if ( m_audio_extractor != null ) m_audio_extractor.disableLogging();
    }

    /**
     * Enable modification of audio which can add syncword prefix.
     * After this user will get MFU of audio containing syncword prefix.
     * {@link TlvDemultiplexer#disableAudioPreModification()}
     *
     * <p>
     * In general AudioMuxElement that synchronization byte and length information
     * are removed from AudioSyncStream() is transmitted as MFU.
     * AudioSyncStream and Audio MuxElement refers to ISO14496-3
     */
    public void enableAudioPreModification() {
        if ( m_audio_extractor != null ) m_audio_extractor.enablePreModification();
    }

    /**
     * Disable modification of audio which can add syncword prefix.
     * After this user will get MFU of audio without syncword prefix.
     * {@link TlvDemultiplexer#enableAudioPreModification()}
     *
     * <p>
     * AudioSyncStream and Audio MuxElement refers to ISO14496-3
     */
    public void disableAudioPreModification() {
        if ( m_audio_extractor != null ) m_audio_extractor.disablePreModification();
    }

    /**
     * Enable logging while extracting TTML.
     */
    public void enableTtmlLogging() {
        if ( m_ttml_extractor != null ) m_ttml_extractor.enableLogging();
    }

    /**
     * Disable logging while extracting TTML.
     */
    public void disableTtmlLogging() {
        if ( m_ttml_extractor != null ) m_ttml_extractor.disableLogging();
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
     * Enable logging while extracting NTP.
     */
    public void enableNtpLogging() {
        if ( m_ntp_extractor != null ) m_ntp_extractor.enableLogging();
    }

    /**
     * Disable logging while extracting NTP.
     */
    public void disableNtpLogging() {
        if ( m_ntp_extractor != null ) m_ntp_extractor.disableLogging();
    }

    /**
     * Enable logging while extracting Application.
     */
    public void enableApplicationLogging() {
        if ( m_application_extractor != null ) m_application_extractor.enableLogging();
    }

    /**
     * Disable logging while extracting Application.
     */
    public void disableAppLogging() {
        if ( m_application_extractor != null ) m_application_extractor.disableLogging();
    }

    /**
     * Enable logging while extracting General Purpose Data.
     */
    public void enableGeneralDataLogging() {
        if ( m_generaldata_extractor != null ) m_generaldata_extractor.enableLogging();
    }

    /**
     * Disable logging while extracting General Purpose Data.
     */
    public void disableGeneralDataLogging() {
        if ( m_generaldata_extractor != null ) m_generaldata_extractor.disableLogging();
    }

    /**
     * Enable or disable Video filter, user can get Video which delivered
     * in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#disableVideoFilter()}
     */
    public void enableVideoFilter() {
        m_enable_video_filter = true;
    }

    /**
     * Enable or disable Video filter, user can get Video which delivered
     * in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#enableVideoFilter()}
     */
    public void disableVideoFilter() {
        m_enable_video_filter = false;
    }

    /**
     * Enable or disable Audio filter, user can get Audio which delivered
     * in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#disableAudioFilter()}
     */
    public void enableAudioFilter() {
        m_enable_audio_filter = true;
    }

    /**
     * Enable or disable Audio filter, user can get Audio which delivered
     * in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#enableAudioFilter()}
     */
    public void disableAudioFilter() {
        m_enable_audio_filter = false;
    }

    /**
     * Enable or disable TTML filter, user can get TTML which delivered
     * in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#disableTtmlFilter()}
     */
    public void enableTtmlFilter() {
        m_enable_ttml_filter = true;
    }

    /**
     * Enable or disable TTML filter, user can get TTML which delivered
     * in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#enableTtmlFilter()}
     */
    public void disableTtmlFilter() {
        m_enable_ttml_filter = false;
    }

    /**
     * Enable or disable table_id filter, user can get table which delivered
     * in Compressed Packet, Signaling Packet of TLV.
     * {@link TlvDemultiplexer#disableSiFilter()}
     */
    public void enableSiFilter() {
        m_enable_si_filter = true;
    }

    /**
     * Enable or disable table_id filter, user can get table which delivered
     * in Compressed Packet, Signaling Packet of TLV.
     * {@link TlvDemultiplexer#enableSiFilter()}
     */
    public void disableSiFilter() {
        m_enable_si_filter = false;
    }

    /**
     * Enable or disable NTP filter, user can get only NTP which delivered
     * in IPv4, IPv6 Packet of TLV
     * {@link TlvDemultiplexer#disableNtpFilter()}
     */
    public void enableNtpFilter() {
        m_enable_ntp_filter = true;
    }

    /**
     * Enable or disable NTP filter, user can get only NTP which delivered
     * in IPv4, IPv6 Packet of TLV
     * {@link TlvDemultiplexer#enableNtpFilter()}
     */
    public void disableNtpFilter() {
        m_enable_ntp_filter = false;
    }

    /**
     * Enable or disable Application filter, user can get Application which delivered
     * in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#disableApplicationFilter()}
     */
    public void enableApplicationFilter() {
        m_enable_application_filter = true;
    }

    /**
     * Enable or disable Application filter, user can get Application which delivered
     * in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#enableApplicationFilter()}
     */
    public void disableApplicationFilter() {
        m_enable_application_filter = false;
    }

    /**
     * Enable or disable General Purpose Data filter, user can get General Purpose Data
     * which delivered in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#enableGeneralDataFilter()}
     */
    public void enableGeneralDataFilter() {
        m_enable_general_data_filter = true;
    }

    /**
     * Enable or disable General Purpose Data filter, user can get General Purpose Data
     * which delivered in CompressedIPPacket of TLV.
     * {@link TlvDemultiplexer#enableGeneralDataFilter()}
     */
    public void disableGeneralDataFilter() {
        m_enable_general_data_filter = false;
    }

    /**
     * Put a TLV packet into and the packet will be decoded by each Extractor.
     * The packet can be 64K bytes as maximum since packet_length of TLV has 16 bits.
     * @param tlv_raw one TLV packet
     * @return return true if putting succeed or return false
     *
     * <p>
     * Every Extractor has thread running but the thread is always paused unless user put a TLV.
     * This is for better performance, general thread having sleep of user make burden of performance.
     */
    public boolean put(byte[] tlv_raw) {
        try {
            if ( m_enable_video_filter == true &&
                    m_video_extractor != null ) {
                m_video_extractor.putIn(tlv_raw);
            }

            if ( m_enable_audio_filter == true &&
                    m_audio_extractor != null ) {
                m_audio_extractor.putIn(tlv_raw);
            }

            if ( m_enable_ttml_filter == true &&
                    m_ttml_extractor != null ) {
                m_ttml_extractor.putIn(tlv_raw);
            }

            if ( m_enable_si_filter == true &&
                    m_si_extractor != null ) {
                m_si_extractor.putIn(tlv_raw);
            }

            if ( m_enable_ntp_filter == true &&
                    m_ntp_extractor != null ) {
                m_ntp_extractor.putIn(tlv_raw);
            }

            if ( m_enable_application_filter == true &&
                    m_application_extractor != null ) {
                m_application_extractor.putIn(tlv_raw);
            }

            if ( m_enable_general_data_filter == true &&
                    m_generaldata_extractor != null ) {
                m_generaldata_extractor.putIn(tlv_raw);
            }
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onReceivedAudio(int packet_id, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedAudio(packet_id, buffer);
        }
    }

    @Override
    public void onReceivedVideo(int packet_id, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedVideo(packet_id, buffer);
        }
    }

    @Override
    public void onReceivedTtml(int packet_id, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedTtml(packet_id, buffer);
        }
    }

    @Override
    public void onReceivedNtp(NetworkTimeProtocolData ntp) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedNtp(ntp);
        }
    }

    @Override
    public void onReceivedTable(Table table) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedTable(table);
        }
    }

    @Override
    public void onReceivedApplication(int packet_id, int item_id,
            int mpu_sequence_number, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedApplication(packet_id, item_id,
                    mpu_sequence_number, buffer);
        }
    }

    @Override
    public void onReceivedIndexItem(int packet_id, int item_id,
            int mpu_sequence_number, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedIndexItem(packet_id, item_id,
                    mpu_sequence_number, buffer);
        }
    }

    @Override
    public void onReceivedGeneralPurposeData(int packet_id, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedGeneralData(packet_id, buffer);
        }
    }
}
