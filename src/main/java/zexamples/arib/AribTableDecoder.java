package zexamples.arib;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sedec2.base.Table;

/**
 * AribTableDecoder is an application as example for getting
 * <ul>
 * <li> Tables which are include in TLV-SI of TLV, MMT-SI of MMTP packet
 * </ul>
 * from byte buffer as a whole of table gathered.
 */
public class AribTableDecoder {
    protected static final String TAG = AribTableDecoder.class.getSimpleName();

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

                Table table = sedec2.arib.b10.TableFactory.createTable(table_buffer);
                if ( null == table ) {
                    table = sedec2.arib.tlv.si.TableFactory.createTable(table_buffer);
                    if ( null == table ) {
                        table = sedec2.arib.tlv.container.mmt.si.TableFactory.createTable(table_buffer);
                    }
                }

                System.out.println(
                        String.format("[%d] table information \n",  i));
                table.printBuffer();
                table.print();
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
