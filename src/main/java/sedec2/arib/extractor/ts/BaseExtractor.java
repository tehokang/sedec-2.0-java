package sedec2.arib.extractor.ts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.mmtp.MMTP_Packet;
import sedec2.arib.tlv.container.mmtp.MMTP_Payload_MPU;
import sedec2.arib.tlv.container.mmtp.MMTP_Payload_MPU.MFU;
import sedec2.arib.tlv.container.mmtp.MMTP_Payload_SignalingMessage;
import sedec2.arib.tlv.container.mmtp.MessageFactory;
import sedec2.arib.tlv.container.mmtp.messages.Message;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.util.Logger;

/**
 * BaseExtractor has implementations which are able to parse TLV, MMTP packet.
 * This is a common functionality which can be extend to each extractor for other purpose like
 * adding NAL prefix into video or syncword into audio and so on.
 */
public abstract class BaseExtractor {
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
     * Extracting thread which can pull out a TLV packet from TLV packets raw queue
     */
    protected Thread m_tlv_raw_extractor_thread;

    /**
     * Extracting thread which can pull out a TLV packet from TLV packets formatted queue
     */
    protected Thread m_tlv_formatted_extractor_thread;

    /**
     * TLV packets queue as raw
     */
    protected BlockingQueue<byte[]> m_tlv_raw_packets = null;

    /**
     * TLV packets queue as formatted
     */
    protected BlockingQueue<TypeLengthValue> m_tlv_formatted_packets = null;

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

    protected static final String TAG = BaseExtractor.class.getSimpleName();

    /**
     * Every Extractor has to inherit from this so that user can use unified listener.
     */
    public interface Listener {

    }

    protected class QueueData {
        public int packet_id;
        public byte[] data;

        public QueueData() {

        }

        public QueueData(int pid, byte[] data) {
            this.packet_id = pid;
            this.data = data;
        }
    }

