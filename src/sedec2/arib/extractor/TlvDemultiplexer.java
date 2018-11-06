package sedec2.arib.extractor;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.base.Table;

public class TlvDemultiplexer implements 
        SiExtractor.ITableExtractorListener, NtpExtractor.INtpExtractorListener, 
        TtmlExtractor.ITtmlExtractorListener, VideoExtractor.IVideoExtractorListener,
        AudioExtractor.IAudioExtractorListener, ApplicationExtractor.IAppExtractorListener, 
        GeneralPurposeDataExtractor.IGeneralPurposeDataExtractorListener {
    public interface Listener {
        public void onReceivedTable(Table table);
        public void onReceivedNtp(NetworkTimeProtocolData ntp);
        public void onReceivedTtml(int packet_id, byte[] buffer);
        public void onReceivedVideo(int packet_id, byte[] buffer);
        public void onReceivedAudio(int packet_id, byte[] buffer);
        public void onReceivedApplication(int packet_id, byte[] buffer);
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
    
    public void addEventListener(Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }
    
    public void removeEventListener(Listener listener) {
        m_listeners.remove(listener);
    }
    
    public void addVideoPidFilter(int pid) {
        m_video_extractor.addPidFilter(pid);
    }
    
    public void removeVideoPidFilter(int pid) {
        m_video_extractor.removePidFilter(pid);
    }
    
    public void addAudioPidFilter(int pid) {
        m_audio_extractor.addPidFilter(pid);
    }
    
    public void removeAudioPidFilter(int pid) {
        m_audio_extractor.removePidFilter(pid);
    }
    
    public void addTtmlPidFilter(int pid) {
        m_ttml_extractor.addPidFilter(pid);
    }
    
    public void removeTtmlPidFilter(int pid) {
        m_ttml_extractor.removePidFilter(pid);
    }
    
    public void addSiFilter(byte table_id) {
        m_si_extractor.addPidFilter(table_id);
    }
    
    public void addSiAllFilter() {
        for ( int i=0; i<256; i++ ) {
            m_si_extractor.addPidFilter((byte)(i & 0xff));
        }
    }
    
    public void removeSiAllFilter() {
        for ( int i=0; i<256; i++ ) {
            m_si_extractor.removePidFilter((byte)(i & 0xff));
        }
    }
    
    public void removeSiFilter(byte table_id) {
        m_si_extractor.removePidFilter(table_id);
    }
    
    public void addNtpPidFilter(int pid) {
        m_ntp_extractor.addPidFilter(pid);
    }
    
    public void removeNtpPidFilter(int pid) {
        m_ntp_extractor.removePidFilter(pid);
    }
    
    public void addApplicationPidFilter(int pid) {
        m_application_extractor.addPidFilter(pid);
    }
    
    public void removeApplicationPidFilter(int pid) {
        m_application_extractor.removePidFilter(pid);
    }
    
    public void addGeneralPurposeDataPidFilter(int pid) {
        m_generaldata_extractor.addPidFilter(pid);
    }
    
    public void removeGeneralPurposeDataPidFilter(int pid) {
        m_generaldata_extractor.removePidFilter(pid);
    }

    public void enableVideoLogging() {
        m_video_extractor.enableLogging();
    }
    
    public void disableVideoLogging() {
        m_video_extractor.disableLogging();
    }
    
    public void enableAudioLogging() {
        m_audio_extractor.enableLogging();
    }
    
    public void disableAudioLogging() {
        m_audio_extractor.disableLogging();
    }
    
    public void enableTtmlLogging() {
        m_ttml_extractor.enableLogging();
    }
    
    public void disableTtmlLogging() {
        m_ttml_extractor.disableLogging();
    }
    
    public void enableSiLogging() {
        m_si_extractor.enableLogging();
    }
    
    public void disableSiLogging() {
        m_si_extractor.disableLogging();
    }
    
    public void enableNtpLogging() {
        m_ntp_extractor.enableLogging();
    }
    
    public void disableNtpLogging() {
        m_ntp_extractor.disableLogging();
    }
    
    public void enableApplicationLogging() {
        m_application_extractor.enableLogging();
    }
    
    public void disableAppLogging() {
        m_application_extractor.disableLogging();
    }
    
    public void enableGeneralDataLogging() {
        m_generaldata_extractor.enableLogging();
    }
    
    public void disableGeneralDataLogging() {
        m_generaldata_extractor.disableLogging();
    }
    
    public void enableVideoFilter() {
        m_enable_video_filter = true;
    }
    
    public void disableVideoFilter() {
        m_enable_video_filter = false;
    }
    
    public void enableAudioFilter() {
        m_enable_audio_filter = true;
    }
    
    public void disableAudioFilter() {
        m_enable_audio_filter = false;
    }
    
    public void enableTtmlFilter() {
        m_enable_ttml_filter = true;
    }
    
    public void disableTtmlFilter() {
        m_enable_ttml_filter = false;
    }
    
    public void enableSiFilter() {
        m_enable_si_filter = true;
    }
    
    public void disableSiFilter() {
        m_enable_si_filter = false;
    }
    
    public void enableNtpFilter() {
        m_enable_ntp_filter = true;
    }
    
    public void disableNtpFilter() {
        m_enable_ntp_filter = false;
    }
    
    public void enableApplicationFilter() {
        m_enable_application_filter = true;
    }
    
    public void disableApplicationFilter() {
        m_enable_application_filter = false;
    }
    
    public void enableGeneralDataFilter() {
        m_enable_general_data_filter = true;
    }
    
    public void disableGeneralDataFilter() {
        m_enable_general_data_filter = false;
    }
    
    public boolean put(byte[] tlv_raw) {
        try {
            if ( m_enable_video_filter == true ) {
                m_video_extractor.putIn(tlv_raw);
            }
            
            if ( m_enable_audio_filter == true ) {
                m_audio_extractor.putIn(tlv_raw);
            }
            
            if ( m_enable_ttml_filter == true ) {
                m_ttml_extractor.putIn(tlv_raw);
            }
            
            if ( m_enable_si_filter == true ) {
                m_si_extractor.putIn(tlv_raw);
            }
            
            if ( m_enable_ntp_filter == true ) {
                m_ntp_extractor.putIn(tlv_raw);
            }
            
            if ( m_enable_application_filter == true ) {
                m_application_extractor.putIn(tlv_raw);
            }
            
            if ( m_enable_general_data_filter == true ) {
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
    public void onReceivedApplication(int packet_id, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedApplication(packet_id, buffer);
        }
    }

    @Override
    public void onReceivedGeneralPurposeData(int packet_id, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedGeneralData(packet_id, buffer);
        }   
    }
}