package sedec2.arib.tlv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Payload_MPU;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Payload_MPU.MFU;
import sedec2.util.Logger;

public class TlvMpuExtractor {
    public interface IMediaExtractorListener {
        public void onReceivedVideo(int packet_id, byte[] buffer);
        public void onReceivedAudio(int packet_id, byte[] buffer);
        public void onReceivedTimedText(int packet_id, byte[] buffer);
    }

    class QueueData {
        public int packet_id;
        public byte[] data;
        
        public QueueData(int pid, byte[] data) {
            this.packet_id = pid;
            this.data = data;
        }
    }
    
    protected final String TAG = "TlvMpuExtractor";
    
    List<Integer> m_video_pid_filters = new ArrayList<>();
    List<Integer> m_audio_pid_filters = new ArrayList<>();
    List<Integer> m_timed_text_pid_filters = new ArrayList<>();
    List<Integer> m_app_pid_filters = new ArrayList<>();
    
    protected Thread m_tlv_extractor_thread;
    protected Thread m_mfu_video_event_thread;
    protected Thread m_mfu_audio_event_thread;
    protected Thread m_mfu_timed_text_event_thread;
    
    protected boolean m_is_running = true;
    
    protected List<IMediaExtractorListener> m_listeners = new ArrayList<>();

    protected BlockingQueue<QueueData> m_mfu_videos = new ArrayBlockingQueue<QueueData>(100);
    protected BlockingQueue<QueueData> m_mfu_audios = new ArrayBlockingQueue<QueueData>(100);
    protected BlockingQueue<QueueData> m_mfu_timed_texts = new ArrayBlockingQueue<QueueData>(100);
    protected BlockingQueue<byte[]> m_tlv_packets = new ArrayBlockingQueue<byte[]>(100);
    
    protected List<MMTP_Payload_MPU> m_fragmented01_mmtp_video_mpu = new ArrayList<>();
    protected List<MMTP_Payload_MPU> m_fragmented02_mmtp_video_mpu = new ArrayList<>();

    public TlvMpuExtractor() {
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
        
        m_mfu_video_event_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_mfu_videos ) emitVideoMfu(m_mfu_videos.take());
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /** 
                         * @note Nothing to do
                         */
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        m_mfu_audio_event_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_mfu_audios ) emitAudioMfu(m_mfu_audios.take());
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /** 
                         * @note Nothing to do
                         */
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        m_mfu_timed_text_event_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_mfu_timed_texts ) emitTimedTextMfu(m_mfu_timed_texts.take());
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /** 
                         * @note Nothing to do
                         */
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
        m_tlv_extractor_thread.start();
        m_mfu_video_event_thread.start();
