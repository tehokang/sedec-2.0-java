package sedec2.arib.extractor;

import java.io.IOException;
import java.util.List;

import sedec2.arib.tlv.container.PacketFactory;
import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.SignallingPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.arib.tlv.mmt.messages.Message;
import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;
import sedec2.base.Table;

public class SiExtractor extends BaseExtractor {
    protected final String TAG = "SiExtractor";
    
    public interface ITableExtractorListener extends BaseExtractor.Listener {
        public void onReceivedTable(Table table);    
    }
    
    public class QueueData extends BaseExtractor.QueueData {
        public Table table;
        
        public QueueData(Table table) {
            this.table = table;
        }
    }
    
    public SiExtractor() {
        super();
        
        m_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                QueueData data = null;
                
                while ( m_is_running ) {
                    try {
                        Thread.sleep(0, m_sleep_micro_interval);
                        if ( null != m_event_queue && 
                                ( data = (QueueData) m_event_queue.take()) != null ) {
                            Table table = data.table;
                            
                            if ( m_byte_id_filter.contains(table.getTableId()) == false )
                                continue;
                            
                            if ( m_enable_logging == true ) table.print();
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((ITableExtractorListener)m_listeners.get(i)).
                                        onReceivedTable(table);
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
    public void destroy() {
        super.destroy();
        
        m_event_thread.interrupt();
        m_event_thread = null;
    }
    
    protected synchronized void process(TypeLengthValue tlv) 
            throws InterruptedException, IOException {
        switch ( tlv.getPacketType() ) {
            case PacketFactory.SIGNALLING_PACKET:
                putOut( new QueueData(((SignallingPacket)tlv).getTable()));
                break;
            case PacketFactory.COMPRESSED_IP_PACKET:
                CompressedIpPacket cip = (CompressedIpPacket) tlv;
                MMTP_Packet mmtp_packet = cip.getPacketData().mmtp_packet;
                
                if ( mmtp_packet == null ) break;
                
                /**
                 * @note Signaling Message
                 */
                if ( 0x02 == mmtp_packet.getPayloadType() ) {
                    Message message = getSinallingMessage(mmtp_packet);
                    if ( message != null ) {
                        List<Table> tables = message.getTables();
                        for ( int i=0; i<tables.size(); i++ ) {
                            putOut(new QueueData(tables.get(i)));
                        }
                    }
                } 
                break;
        }
    }
}
