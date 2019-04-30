package zexamples.arib;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;

import sedec2.base.Table;
import sedec2.util.CommandLineParam;

/**
 * AribTableDecoder is an application as example for getting
 * <ul>
 * <li> Tables which are include in TLV-SI of TLV, MMT-SI of MMTP packet
 * </ul>
 * from byte buffer as a whole of table gathered.
 */
public class AribTableDecoder extends BaseSimpleDecoder {
    protected static final String TAG = AribTableDecoder.class.getSimpleName();

    @Override
    public void justDoIt(CommandLine commandLine) {
        String target_file = commandLine.getOptionValue(CommandLineParam.SECTION_TYPE);
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

            Table table = sedec2.arib.b10.TableFactory.createTable(table_buffer);
            if ( null == table ) {
                table = sedec2.arib.tlv.si.TableFactory.createTable(table_buffer);
                if ( null == table ) {
                    table = sedec2.arib.tlv.container.mmt.si.TableFactory.createTable(table_buffer);
                }
            }

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
