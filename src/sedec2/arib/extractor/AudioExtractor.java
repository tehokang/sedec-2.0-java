package sedec2.arib.extractor;

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
import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class AudioExtractor extends BaseExtractor {
    public interface IAudioExtractorListener extends BaseExtractor.Listener {
        public void onReceivedAudio(int packet_id, byte[] buffer);
    }

    class QueueData {
        public int packet_id;
        public byte[] data;
        
        public QueueData(int pid, byte[] data) {
            this.packet_id = pid;
            this.data = data;
        }
    }

    protected final String TAG = "AudioExtractor";
    protected boolean m_is_running = true;
    protected Thread m_tlv_extractor_thread;
    protected Thread m_mfu_audio_event_thread;
        
    protected List<MMTP_Packet> m_fragmented01_mmtp_audio = new ArrayList<>();
    protected List<MMTP_Packet> m_fragmented02_mmtp_audio = new ArrayList<>();

    protected BlockingQueue<byte[]> m_tlv_packets = new ArrayBlockingQueue<byte[]>(100);
    protected BlockingQueue<QueueData> m_mfu_audios = new ArrayBlockingQueue<QueueData>(100);
    
    public AudioExtractor() {
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
                            process(tlv); 
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
        
        m_mfu_audio_event_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                QueueData data = null;
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(1);
                        if ( null != m_mfu_audios && ( data = m_mfu_audios.take()) != null) {
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((IAudioExtractorListener)m_listeners.get(i)).
                                        onReceivedAudio(data.packet_id, data.data);
                            }
                        }
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
        m_mfu_audio_event_thread.start();
    }
    
    /**
     * User should use this function when they don't use TLVExtractor any more.
     */
    public void destroy() {
        m_is_running = false;
        
        m_tlv_extractor_thread.interrupt();
        m_tlv_extractor_thread = null;
        
        m_mfu_audio_event_thread.interrupt();
        m_mfu_audio_event_thread = null;
        
        m_listeners.clear();
        m_listeners = null;
                
        m_tlv_packets.clear();
        m_tlv_packets = null;
        
    }
    
    /**
     * User can put a TLV packet to get the results as Table of MMT-SI, TLV-SI and more.
     * @param tlv a variable TLV packet
     * @return Return false if TLVExtractor has situation which can't parse like overflow.
     * @throws InterruptedException 
     */
    public void putIn(byte[] tlv) throws InterruptedException {
        if ( m_is_running == true && m_tlv_packets != null && tlv != null ) {
            m_tlv_packets.put(tlv);
        } 
    }

    @Override
    protected void putOut(Object obj) throws InterruptedException {
        if ( obj == null ) return;
        
        m_mfu_audios.put((QueueData)obj);
    }
    
    protected synchronized void process(TypeLengthValue tlv) 
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
                    if ( m_int_id_filter.contains(mmtp_packet.getPacketId()) ) {
                        processMfuAudio(mmtp_packet);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * processMfuAudio assumes that audio should be mpeg4-audio(aac or als) specified in ARIB B60
     * @throws InterruptedException 
     * @throws IOException 
     * @note Video Filtering
     */
    protected void processMfuAudio(MMTP_Packet mmtp) 
            throws InterruptedException, IOException {
        int dataUnitLen = 0;
        List<MFU> mfus = null;
        int packet_id = mmtp.getPacketId();
        MMTP_Payload_MPU mpu = mmtp.getMPU();
        ByteArrayOutputStream outputStreamRawMfu = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStreamAudio = new ByteArrayOutputStream();
        /**
         * @note Please refer to 8.2 Transmission of Audio Signal in ARIB B60
         * synchronization byte and length information should be added
         */
        BitReadWriter syncword = new BitReadWriter(new byte[3]);
        syncword.writeOnBuffer((int)0x2b7, 11);
        
        /**
         * @note Please enable following if you'd like to see video sequence flow
         */
        if ( enable_logging == true ) {
            Logger.d(String.format("[A] pid : 0x%04x, " + 
                    "msn : 0x%08x, f_i : 0x%x, timed_flag : 0x%x, a_f : 0x%x, mfu.size : %d \n", 
                    packet_id, mpu.getMPUSequenceNumber(), mpu.getFragmentationIndicator(),
                    mpu.getTimedFlag(), mpu.getAggregationFlag(), mpu.getMFUList().size()));
        }
        
        switch ( mpu.getFragmentationIndicator() ) {
            case 0x00:
                mfus = mpu.getMFUList();
                for ( int i=0; i<mfus.size(); i++ ) {
                    dataUnitLen += mfus.get(i).MFU_data_byte.length;
                    outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);                   
                }                
                syncword.writeOnBuffer(dataUnitLen, 13);
                outputStreamAudio.write(syncword.getBuffer());
                outputStreamAudio.write(outputStreamRawMfu.toByteArray());
                putOut(new QueueData(packet_id, outputStreamAudio.toByteArray()));
                break;
            case 0x01:
                m_fragmented01_mmtp_audio.add(mmtp);
                break;
            case 0x02:
                m_fragmented02_mmtp_audio.add(mmtp);
                break;
            case 0x03:
                boolean found_01_fragmentation_indicator = false;
                for ( Iterator<MMTP_Packet> it = m_fragmented01_mmtp_audio.iterator() ; 
                        it.hasNext() ; ) {
                    MMTP_Payload_MPU mpu01 = it.next().getMPU();
                    if( mpu01.getFragmentationIndicator() == 0x01 ) {
                        mfus = mpu01.getMFUList();
                        for ( int i=0; i<mfus.size(); i++ ) {
                            dataUnitLen += mfus.get(i).MFU_data_byte.length;
                            outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                        }
                        it.remove();
                        found_01_fragmentation_indicator = true;
                        break;
                    }
                }
                
                if ( found_01_fragmentation_indicator == true ) {
                    for ( Iterator<MMTP_Packet> it = m_fragmented02_mmtp_audio.iterator() ; 
                            it.hasNext() ; ) {
                        MMTP_Payload_MPU mpu02 = it.next().getMPU();
                        if( mpu02.getFragmentationIndicator() == 0x02 ) {
                            mfus = mpu02.getMFUList();
                            for ( int i=0; i<mfus.size(); i++ ) {
                                dataUnitLen += mfus.get(i).MFU_data_byte.length;
                                outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                            }
                            it.remove();
                        }
                    } 
                
                    mfus = mpu.getMFUList();
                    for ( int i=0; i<mfus.size(); i++ ) {
                        dataUnitLen += mfus.get(i).MFU_data_byte.length;
                        outputStreamRawMfu.write(mfus.get(i).MFU_data_byte);
                    }
                    syncword.writeOnBuffer(dataUnitLen, 13);
                    outputStreamAudio.write(syncword.getBuffer());
                    outputStreamAudio.write(outputStreamRawMfu.toByteArray());
                    putOut(new QueueData(packet_id, outputStreamAudio.toByteArray()));
                }
                break;
        }
    }

}