//        m_mfu_audio_event_thread.start();
//        m_mfu_timed_text_event_thread.start();
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

    protected synchronized void emitVideoMfu(QueueData data) {
        if ( data != null ) {
            for ( int i=0; i<m_listeners.size(); i++ ) {
                m_listeners.get(i).onReceivedVideo(data.packet_id, data.data);
            }
        }
    }
    
    protected synchronized void emitAudioMfu(QueueData data) {
        if ( data != null ) {
            for ( int i=0; i<m_listeners.size(); i++ ) {
                m_listeners.get(i).onReceivedAudio(data.packet_id, data.data);
            }
        }
    }
    
    protected synchronized void emitTimedTextMfu(QueueData data) {
        if ( data != null ) {
            for ( int i=0; i<m_listeners.size(); i++ ) {
                m_listeners.get(i).onReceivedTimedText(data.packet_id, data.data);
            }
        }
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

    protected void put(QueueData data) throws InterruptedException {
        if ( data == null ) return;
        
        if ( m_video_pid_filters.contains(data.packet_id) == true ) {
            m_mfu_videos.put(data);
        }
        
        if ( m_audio_pid_filters.contains(data.packet_id) == true ) {
            m_mfu_audios.put(data);
        }
        
        if ( m_timed_text_pid_filters.contains(data.packet_id) == true ) {
            m_mfu_timed_texts.put(data);
        }
    }
    
    /**
     * @throws InterruptedException 
     * @throws IOException 
     * @note Video Filtering
     */
    protected void processMfuVideo(int packet_id, MMTP_Payload_MPU mpu) throws InterruptedException, IOException {
        List<MFU> mfus = null;
        byte[] nal_prefix = {0x00, 0x00, 0x00, 0x01};
        ByteArrayOutputStream outputStreamNal = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStreamRawMfu = new ByteArrayOutputStream();

        Logger.d(String.format("[V] pid : 0x%04x, " + 
                "msn : 0x%08x, f_i : 0x%x, timed_flag : 0x%x, a_f : 0x%x, mfu.size : %d \n", 
                packet_id, mpu.getMPUSequenceNumber(), mpu.getFragmentationIndicator(),
                mpu.getTimedFlag(), mpu.getAggregationFlag(), mpu.getMFUList().size()));
        
        switch ( mpu.getFragmentationIndicator() ) {
            case 0x00:
                mfus = mpu.getMFUList();
                for ( int i=0; i<mfus.size(); i++ ) {
                    /**
                     * @note Replacement length of NAL with NAL prefix 
                     */
                    System.arraycopy(nal_prefix, 0, mfus.get(i).MFU_data_byte, 0, nal_prefix.length);                    
                    outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                }
                outputStreamNal.write(outputStreamRawMfu.toByteArray());
                put(new QueueData(packet_id, outputStreamNal.toByteArray()));
                break;
            case 0x01:
                m_fragmented01_mmtp_video_mpu.add(mpu);
                break;
            case 0x02:
                m_fragmented02_mmtp_video_mpu.add(mpu);
                break;
            case 0x03:
                for ( Iterator<MMTP_Payload_MPU> it = m_fragmented01_mmtp_video_mpu.iterator() ; 
                        it.hasNext() ; ) {
                    MMTP_Payload_MPU mpu01 = it.next();
                    if( mpu01.getFragmentationIndicator() == 0x01 ) {
                        mfus = mpu01.getMFUList();
                        for ( int i=0; i<mfus.size(); i++ ) {
                        	if(i == 0) {
                                /**
                                 * @note Replacement length of NAL with NAL prefix 
                                 */
                        		System.arraycopy(nal_prefix, 0, mfus.get(i).MFU_data_byte, 0, nal_prefix.length);
                        	}
                            outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                        }
                        it.remove();
                        break;
                    }
                }
                
                for ( Iterator<MMTP_Payload_MPU> it = m_fragmented02_mmtp_video_mpu.iterator() ; 
                        it.hasNext() ; ) {
                    MMTP_Payload_MPU mpu02 = it.next();
                    if( mpu02.getFragmentationIndicator() == 0x02 ) {
                        mfus = mpu02.getMFUList();
                        for ( int i=0; i<mfus.size(); i++ ) {
                            outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                        }
                        it.remove();
                    }
                } 
                
                mfus = mpu.getMFUList();
                for ( int i=0; i<mfus.size(); i++ ) {
                    outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                }
                
                outputStreamNal.write(outputStreamRawMfu.toByteArray());
                put(new QueueData(packet_id, outputStreamNal.toByteArray()));
                break;
        }
    }
    
    protected void processMfuAudio(int packet_id, MMTP_Payload_MPU mpu) throws InterruptedException, IOException {
        List<MFU> mfus = null;
        ByteArrayOutputStream outputStreamRawMfu = new ByteArrayOutputStream();

        Logger.d(String.format("[A] pid : 0x%04x, " + 
                "msn : 0x%08x, f_i : 0x%x, timed_flag : 0x%x, a_f : 0x%x, mfu.size : %d \n", 
                packet_id, mpu.getMPUSequenceNumber(), mpu.getFragmentationIndicator(),
                mpu.getTimedFlag(), mpu.getAggregationFlag(), mpu.getMFUList().size()));
        
        switch ( mpu.getFragmentationIndicator() ) {
            case 0x00:
                mfus = mpu.getMFUList();
                for ( int i=0; i<mfus.size(); i++ ) {
                    /**
                     * @note Gathering MFU
                     */
                }
                put(new QueueData(packet_id, outputStreamRawMfu.toByteArray()));
                break;
            case 0x01:
                m_fragmented01_mmtp_video_mpu.add(mpu);
                break;
            case 0x02:
                m_fragmented02_mmtp_video_mpu.add(mpu);
                break;
            case 0x03:
                for ( Iterator<MMTP_Payload_MPU> it = m_fragmented01_mmtp_video_mpu.iterator() ; 
                        it.hasNext() ; ) {
                    MMTP_Payload_MPU mpu01 = it.next();
                    if( mpu01.getFragmentationIndicator() == 0x01 ) {
                        mfus = mpu01.getMFUList();
                        for ( int i=0; i<mfus.size(); i++ ) {
                            /**
                             * @note Gathering MFU
                             */
                            outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                        }
                        it.remove();
                        break;
                    }
                }
                
                for ( Iterator<MMTP_Payload_MPU> it = m_fragmented02_mmtp_video_mpu.iterator() ; 
                        it.hasNext() ; ) {
                    MMTP_Payload_MPU mpu02 = it.next();
                    if( mpu02.getFragmentationIndicator() == 0x02 ) {
                        mfus = mpu02.getMFUList();
                        for ( int i=0; i<mfus.size(); i++ ) {
                            outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                        }
                        it.remove();
                    }
                } 
                
                mfus = mpu.getMFUList();
                for ( int i=0; i<mfus.size(); i++ ) {
                    outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                }
                put(new QueueData(packet_id, outputStreamRawMfu.toByteArray()));
                break;
        }
    }
    
    protected void processMfuData(int packet_id, MMTP_Payload_MPU mpu) {
        /**
         * Data Filtering
         */
    }
    
    protected void processMmtpMpu(MMTP_Packet mmtp) throws InterruptedException, IOException {
        int packet_id = mmtp.getPacketId();
        MMTP_Payload_MPU mpu = mmtp.getMPU();
        
        if ( m_video_pid_filters.contains(packet_id) ) {
            processMfuVideo(packet_id, mpu);
        } 
        
        if ( m_audio_pid_filters.contains(packet_id) ) {
            processMfuAudio(packet_id, mpu);
        }
        
        if ( m_timed_text_pid_filters.contains(packet_id) ) {
            processMfuData(packet_id, mpu);
        }
    }
    
    public void setVideoPidFilter(List<Integer> video_pids) {
        m_video_pid_filters = video_pids;
    }
    
    public void setAudioPidFilter(List<Integer> audio_pids) {
        m_audio_pid_filters = audio_pids;
    }
    
    public void setTimedTextPidFilter(List<Integer> data_pids) {
        m_timed_text_pid_filters = data_pids;
    }
    
    public void setApplicationPidFilter(List<Integer> app_pids) {
        m_app_pid_filters = app_pids;
    }
}