    /**
     * Constructor which initialize queue buffer of TLV input packet, Event and
     * start running thread with blocking queue which only run when queue obtain an input.
     */
    public BaseExtractor() {
        m_tlv_raw_packets = new ArrayBlockingQueue<byte[]>(100);
        m_tlv_formatted_packets = new ArrayBlockingQueue<TypeLengthValue>(100);
        m_event_queue = new ArrayBlockingQueue<QueueData>(100);

        m_tlv_raw_extractor_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TypeLengthValue tlv = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_tlv_raw_packets ) {
                            byte[] tlv_raw = m_tlv_raw_packets.take();
                            tlv = PacketFactory.createPacket(tlv_raw);

                            if ( tlv != null ) process(tlv);
                        }

                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG,
                                String.format("Error while parsing TLV (type %d)\n",
                                tlv.getPacketType()));
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * Nothing to do
                         */
                    } catch ( Exception e ) {
                        /**
                         * You should remove a line below like break, exit statement,
                         * because TLVExtractor has to keep alive even though
                         * TLVExtractor get any wrong packets.
                         */
                        e.printStackTrace();
                    }
                }
            }
        });

        m_tlv_formatted_extractor_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TypeLengthValue tlv = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_tlv_formatted_packets ) {
                            tlv = m_tlv_formatted_packets.take();
                            if ( tlv != null ) process(tlv);
                        }

                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG,
                                String.format("Error while parsing TLV (type %d)\n",
                                tlv.getPacketType()));
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * Nothing to do
                         */
                    } catch ( Exception e ) {
                        /**
                         * You should remove a line below like break, exit statement,
                         * because TLVExtractor has to keep alive even though
                         * TLVExtractor get any wrong packets.
                         */
                        e.printStackTrace();
                    }
                }
            }
        });

        m_tlv_formatted_extractor_thread.start();
        m_tlv_raw_extractor_thread.start();
    }

    /**
     * Clear all of queue containing TLV, Event and MMTP fragmented packets.
     */
    public void clearQueue() {
        m_tlv_raw_packets.clear();
        m_tlv_formatted_packets.clear();
        m_event_queue.clear();

        m_fragmented01_mmtp.clear();
        m_fragmented02_mmtp.clear();
    }

    /**
     * Clean up all of resource under BaseExtractor.
     * User must call this after their job finished.
     */
    public void destroy() {
        m_is_running = false;

        m_tlv_formatted_extractor_thread.interrupt();
        m_tlv_formatted_extractor_thread = null;

        m_tlv_raw_extractor_thread.interrupt();
        m_tlv_raw_extractor_thread = null;

        clearQueue();

        m_event_queue = null;
        m_fragmented01_mmtp = null;
        m_fragmented02_mmtp = null;
        m_tlv_raw_packets = null;
        m_tlv_formatted_packets = null;

        m_listeners.clear();
        m_listeners = null;
    }

    /**
     * User can add event-listener to get information from each extractor
     * @param listener to get information from Extractor
     */
    public void addEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }

    /**
     * User can remove event-listener not to get information
     * @param listener which added
     */
    public void removeEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == true ) {
            m_listeners.remove(listener);
        }
    }

    /**
     * User should put a TLV packet into extractor, the packet will be collected as kinds of them
     * @param tlv one TLV packet
     * @throws InterruptedException occur when thread interrupted
     */
    public void putIn(byte[] tlv) throws InterruptedException {
        if ( m_is_running == true && m_tlv_raw_packets != null && tlv != null ) {
            m_tlv_raw_packets.put(tlv);
        }
    }

    public void putIn(TypeLengthValue tlv) throws InterruptedException {
        if ( m_is_running == true && m_tlv_raw_packets != null && tlv != null ) {
            m_tlv_formatted_packets.put(tlv);
        }
    }

    /**
     * Child extractor of BaseExtractor must implement process function to control TLV packets
     * as kinds of them. TLV can be IPv4, IPv6, CompressedPacket, SignallingPacket, NullPacket.
     * @param tlv one TLV packet already decoded
     * @throws InterruptedException occur when thread interrupted
     * @throws IOException occur when ByteBuffer has problem
     */
    protected abstract void process(TypeLengthValue tlv)
            throws InterruptedException, IOException;

    /**
     * Internal function to make a result after a TLV
     * @param event to emit to user
     * @throws InterruptedException occur when event thread has interrupted
     */
    protected void putOut(QueueData event) throws InterruptedException {
        if ( event == null ) return;

        m_event_queue.put(event);
    }

    /**
     * Add a filter to get MFU corresponding only to packet_id of MMTP.
     * @param id packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void addPidFilter(int id) {
        if ( m_int_id_filter.contains(id) == false ) {
            m_int_id_filter.add(id);
        }
    }

    /**
     * Add a filter to get SI corresponding only to table_id of Private Section.
     * @param id specific table id which user wants to get
     *
     * <p>
     * Table ID refers to 2.4.4.10 Syntax of the Private section in ISO13838-1
     */
    public void addPidFilter(byte id) {
        if ( m_byte_id_filter.contains(id) == false ) {
            m_byte_id_filter.add(id);
        }
    }

    /**
     * Remove a filter which user added corresponding only to packet_id of MMTP.
     * @param id packet_id of MMTP
     *
     * <p>
     * Packet ID refers to 6.4 MMTP Packet of ARIB STD-B60
     */
    public void removePidFilter(int id) {
        if ( m_int_id_filter.contains(id) == true ) {
            m_int_id_filter.remove(id);
        }
    }

    /**
     * Remove a filter as table id of Private Section.
     * @param id which user doesn't want to receive via
     */
    public void removePidFilter(byte id) {
        if ( m_byte_id_filter.contains(id) == true ) {
            m_byte_id_filter.remove(id);
        }
    }

    /**
     * Enable logging while extracting.
     * {@link BaseExtractor#disableLogging()}
     */
    public void enableLogging() {
        m_enable_logging = true;
    }

    /**
     * Enable logging while extracting.
     * {@link BaseExtractor#enableLogging()}
     */
    public void disableLogging() {
        m_enable_logging = false;
    }

    /**
     * Enable modification of which can be added.
     * The child of BaseExtractor can get a chance to modify before final event emit.
     * {@link BaseExtractor#disablePreModification()}
     */
    public void enablePreModification() {
        m_enable_pre_modification = true;
    }

    /**
     * Disable modification of which can be added.
     * {@link BaseExtractor#enablePreModification()}
     */
    public void disablePreModification() {
        m_enable_pre_modification = false;
    }

    /**
     * Gets a whole MFU of MMTP payload which is gathered from fragmentation.
     * @param mmtp MMT Packet which being already decoded
     * @return MFU byte array(not fragmented, it's already collected as whole)
     * @throws InterruptedException occur when interrupted
     * @throws IOException occur when ByteBuffer has problem
     */
    protected List<ByteArrayOutputStream> getMFU(MMTP_Packet mmtp)
            throws InterruptedException, IOException {
        List<MFU> mfus = null;
        MMTP_Payload_MPU mpu = mmtp.getMPU();
        List<ByteArrayOutputStream> samples = new ArrayList<>();

        if ( mmtp.isScrambled() == true || mpu == null ) return samples;

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
     * Gets table(AKA section) of ARIB MMT-SI, TLV-SI from MMTP payload
     * like PA, CA, M2Section, M2ShortSection, DataTransmissionMessage.
     * @param mmtp MMT packet which being already decoded
     * @return Signal Message including variable tables
     * @throws IOException occur when ByteByffer has problem
     * @throws InterruptedException occur when interrupted
     */
    protected Message getSinallingMessage(MMTP_Packet mmtp)
            throws IOException, InterruptedException {
        Message message = null;
        MMTP_Payload_SignalingMessage signal_message = mmtp.getSignalingMessage();

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
                     * ARIB B60 Table 6-2
                     * This involves header part of divided data.
                     */
                    m_fragmented01_mmtp.add(mmtp);
                    break;
                case 0x02:
                    /**
                     * ARIB B60 Table 6-2
                     * This involves a part of divided data which is \n
                     * neither header part nor last part.
                     */
                    m_fragmented02_mmtp.add(mmtp);
                    break;
                case 0x03:
                    boolean found_01_fragmentation_indicator = false;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    /**
                     * ARIB B60 Table 6-2, This involves last part of divided data.
                     * In other words, it's a timing to make whole section table since 0x03 is last packet
                     * after gathering fragmented data of tables.
                     * Case.1 0x01 of fragmentation_indicator will be only one thing.
                     */
                    for ( Iterator<MMTP_Packet> it =
                            m_fragmented01_mmtp.iterator() ;
                            it.hasNext() ; ) {
                        MMTP_Packet mmtp01 = it.next();
                        MMTP_Payload_SignalingMessage sm = mmtp01.getSignalingMessage();
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
                            MMTP_Payload_SignalingMessage sm = mmtp02.getSignalingMessage();
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
