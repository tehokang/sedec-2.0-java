package sedec2.dvb.extractor;

import java.io.IOException;

import sedec2.base.Table;
import sedec2.dvb.ts.container.packets.TransportStream;
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

                            if ( m_byte_id_filter.contains(table.getTableId()) == false )
                                continue;

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
        });
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

    }
}
