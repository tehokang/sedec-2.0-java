package com.sedec.arib.extractor.tlvts;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.tlvts.container.packets.TlvTransportStream;

/**
 * Wrapper class containing each Extractor TLV in TS who can be extracted.
 * <p>
 * This is able to control all of Extractor with public interfaces and even
 * user can directly use each Extractor like below
 *
 * @see TlvExtractor
 */
public class TlvTsDemultiplexer implements
        TlvExtractor.ITlvExtractorListener {
    protected static final String TAG = TlvTsDemultiplexer.class.getSimpleName();

    /**
     * Listener to get information after putting TS into here
     * The listener can be added or removed via <br>
     * {@link TlvTsDemultiplexer#addEventListener(Listener)} <br>
     * {@link TlvTsDemultiplexer#removeEventListener(Listener)}
     */
    public interface Listener {
        public void onReceivedTlv(int packet_id, byte[] buffer);
    }

    protected boolean m_enable_tlv_filter = false;

    protected BaseExtractor m_tlv_extractor = null;

    protected List<Listener> m_listeners = new ArrayList<>();

    /**
     * Constructor which is including each Extractor's creation and running task
     *
     * <ul>
     * <li> {@link TlvExtractor}
     * </ul>
     *
     * <p>
     * Each Extractor could start their own thread which's putting TS into and popping Event out
     */
    public TlvTsDemultiplexer() {
        m_tlv_extractor = new TlvExtractor();
        m_tlv_extractor.addEventListener(this);
    }

    /**
     * Clear all of data which Extractor has at this moment,
     * User should clear queue since their queue can have packets fragmented
     * when resource like channel or file is changed.
     */
    public void clearQueue() {
        m_tlv_extractor.clearQueue();
    }

    /**
     * Destroy all of Extractor and clean up resources
     */
    public void destroy() {
        m_tlv_extractor.removeEventListener(this);
        m_tlv_extractor.destroy();
        m_tlv_extractor = null;
    }

    /**
     * Add event listener to get informations from here.
     * @param listener {@link TlvTsDemultiplexer.Listener}
     */
    public void addEventListener(Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }

    /**
     * Remove event listener which user added.
     * @param listener {@link TlvTsDemultiplexer.Listener}
     */
    public void removeEventListener(Listener listener) {
        m_listeners.remove(listener);
    }

    /**
     * Add a filter to get a TLV as corresponding only to packet_id of TS.
     * @param pid packet_id of TS
     *
     * <p>
     * Packet ID refers to ISO-13818
     */
    public void addTlvFilter(int pid) {
        if ( m_tlv_extractor != null ) m_tlv_extractor.addPidFilter(pid);
    }

    public void addTlvAllFilter() {
        if ( m_tlv_extractor != null ) {
            for ( int i=0; i<0x2000; i++ ) {
                m_tlv_extractor.addPidFilter(i &0x1fff);
            }
        }
    }

    /**
     * Remove a filter which user added corresponding only to packet_id of TS.
     * @param pid packet_id of TS
     *
     * <p>
     * Packet ID refers to ISO-13818
     */
    public void removeTlvFilter(int pid) {
        if ( m_tlv_extractor != null ) m_tlv_extractor.removePidFilter(pid);
    }

    /**
     * Enable logging while extracting of TLV.
     * {@link TlvTsDemultiplexer#disableTlvLogging()}
     */
    public void enableTlvLogging() {
        if ( m_tlv_extractor != null ) m_tlv_extractor.enableLogging();
    }

    /**
     * Disable logging while extracting of TLV.
     * {@link TlvTsDemultiplexer#enableTlvLogging()}
     */
    public void disableTlvLogging() {
        if ( m_tlv_extractor != null ) m_tlv_extractor.disableLogging();
    }

    /**
     * Enable or disable TLV filter, user can get TLV which delivered in TS.
     * {@link TlvTsDemultiplexer#disableTlvFilter()}
     */
    public void enableTlvFilter() {
        m_enable_tlv_filter = true;
    }

    /**
     * Enable or disable TLV filter, user can get TLV which delivered in TS.
     * {@link TlvTsDemultiplexer#enableTlvFilter()}
     */
    public void disableTlvFilter() {
        m_enable_tlv_filter = false;
    }

    /**
     * Put a TS packet into and the packet will be decoded by each Extractor.
     * @param tlvts_raw one TS packet
     * @return return true if putting succeed or return false
     *
     * <p>
     * Every Extractor has thread running but the thread is always paused unless user put a TS.
     * This is for better performance, general thread having sleep of user make burden of performance.
     */
    public boolean put(byte[] tlvts_raw) {
        try {
            if ( m_enable_tlv_filter == true &&
                    m_tlv_extractor != null ) {
                m_tlv_extractor.putIn(tlvts_raw);
            }

        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    /**
     * Put a TS packet as formatted by sedec into and the packet will be decoded by TLV Extractor.
     * @param tlvts one TS packet
     * @return return true if putting succeed or return false
     *
     * <p>
     * Every Extractor has thread running but the thread is always paused unless user put a TS.
     * This is for better performance, general thread having sleep of user make burden of performance.
     */
    public boolean put(TlvTransportStream tlvts) {
        try {
            if ( m_enable_tlv_filter == true &&
                    m_tlv_extractor != null ) {
                m_tlv_extractor.putIn(tlvts);
            }

        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onReceivedTlv(int packet_id, byte[] buffer) {
        for ( int i=0; i<m_listeners.size(); i++ ) {
            m_listeners.get(i).onReceivedTlv(packet_id, buffer);
        }
    }
}
