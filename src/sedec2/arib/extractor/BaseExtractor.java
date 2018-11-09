package sedec2.arib.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.arib.tlv.mmt.MessageFactory;
import sedec2.arib.tlv.mmt.messages.Message;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Payload_MPU;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Payload_MPU.MFU;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Payload_SignallingMessage;
import sedec2.util.Logger;

public abstract class BaseExtractor {
    protected boolean m_is_running = true;
    protected boolean m_enable_logging = false;
    protected boolean m_enable_pre_modification = true;

    protected List<Listener> m_listeners = new ArrayList<>();
    protected List<Byte> m_byte_id_filter = new ArrayList<>();
    protected List<Integer> m_int_id_filter = new ArrayList<>();
    
    protected Thread m_event_thread;
    protected Thread m_tlv_extractor_thread;
    protected BlockingQueue<byte[]> m_tlv_packets = null;
    protected BlockingQueue<QueueData> m_event_queue = null;
    protected List<MMTP_Packet> m_fragmented01_mmtp = new ArrayList<>();
    protected List<MMTP_Packet> m_fragmented02_mmtp = new ArrayList<>();

    public interface Listener {
        
    }
    
    public class QueueData {
        public int packet_id;
        public byte[] data;
        
        public QueueData() {
            
        }
        
        public QueueData(int pid, byte[] data) {
            this.packet_id = pid;
            this.data = data;
        }
    }
    
