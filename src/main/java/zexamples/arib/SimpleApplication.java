package zexamples.arib;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import sedec2.util.CommandLineParam;
import sedec2.util.CommandLineUtility;

public class SimpleApplication {

    public static void main(String []args) {
        CommandLineUtility.init();
        CommandLineUtility.addOption(CommandLineParam.SHOW_PROGRESS, "show_progress", false,
                "Enable to show progress bar");
        CommandLineUtility.addOption(CommandLineParam.SHOW_TABLES, "show_tables", false,
                "Enable to print tables");
        CommandLineUtility.addOption(CommandLineParam.EXTRACT, "extract", false,
                "Enable to extract elementary stream");
        CommandLineUtility.addOption(CommandLineParam.REMOTE_RESOURCES, "remote", false,
                "Enable getting via remote stream like http");
        CommandLineUtility.addOption(CommandLineParam.SECTION_TYPE,  "input_section_file", true,
                "Target input section");
        CommandLineUtility.addOption(CommandLineParam.TS_TYPE, "input_ts_file", true,
                "Target input file as ts");
        CommandLineUtility.addOption(CommandLineParam.TLV_TYPE, "input_tlv_file", true,
                "Target input file as tlv");
        CommandLineUtility.addOption(CommandLineParam.TSTLV_TYPE, "input_tstlv_file", true,
                "Target input file as ts including tlv");

        CommandLineUtility.done(args);
        CommandLine cli = CommandLineUtility.getCommandLine();

        if ( cli.hasOption(CommandLineParam.SECTION_TYPE) == false &&
                cli.hasOption(CommandLineParam.TS_TYPE) == false &&
                cli.hasOption(CommandLineParam.TLV_TYPE) == false &&
                cli.hasOption(CommandLineParam.TSTLV_TYPE) == false ) {
            HelpFormatter formmater = new HelpFormatter();
            String title = SimpleApplication.class.getSimpleName() + " in sedec2";
            formmater.printHelp(title, CommandLineUtility.getOptions());
        } else {
            BaseSimpleDecoder decoder = null;
            if ( cli.hasOption(CommandLineParam.TS_TYPE) ) {
                decoder = new TsPacketDecoder();
            } else if ( cli.hasOption(CommandLineParam.TLV_TYPE) ) {
                decoder = new TlvPacketDecoder();
            } else if ( cli.hasOption(CommandLineParam.TSTLV_TYPE) ) {
                decoder = new TlvTsPacketDecoder();
            } else if ( cli.hasOption(CommandLineParam.SECTION_TYPE) ) {
                decoder = new AribTableDecoder();
            }

            if ( decoder != null ) decoder.justDoIt(cli);
        }
    }
}
