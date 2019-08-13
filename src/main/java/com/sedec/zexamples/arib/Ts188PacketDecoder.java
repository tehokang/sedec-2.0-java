package com.sedec.zexamples.arib;

import org.apache.commons.cli.CommandLine;

import com.sedec.util.CommandLineParam;
import com.sedec.util.ConsoleProgress;
import com.sedec.util.FileTs188PacketReader;
import com.sedec.util.HttpTsPacketReader;
import com.sedec.util.PacketReader;

/**
 * TsPacketDecoder is an application as example for getting
 * <ul>
 * <li> Tables which are include in SI of TS
 * </ul>
 */
public class Ts188PacketDecoder extends BaseSimpleDecoder {
    @Override
    public void justDoIt(CommandLine commandLine) {
        String target_file = commandLine.getOptionValue(CommandLineParam.TS188_TYPE);
        SimpleTsCoordinator simple_ts_coordinator = new SimpleTsCoordinator(commandLine);
        ConsoleProgress progress_bar = new ConsoleProgress("TS188").
                show(true, true, true, true, true, false);
        /**
         * Getting each one TS packet from specific file.
         * It assume that platform should give a TS packet to us as input of TSExtractor
         */
        PacketReader ts_reader = new FileTs188PacketReader(target_file);

        if ( commandLine.hasOption(CommandLineParam.REMOTE_RESOURCES) )
            ts_reader = new HttpTsPacketReader(target_file);

        if ( false == ts_reader.open() ) return;

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
            if ( commandLine.hasOption(CommandLineParam.SHOW_PROGRESS) )
                progress_bar.update(ts_packet.length);
        }

        simple_ts_coordinator.clearQueue();

        progress_bar.stop();
        ts_reader.close();
        ts_reader = null;

        /**
         * Destroy of SimpleTsCoordinator to not handle and released by garbage collector
         */
        simple_ts_coordinator.destroy();
        simple_ts_coordinator = null;
    }
}
