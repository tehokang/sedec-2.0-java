package sedec2.dvb.extractor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import sedec2.base.Table;
import sedec2.dvb.ts.container.packets.TransportStream;
import sedec2.dvb.ts.si.TableFactory;
import sedec2.util.Logger;

/**
 * Class to extract SI as Table from Transport Stream of ISO-13818.
 * {@link Table}
 *
 * It has inherited from BaseExtractor which has already implementations to get Table.
 * {@link BaseExtractor}
 *
 * User can receive Table via
 * {@link SiExtractor.ITableExtractorListener#onReceivedTable(Table)}
 */
public class SiExtractor extends BaseExtractor {
    protected static final String TAG = SiExtractor.class.getSimpleName();

    /**
     * Listener to receive tables as SI of DVB
     */
    public interface ITableExtractorListener extends BaseExtractor.Listener {
        /**
         * Receives table
         * @param table in chapter of DVB
         */
        public void onReceivedTable(Table table);
    }

    public class QueueData extends BaseExtractor.QueueData {
        public Table table;

        public QueueData(Table table) {
            this.table = table;
        }
    }

    /**
     * Constructor which start running thread to emit Event to user.
     */
    public SiExtractor() {
        super();

        m_event_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                QueueData data = null;

                while ( m_is_running ) {
                    try {
                        if ( null != m_event_queue &&
                                ( data = (QueueData) m_event_queue.take()) != null ) {
                            Table table = data.table;
                            if ( m_enable_logging == true ) table.print();
                            for ( int i=0; i<m_listeners.size(); i++ ) {
                                ((ITableExtractorListener)m_listeners.get(i)).
                                        onReceivedTable(table);
                            }
                        }
                    } catch ( ArrayIndexOutOfBoundsException e ) {
                        Logger.e(TAG, "Error while emitting events \n");
                        e.printStackTrace();
                    } catch ( InterruptedException e ) {
                        /**
                         * Nothing to do
                         */
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        }, TAG);
        m_event_thread.start();
    }

    @Override
    public void destroy() {
        super.destroy();

        m_event_thread.interrupt();
        m_event_thread = null;
    }

    @Override
    /**
     * Chapter 2 of ISO-13818
     * Processes to put QueueData with Table having descriptors into event queue,
     *
     * @param one TS packet
     */
    protected synchronized void process(TransportStream ts)
            throws InterruptedException, IOException {
        /**
         * Section already has 0x01 of adaptation field control
         */
        if ( ts.getAdaptationFieldControl() != 0x01 ) return;

        /**
         * Section filter by TS PID
         */
        if ( m_int_id_filter.contains(ts.getPID()) == false ) return;

        ByteArrayOutputStream section_buffer = m_fragmented_transport_stream.get(ts.getPID());
        if ( ts.getPayloadUnitStartIndicator() == 0x01 ) {
            if ( null == section_buffer ) {
                /**
                 * Put begin of section
                 */
                section_buffer = new ByteArrayOutputStream();
                section_buffer.write(ts.getDataByte(), 1, ts.getDataByte().length-1);
                m_fragmented_transport_stream.put(ts.getPID(), section_buffer);
            } else {
                /**
                 * Put previous buffered sections out since new section come here
                 * with being careful whether current package has pointer field which
                 * refers to previous section's data or not.
                 */
                int remaining = 0xff & ts.getPointerField();
                if ( remaining > 0x00 ) {
                    section_buffer.write(ts.getDataByte(), 1, remaining);
                }

                Table table = TableFactory.createTable(section_buffer.toByteArray());
                if ( m_enable_logging ) {
                    Logger.d(String.format("PID : 0x%04x, table_id : 0x%x\n",
                            ts.getPID(), table.getTableId()));
                }
                putOut(new QueueData(table));

                /**
                 * Clear buffer
                 */
                section_buffer.reset();
                m_fragmented_transport_stream.remove(ts.getPID());

                /**
                 * Put new section into buffer
                 */
                section_buffer.write(ts.getDataByte(), 1 + remaining,
                        ts.getDataByte().length-1-remaining);
                m_fragmented_transport_stream.put(ts.getPID(), section_buffer);
            }
        } else {
            /**
             * Put middle of section
             */
            if ( section_buffer != null ) {
                section_buffer.write(ts.getDataByte());
            }
        }
    }
}
