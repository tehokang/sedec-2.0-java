package sedec2.arib.tlv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Payload_MPU;
import sedec2.base.Table;
import sedec2.util.Logger;

public class TlvMediaExtractor {
    public interface IMediaExtractorListener {
        public void onReceivedVideo(int packet_id, byte[] buffer);
        public void onReceivedAudio(int packet_id, byte[] buffer);
    }
    
    protected final String TAG = "TlvMediaExtractor";
    
    List<Integer> m_video_pid_filters = new ArrayList<>();
    List<Integer> m_audio_pid_filters = new ArrayList<>();
    List<Integer> m_data_pid_filters = new ArrayList<>();
    
    protected Thread m_tlv_extractor_thread;
    protected boolean m_is_running = true;
    
    protected List<IMediaExtractorListener> m_listeners = new ArrayList<>();

    protected BlockingQueue<byte[]> m_tlv_packets = new ArrayBlockingQueue<byte[]>(100);
    
    public TlvMediaExtractor() {
        m_tlv_extractor_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_tlv_packets ) {
                            byte[] tlv_raw = (byte[])m_tlv_packets.take();
                            TypeLengthValue tlv = 
                                    sedec2.arib.tlv.container.PacketFactory.createPacket(tlv_raw);
                            processTLV(tlv); 
                        }
                        
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * @note Nothing to do
                         */
                    } catch ( Exception e ) {
                        /**
                         * @todo You should remove a line below, because TLVExtractor \n
                         * has to keep alive even though TLVExtractor get any wrong packets.
                         */
                        e.printStackTrace();
                    }
                }        
            }
        });
        
        m_tlv_extractor_thread.start();
    }
    
    /**
     * User should use this function when they don't use TLVExtractor any more.
     */
    public void destroy() {
        m_is_running = false;
        
        m_tlv_extractor_thread.interrupt();
        m_tlv_extractor_thread = null;
        
        m_listeners.clear();
        m_listeners = null;
                
        m_tlv_packets.clear();
        m_tlv_packets = null;
        
    }
    
    /**
     * Application should add their own listener to recieve tables, ntp, and so on which
     * SDK can send
     * @param listener
     */
    public void addEventListener(IMediaExtractorListener listener) {
        m_listeners.add(listener);
    }
    
    public void removeEventListener(IMediaExtractorListener listener) {
        m_listeners.remove(listener);
    }
    
    /**
     * User can put a TLV packet to get the results as Table of MMT-SI, TLV-SI and more.
     * @param tlv a variable TLV packet
     * @return Return false if TLVExtractor has situation which can't parse like overflow.
     */
    public boolean put(byte[] tlv) {
        try {
            if ( m_is_running == true && m_tlv_packets != null && tlv != null ) {
                m_tlv_packets.put(tlv);
            } else {
                return false;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Sending a table to application
     * @param table
     */
    protected synchronized void emitTable(Table table) {
        if ( table != null ) {
            for ( int i=0; i<m_listeners.size(); i++ ) {
                //m_listeners.get(i).onReceivedTable(table);
            }
        }
    }
    
    protected void putTableToEmitAsEvent(Table table) throws InterruptedException {
        if ( table == null ) return;
        
    }
    
    protected synchronized void processTLV(TypeLengthValue tlv) 
            throws InterruptedException, IOException {
        switch ( tlv.getPacketType() ) {
            case PacketFactory.SIGNALLING_PACKET:
            case PacketFactory.IPV4_PACKET:
            case PacketFactory.IPV6_PACKET:
                break;
            case PacketFactory.COMPRESSED_IP_PACKET:
                CompressedIpPacket cip = (CompressedIpPacket) tlv;
                MMTP_Packet mmtp_packet = cip.getPacketData().mmtp_packet;
                
                if ( mmtp_packet == null ) break;
                
                /**
                 * @note MPU-MFU
                 */
                if ( 0x00 == mmtp_packet.getPayloadType() ) {
                    processMmtpMpu(mmtp_packet);
                }
                break;
            default:
                break;
        }
    }
    
    protected void processMmtpMpu(MMTP_Packet mmtp) {
        int packet_id = mmtp.getPacketId();
        MMTP_Payload_MPU mpu = mmtp.getMPU();
        
        if ( m_video_pid_filters.contains(packet_id) ) {
            /**
             * Video Filtering
             */
            Logger.d(String.format("[V] pid : 0x%04x, ptype : 0x%x, psn : 0x%08x, " + 
                    "pc : 0x%x, msn : 0x%08x, f_i : 0x%x  \n", 
                    packet_id, mmtp.getPayloadType(), mmtp.getPacketSequenceNumber(),
                    mmtp.getPacketCounter(), mpu.getMPUSequenceNumber(),
                    mpu.getFragmentationIndicator()));
            
        } 
        
        if ( m_audio_pid_filters.contains(packet_id) ) {
            /**
             * Audio Filtering
             */
            Logger.d(String.format("[A] pid : 0x%04x, ptype : 0x%x, psn : 0x%08x, " + 
                    "pc : 0x%x, msn : 0x%08x, f_i : 0x%x  \n", 
                    packet_id, mmtp.getPayloadType(), mmtp.getPacketSequenceNumber(),
                    mmtp.getPacketCounter(), mpu.getMPUSequenceNumber(),
                    mpu.getFragmentationIndicator()));

        }
        
        if ( m_data_pid_filters.contains(packet_id) ) {
            /**
             * Data Filtering
             */
            Logger.d(String.format("[D] pid : 0x%04x, ptype : 0x%x, psn : 0x%08x, " + 
                    "pc : 0x%x, msn : 0x%08x, f_i : 0x%x  \n", 
                    packet_id, mmtp.getPayloadType(), mmtp.getPacketSequenceNumber(),
                    mmtp.getPacketCounter(), mpu.getMPUSequenceNumber(),
                    mpu.getFragmentationIndicator()));

        }
        
    }
    
    public void setVideoPidFilter(List<Integer> video_pids) {
        m_video_pid_filters = video_pids;
    }
    
    public void setAudioPidFilter(List<Integer> audio_pids) {
        m_audio_pid_filters = audio_pids;
    }
    
    public void setDataPidFilter(List<Integer> data_pids) {
        m_data_pid_filters = data_pids;
    }
    
}
