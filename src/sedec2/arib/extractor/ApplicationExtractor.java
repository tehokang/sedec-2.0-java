package sedec2.arib.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.mmtp.MMTP_Packet;
import sedec2.arib.tlv.container.mmtp.MMTP_Packet.HeaderExtensionByte;
import sedec2.arib.tlv.container.mmtp.MMTP_Payload_MPU;
import sedec2.arib.tlv.container.mmtp.MMTP_Payload_MPU.MFU;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;

public class ApplicationExtractor extends BaseExtractor {
    public interface IAppExtractorListener extends BaseExtractor.Listener {
        public void onReceivedApplication(int packet_id, int item_id, 
                int mpu_sequence_number, byte[] buffer);
        public void onReceivedIndexItem(int packet_id, int item_id, 
                int mpu_sequence_number, byte[] buffer);
    }
 
    class QueueData extends BaseExtractor.QueueData {
        public int item_id;
        public int mpu_sequence_number;
        public boolean is_index_item = false;
        
        public QueueData(int pid, int item_id, int mpu_sequence_number, 
                boolean is_index_item, byte[] data) {
            this.data = data;
            this.packet_id = pid;
            this.item_id = item_id;
            this.is_index_item = is_index_item;
            this.mpu_sequence_number = mpu_sequence_number;
        }
    }

    protected final String TAG = "ApplicationExtractor";
    
    public ApplicationExtractor() {
        super();
        
        m_event_thread = new Thread(new Runnable() {

            @Override
            public void run() {
                QueueData data = null;
                while ( m_is_running ) {
                    try {
                        Thread.sleep(0, m_sleep_micro_interval);
                        if ( null != m_event_queue && 
                                (data = (QueueData) m_event_queue.take()) != null ) {
                            
                            if ( data.is_index_item == false ) {
                                for ( int i=0; i<m_listeners.size(); i++ ) {
                                    ((IAppExtractorListener)m_listeners.get(i)).
                                            onReceivedApplication(data.packet_id, 
                                                    data.item_id, 
                                                    data.mpu_sequence_number,
                                                    data.data);
                                }
                            } else {
                                for ( int i=0; i<m_listeners.size(); i++ ) {
                                    ((IAppExtractorListener)m_listeners.get(i)).
                                            onReceivedIndexItem(data.packet_id, 
                                                    data.item_id,
                                                    data.mpu_sequence_number,
                                                    data.data);
                                }
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
        m_event_thread.start();
    }
    
    /**
     * User should use this function when they don't use TLVExtractor any more.
     */
    @Override
    public void destroy() {
        super.destroy();
        
        m_event_thread.interrupt();
        m_event_thread = null;
    }
    
    /**
     * Chapter 9 of ARIB B60v1-12
     * process function can send QueueData with MFU_data_byte as event value.
     * User can cast MFU_data_byte to MFU_IndexItem or raw data to be read.
     */
    @Override
    protected synchronized void process(TypeLengthValue tlv) 
            throws InterruptedException, IOException {
        switch ( tlv.getPacketType() ) {
            case PacketFactory.COMPRESSED_IP_PACKET:
                CompressedIpPacket cip = (CompressedIpPacket) tlv;
                MMTP_Packet mmtp_packet = cip.getPacketData().mmtp_packet;
                
                if ( mmtp_packet == null ) break;
                
                /**
                 * @note MPU-MFU
                 */
                if ( 0x00 == mmtp_packet.getPayloadType() ) {
                    if ( m_int_id_filter.contains(mmtp_packet.getPacketId()) ) {
                        List<ByteArrayOutputStream> samples = getMFU(mmtp_packet);
                        if ( samples.size() == 0 ) break;
                        
                        boolean is_index_item = false;
                        List<HeaderExtensionByte> header_ext = 
                                mmtp_packet.getHeaderExtensionByte();
                        for ( int j=0; j<header_ext.size(); j++ ) {
                            HeaderExtensionByte he = header_ext.get(j);
                            if ( he.item_fragment_number == 0x0000 &&
                                    he.last_item_fragment_number == 0x0000 ) {
                                is_index_item = true;
                            }
                        }
                        
                        MMTP_Payload_MPU mpu = mmtp_packet.getMPU();
                        List<MFU> mfus = mpu.getMFUList();
                        for ( int i=0; i<samples.size(); i++ ) {
                            putOut(new QueueData(
                                    mmtp_packet.getPacketId(), 
                                    mfus.get(0).item_id, 
                                    mmtp_packet.getMPU().getMPUSequenceNumber(), 
                                    mfus.get(0).item_id == 0x0 && is_index_item ? true : false, 
                                    samples.get(i).toByteArray() ));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
