package com.sedec.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineUtility {
    protected static CommandLine commandLine;
    protected static Options options;

    public static void init() {
        options = new Options();
    }

    public static void addOption(String option_short, String option_long,
            boolean has_arg, String description) {
        options.addOption(option_short, option_long, has_arg, description);
    }

    public static boolean done(String []args) {
        try {
            CommandLineParser parser = new DefaultParser();
            commandLine = parser.parse(options,  args);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static CommandLine getCommandLine() {
        return commandLine;
    }

    public static Options getOptions() {
        return options;
    }
}