    public BaseExtractor() {
        m_tlv_packets = new ArrayBlockingQueue<byte[]>(100);
        m_event_queue = new ArrayBlockingQueue<QueueData>(100);
        
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
                            
                            if ( tlv != null ) process(tlv); 
                        }
                        
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * @note Nothing to do
                         */
                    } catch ( Exception e ) {
                        /**
                         * @todo You should remove a line below like break, exit statement, 
                         * because TLVExtractor has to keep alive even though 
                         * TLVExtractor get any wrong packets.
                         */
                        e.printStackTrace();
                    }
                }        
            }
        });
        m_tlv_extractor_thread.start();
    }
    
    public void destroy() {
        m_is_running = false;
        
        m_tlv_extractor_thread.interrupt();
        m_tlv_extractor_thread = null;
        
        m_tlv_packets.clear();
        m_tlv_packets = null;
        
        m_listeners.clear();
        m_listeners = null;
    }
    
    public void addEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }
    
    public void removeEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == true ) {
            m_listeners.remove(listener);
        }
    }

    public void putIn(byte[] tlv) throws InterruptedException {
        if ( m_is_running == true && m_tlv_packets != null && tlv != null ) {
            m_tlv_packets.put(tlv);
        }
    }
    
    protected abstract void process(TypeLengthValue tlv) 
            throws InterruptedException, IOException;

    protected void putOut(Object obj) throws InterruptedException {
        if ( obj == null ) return;
        
        m_event_queue.put((QueueData)obj);
    }
    
    public void addPidFilter(int id) { 
        if ( m_int_id_filter.contains(id) == false ) {
            m_int_id_filter.add(id);
        }
    }    
    
    public void addPidFilter(byte id) { 
        if ( m_byte_id_filter.contains(id) == false ) {
            m_byte_id_filter.add(id);
        }
    }    

    public void removePidFilter(int id) { 
        if ( m_int_id_filter.contains(id) == true ) {
            m_int_id_filter.remove(id);
        }
    }
    
    public void removePidFilter(byte id) { 
        if ( m_byte_id_filter.contains(id) == true ) {
            m_byte_id_filter.remove(id);
        }
    }
    
    public void enableLogging() {
        m_enable_logging = true;
    }
    
    public void disableLogging() {
        m_enable_logging = false;
    }

    public void enablePreModification() {
        m_enable_pre_modification = true;
    }
    
    public void disablePreModification() {
        m_enable_pre_modification = false;
    }
    
    protected void showMMTPInfo(String type, MMTP_Packet mmpt) {
        if ( m_enable_logging == true ) {
            Logger.d(String.format("[%s] pid : 0x%04x, [%s] psn : 0x08x \n", 
                    type, mmpt.getPacketId(),
                    mmpt.getPayloadType() == 0x00 ? "MPU" : "Control Message",
                    mmpt.getPacketSequenceNumber()));
        }
    }
    
    protected List<ByteArrayOutputStream> getMFU(MMTP_Packet mmtp) 
            throws InterruptedException, IOException {
        List<MFU> mfus = null;
        MMTP_Payload_MPU mpu = mmtp.getMPU();
        List<ByteArrayOutputStream> samples = new ArrayList<>();

        switch ( mpu.getFragmentationIndicator() ) {
            case 0x00:
                mfus = mpu.getMFUList();
                for ( int i=0; i<mfus.size(); i++ ) {
                    ByteArrayOutputStream sample = new ByteArrayOutputStream();
                    sample.write(mfus.get(i).MFU_data_byte);
                    samples.add(sample);
                }
                break;
            case 0x01:
                m_fragmented01_mmtp.add(mmtp);
                break;
            case 0x02:
                m_fragmented02_mmtp.add(mmtp);
                break;
            case 0x03:
                boolean found_01_fragmentation_indicator = false;
                ByteArrayOutputStream sample = new ByteArrayOutputStream();
                for ( Iterator<MMTP_Packet> it = m_fragmented01_mmtp.iterator() ; 
                        it.hasNext() ; ) {
                    MMTP_Payload_MPU mpu01 = it.next().getMPU();
                    if( mpu01.getFragmentationIndicator() == 0x01 ) {
                        mfus = mpu01.getMFUList();
                        for ( int i=0; i<mfus.size(); i++ ) {
                            sample.write(mfus.get(i).MFU_data_byte);
                        }
                        it.remove();
                        found_01_fragmentation_indicator = true;
                        break;
                    }
                }
                
                if ( found_01_fragmentation_indicator == true ) {
                    for ( Iterator<MMTP_Packet> it = m_fragmented02_mmtp.iterator() ; 
                            it.hasNext() ; ) {
                        MMTP_Payload_MPU mpu02 = it.next().getMPU();
                        if( mpu02.getFragmentationIndicator() == 0x02 ) {
                            mfus = mpu02.getMFUList();
                            for ( int i=0; i<mfus.size(); i++ ) {
                                sample.write(mfus.get(i).MFU_data_byte);
                            }
                            it.remove();
                        }
                    } 
                
                    mfus = mpu.getMFUList();
                    for ( int i=0; i<mfus.size(); i++ ) {
                        sample.write(mfus.get(i).MFU_data_byte);
                    }
                    samples.add(sample);
                }
                break;
        }
        return samples;
    }
    
    protected Message getSinallingMessage(MMTP_Packet mmtp) 
            throws IOException, InterruptedException {
        Message message = null;
        MMTP_Payload_SignallingMessage signal_message = mmtp.getSignallingMessage();
        
        if ( signal_message != null ) {
            int fragmentaion_indicator = 
                    signal_message.getFragmentationIndicator() & 0xff;
            switch ( fragmentaion_indicator ) {
                case 0x00:
                    byte[] buffer = signal_message.getMessageByte();
                    message = MessageFactory.createMessage(buffer);
                    break;
                case 0x01:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves header part of divided data.
                     */
                    m_fragmented01_mmtp.add(mmtp);
                    break;
                case 0x02:
                    /**
                     * @note ARIB B60 Table 6-2
                     * This involves a part of divided data which is \n
                     * neither header part nor last part.
                     */
                    m_fragmented02_mmtp.add(mmtp);
                    break;
                case 0x03:
                    boolean found_01_fragmentation_indicator = false;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    /**
                     * @note ARIB B60 Table 6-2, This involves last part of divided data.
                     * In other words, it's a timing to make whole section table since 0x03 is last packet
                     * after gathering fragmented data of tables.
                     * Case.1 0x01 of fragmentation_indicator will be only one thing.  
                     */
                    for ( Iterator<MMTP_Packet> it = 
                            m_fragmented01_mmtp.iterator() ; 
                            it.hasNext() ; ) {
                        MMTP_Packet mmtp01 = it.next();
                        MMTP_Payload_SignallingMessage sm = mmtp01.getSignallingMessage();
                        if( mmtp01.getPacketId() == mmtp.getPacketId() && 
                                sm != null &&
                                sm.getFragmentationIndicator() == 0x01 ) {
                            outputStream.write(sm.getMessageByte());
                            it.remove();
                            found_01_fragmentation_indicator = true;
                            break;
                        }
                    }
              
                    /**
                     * Case.2 0x02 of fragmentation_indicator will be multiple things  
                     */
                    if ( found_01_fragmentation_indicator == true ) {
                        for(Iterator<MMTP_Packet> it = 
                                m_fragmented02_mmtp.iterator() ; 
                                it.hasNext() ; ) {
                            MMTP_Packet mmtp02 = it.next();
                            MMTP_Payload_SignallingMessage sm = mmtp02.getSignallingMessage();
                            if( mmtp02.getPacketId() == mmtp.getPacketId() &&
                                    sm != null && 
                                    sm.getFragmentationIndicator() == 0x02 ) {
                                outputStream.write(sm.getMessageByte());
                                it.remove();
                            }
                        }
                    
                        /**
                         * Case.2 0x03 of fragmentation_indicator will be only one thing as last
                         */
                        outputStream.write(signal_message.getMessageByte());
                        
                        /**
                         * As a result, outputStream has whole data of tables
                         */
                        message = MessageFactory.createMessage(outputStream.toByteArray());
                    }
                    break;
            }
        }
        return message;
    }
}
