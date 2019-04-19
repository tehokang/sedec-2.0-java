package zexamples.dvb;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import sedec2.util.CommandLineUtility;
import zexamples.BaseSimpleDecoder;

public class SimpleApplication {

    public static void main(String []args) {
        CommandLineUtility.init();
        CommandLineUtility.addOption("sp", "show_progress", false,
                "Enable to show progress bar");
        CommandLineUtility.addOption("st", "show_tables", false,
                "Enable to print tables");
        CommandLineUtility.addOption("e", "extract", false,
                "Enable to extract elementary stream");
        CommandLineUtility.addOption("r", "remote", false,
                "Enable getting via remote stream like http");
        CommandLineUtility.addOption("s",  "input_section_file", true,
                "Target input section");
        CommandLineUtility.addOption("ts", "input_ts_file", true,
                "Target input file as ts");

        CommandLineUtility.done(args);
        CommandLine cli = CommandLineUtility.getCommandLine();

        if ( cli.hasOption("s") == false &&
                cli.hasOption("ts") == false &&
                cli.hasOption("tlv") == false &&
                cli.hasOption("tstlv") == false ) {
            HelpFormatter formmater = new HelpFormatter();
            String title = SimpleApplication.class.getSimpleName() + " in sedec2";
            formmater.printHelp(title, CommandLineUtility.getOptions());
        } else {
            BaseSimpleDecoder decoder = null;
            if ( cli.hasOption("ts") ) {
                decoder = new TsPacketDecoder();
            } else if ( cli.hasOption("s") ) {
                decoder = new DvbTableDecoder();
            }

            if ( decoder != null ) decoder.justDoIt(cli);
        }
    }
}
