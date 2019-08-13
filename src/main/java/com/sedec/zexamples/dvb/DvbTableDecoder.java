package com.sedec.zexamples.dvb;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;

import com.sedec.base.Table;
import com.sedec.dvb.ts.si.TableFactory;
import com.sedec.util.CommandLineParam;

/**
 * DvbTableDecoder is an application as example for getting
 * <ul>
 * <li> Tables which are include in DVB specification
 * </ul>
 * from byte buffer as a whole of table gathered.
 */
public class DvbTableDecoder extends BaseSimpleDecoder {

    @Override
    public void justDoIt(CommandLine commandLine) {
        String target_file = commandLine.getOptionValue(CommandLineParam.TS188_TYPE);
        File inOutFile = new File(target_file);
        DataInputStream dataInputStream = null;
        try {

            dataInputStream =
                    new DataInputStream(
                            new BufferedInputStream(
                                    new FileInputStream(inOutFile)));

            long table_buffer_length = inOutFile.length();
            byte[] table_buffer = new byte[(int) table_buffer_length];

            dataInputStream.readFully(table_buffer);

            Table table = TableFactory.createTable(table_buffer);

            if ( null != table ) {
                table.printBuffer();
                table.print();
            }

            dataInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("ByeBye");
        }
    }
}
