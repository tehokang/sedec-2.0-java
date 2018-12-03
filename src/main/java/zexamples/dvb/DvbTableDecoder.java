package zexamples.dvb;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sedec2.base.Table;
import sedec2.dvb.ts.si.TableFactory;

/**
 * DvbTableDecoder is an application as example for getting
 * <ul>
 * <li> Tables which are include in DVB specification
 * </ul>
 * from byte buffer as a whole of table gathered.
 */
public class DvbTableDecoder {
    public static void main(String []args) {
        if ( args.length < 1 ) {
            System.out.println("Oops, " +
                    "I need mpeg2 table to be parsed as 1st parameter");
            System.out.println(
                    "Usage: java -classpath . " +
                    "zexamples.decoder.DvbDecoder " +
                    "{Table Raw File} \n");
        }

        for ( int i=0; i<args.length; i++ ) {
            File inOutFile = new File(args[i]);
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
                    System.out.println(
                            String.format("[%d] table information \n",  i));

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
}
