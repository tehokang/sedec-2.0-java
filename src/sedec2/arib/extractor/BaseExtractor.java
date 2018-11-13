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

/**
 * BaseExtractor has implementations which are able to parse TLV, MMTP packet thus 
 * it is a common module which can be extend to each extractor for other purpose like
 * video, audio, ttml, application and so on.
 */
public abstract class BaseExtractor {
    /**
     * Interval of a thread loop cycle(micro-seconds)
     */
    protected int m_sleep_micro_interval = 100;
    
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
     * Extracting thread which can pull out a TLV packet from TLV packets queue
     */
    protected Thread m_tlv_extractor_thread;
    
    /**
     * TLV packets queue
     */
    protected BlockingQueue<byte[]> m_tlv_packets = null;
    
    /**
     * Event queue which can sent to user
     */
    protected BlockingQueue<QueueData> m_event_queue = null;
    
    /**
     * MMTP packets which have 1 or 2 of fragmentation_indicator.
     * 1 or 2 means the packet is still fragmented and the packet of packet_id has to
     * collect more things by 3.
     */
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
                        Thread.sleep(0, 1);
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
    
    /**
     * User has to call destory() for rapid garbage collecting
     */
    public void destroy() {
        m_is_running = false;
        
        m_tlv_extractor_thread.interrupt();
        m_tlv_extractor_thread = null;
        
        m_tlv_packets.clear();
        m_tlv_packets = null;
        
        m_listeners.clear();
        m_listeners = null;
    }
    
    /**
     * User can set an interval of thread loop as micro-seconds
     * @param micro_sec
     */
    public void setThreadInterval(int micro_sec) {
        m_sleep_micro_interval = micro_sec;
    }
    
    /**
     * User can add event-listener to get information from each extractor
     * @param listener
     */
    public void addEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }
    
    /**
     * User can remove event-listener not to get information
     * @param listener
     */
    public void removeEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == true ) {
            m_listeners.remove(listener);
        }
    }

    /**
     * User should put a TLV packet into extractor, the packet will be collected as kinds of them 
     * @param tlv
     * @throws InterruptedException
     */
    public void putIn(byte[] tlv) throws InterruptedException {
        if ( m_is_running == true && m_tlv_packets != null && tlv != null ) {
            m_tlv_packets.put(tlv);
        }
    }
    
    /**
     * Child extractor of BaseExtractor must implement process function to control TLV packets 
     * as kinds of them. TLV can be IPv4, IPv6, CompressedPacket, SignallingPacket, NullPacket.
     * @param tlv
     * @throws InterruptedException
     * @throws IOException
     */
    protected abstract void process(TypeLengthValue tlv) 
            throws InterruptedException, IOException;

    /**
     * Internal function to make a result after a TLV
     * @param obj
     * @throws InterruptedException
     */
    protected void putOut(Object obj) throws InterruptedException {
        if ( obj == null ) return;
        
        m_event_queue.put((QueueData)obj);
    }
    
    /**
     * Add an id of MMTP packet to do filtering
     * @param id
     */
    public void addPidFilter(int id) { 
        if ( m_int_id_filter.contains(id) == false ) {
            m_int_id_filter.add(id);
        }
    }    
    
    /**
     * Add an id of Table to do filtering
     * @param id
     */
    public void addPidFilter(byte id) { 
        if ( m_byte_id_filter.contains(id) == false ) {
            m_byte_id_filter.add(id);
        }
    }    

    /**
     * Remove an id of MMTP packet not to do filtering
     * @param id
     */
    public void removePidFilter(int id) { 
        if ( m_int_id_filter.contains(id) == true ) {
            m_int_id_filter.remove(id);
        }
    }
    
    /**
     * Remove an id of Table not to do filtering
     * @param id
     */
    public void removePidFilter(byte id) { 
        if ( m_byte_id_filter.contains(id) == true ) {
            m_byte_id_filter.remove(id);
        }
    }
    
    /**
     * Function which can enable logging of extractors own
     */
    public void enableLogging() {
        m_enable_logging = true;
    }
    
    /**
     * Function which can disable logging of extractors own
     */
    public void disableLogging() {
        m_enable_logging = false;
    }

    /**
     * Function which can use pre-modification like NAL unit's header of video,
     * syncword of audio. Implementor of extractor should control this flag when they set.
     */
    public void enablePreModification() {
        m_enable_pre_modification = true;
    }
    
    /**
     * Function which can not use pre-modification.
     * Extractor should pass MFU original data without modification to user when they're disabled.
     */
    public void disablePreModification() {
        m_enable_pre_modification = false;
    }
    
    /**
     * Function to get a whole MFU of MMTP payload which is already gathered
     * @param mmtp
     * @return MFU byte array(not fragmented, it's already collected as whole)
     * @throws InterruptedException
     * @throws IOException
     */
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
                    MMTP_Packet mmtp01 = it.next();
                    if ( mmtp01.getPacketId() != mmtp.getPacketId() ) continue;
                    
                    MMTP_Payload_MPU mpu01 = mmtp01.getMPU();
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
                        MMTP_Packet mmtp02 = it.next();
                        if ( mmtp02.getPacketId() != mmtp.getPacketId() ) continue;
                        
                        MMTP_Payload_MPU mpu02 = mmtp02.getMPU();
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
    
    /**
     * Function to get table(AKA section) of ARIB MMT-SI, TLV-SI from MMTP payload
     * like PA, CA, M2Section, M2ShortSection, DataTransmissionMessage.
     * @param mmtp
     * @return Signal Message
     * @throws IOException
     * @throws InterruptedException
     */
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
