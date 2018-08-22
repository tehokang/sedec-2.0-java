package zexamples.decoder;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import base.Table;
import dvb.TableFactory;

public class DvbDecoder {
    public static void main(String []args) {
        for ( int i=0; i<args.length; i++ ) {
            System.out.println("arg["+i+"] : " + args[i]);
        }
        
        File inOutFile = new File(args[0]);
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
