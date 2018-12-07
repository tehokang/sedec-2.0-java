package zexamples.dvb;

import java.util.List;

import sedec2.base.Table;
import sedec2.dvb.extractor.TsDemultiplexer;
import sedec2.dvb.ts.dsmcc.DSMCCSection;
import sedec2.dvb.ts.si.TableFactory;
import sedec2.dvb.ts.si.tables.NetworkInformationTable;
import sedec2.dvb.ts.si.tables.ProgramAssociationTable;
import sedec2.dvb.ts.si.tables.ProgramAssociationTable.Program;
import sedec2.dvb.ts.si.tables.ProgramMapTable;
import sedec2.util.ConsoleProgress;
import sedec2.util.FilePacketReader;
import sedec2.util.TsPacketReader;

/**
 * SimpleTsCoordinator is an example which's using TsDemultiplexer of sedec2 to get information
 * of TS as asynchronous mechanism, this is a mechanism for better performance, better visibility.
 */
class SimpleTsCoordinator implements TsDemultiplexer.Listener {
    protected static final String TAG = SimpleTsCoordinator.class.getSimpleName();
    protected TsDemultiplexer ts_demuxer = null;
    protected ProgramAssociationTable pat = null;
    protected ProgramMapTable pmt = null;
    protected NetworkInformationTable nit = null;

    public SimpleTsCoordinator() {
        ts_demuxer = new TsDemultiplexer();
        ts_demuxer.addEventListener(this);

        ts_demuxer.enableSiFilter();
        ts_demuxer.addFilter(0x0000); // PAT

        // ait_multisection_teststream_ssyoo.ts
//        ts_demuxer.addFilter(2413); // only DDB
//        ts_demuxer.addFilter(2419); // only DDB
        ts_demuxer.addFilter(2420); // only DDB
//        ts_demuxer.addFilter(2421); // only DSI
//        ts_demuxer.addFilter(2441);
//        ts_demuxer.addFilter(2822);

//      ts_demuxer.enableSiLogging();
    }

    public void destroy() {
        ts_demuxer.removeEventListener(this);
        ts_demuxer.destroy();
        ts_demuxer = null;
    }

    public void clearQueue() {
        ts_demuxer.clearQueue();
    }

    public boolean put(byte[] ts_raw) {
        return ts_demuxer.put(ts_raw);
    }

    @Override
    public void onReceivedTable(Table table) {
        switch ( table.getTableId() ) {
            case TableFactory.PROGRAM_ASSOCIATION_TABLE:
                if ( pat == null ) {
                    pat = (ProgramAssociationTable)table;
                    List<Program> programs = pat.getPrograms();
                    for ( int i=0; i<programs.size(); i++ ) {
                        Program program = programs.get(i);
                        ts_demuxer.addFilter(program.getPid());
                    }
//                    pat.print();
                }
                break;
            case TableFactory.DSMCC_DOWNLOAD_DATA_MESSAGE_TABLE:
                DSMCCSection dsmcc_ddb = (DSMCCSection) table;
                /**
                 * data_broadcast_id of DataBroadcastDescriptor should 0x0006 for data carousel
                 * data_broadcast_id of DataBroadcastDescriptor should 0x0007 for object carousel
                 */
                dsmcc_ddb.updateToDataCarousel();
                dsmcc_ddb.print();
                break;
            case TableFactory.DSMCC_UN_MESSAGE_TABLE:
                DSMCCSection dsmcc_dsi_dii = (DSMCCSection) table;
                /**
                 * data_broadcast_id of DataBroadcastDescriptor should 0x0006 for data carousel
                 * data_broadcast_id of DataBroadcastDescriptor should 0x0007 for object carousel
                 */
                dsmcc_dsi_dii.updateToDataCarousel();
                dsmcc_dsi_dii.print();
                break;
            default:
//                table.print();
                break;
        }
    }
}

/**
 * TsPacketDecoder is an application as example for getting
 * <ul>
 * <li> Tables which are include in SI of TS
 * </ul>
 */
public class TsPacketDecoder {
    public static void main(String []args) throws InterruptedException {
        if ( args.length < 1 ) {
            System.out.println("Oops, " +
                    "You need TS packet(or file) to be parsed as 1st parameter");
            System.out.println(
                    "Usage: java -classpath . " +
                    "zexamples.dvb.TsPacketDecoder " +
                    "{TS Raw File} \n");
        }

        SimpleTsCoordinator simple_ts_coordinator = new SimpleTsCoordinator();
        ConsoleProgress progress_bar = new ConsoleProgress("TS").
                show(true, true, true, true, true, false, false);
        /**
         * Getting each one TS packet from specific file.
         * It assume that platform should give a TS packet to us as input of TSExtractor
         */
        for ( int i=0; i<args.length; i++ ) {
            FilePacketReader ts_reader = new TsPacketReader(args[i]);
            if ( false == ts_reader.open() ) continue;

            progress_bar.start(ts_reader.filesize());

            while ( ts_reader.readable() > 0) {
                final byte[] ts_packet = ts_reader.readPacket();
                if ( ts_packet == null ||
                        ts_packet.length == 0 ||
                        ts_packet[0] != 0x47 ) continue;
                /**
                 * Putting a TS packet into SimpleTsCoordinator
                 * and you can get both the results of as table of MPEG2
                 * from event listener which you registered to TsDemultiplexer
                 */
                if ( false == simple_ts_coordinator.put(ts_packet) ) break;
//                progress_bar.update(ts_packet.length);
            }

            simple_ts_coordinator.clearQueue();

            progress_bar.stop();
            ts_reader.close();
            ts_reader = null;
        }

        /**
         * Destroy of SimpleTsCoordinator to not handle and released by garbage collector
         */
        simple_ts_coordinator.destroy();
        simple_ts_coordinator = null;

        System.out.println("ByeBye");
        System.exit(0);
    }
}
