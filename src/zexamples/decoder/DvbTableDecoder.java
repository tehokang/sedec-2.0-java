package zexamples.decoder;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import base.Table;
import dvb.TableFactory;

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
               
                dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(inOutFile)));
                
                long table_buffer_length = inOutFile.length();
                byte[] table_buffer = new byte[(int) table_buffer_length];
    
                dataInputStream.readFully(table_buffer);
    
                Table table = TableFactory.CreateTable(table_buffer);
                table.PrintRawTable();
                table.PrintTable();
                
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
