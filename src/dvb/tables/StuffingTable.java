package dvb.tables;

import base.Table;
import util.Logger;

public class StuffingTable extends Table {
    protected byte[] data_byte;
    
    public StuffingTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        data_byte = new byte[section_length];
        
        for ( int i=0; i<section_length; i++ ) {
            data_byte[i] = (byte) ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        int j=1;
        Logger.d("data_byte : \n");
        Logger.d(String.format("\t%03d : ", j));
        for(int i=0; i<data_byte.length; i++)
        {
            Logger.d(String.format("%02x ", data_byte[i]));
            if(i%10 == 9) Logger.p(String.format("\n\t%03d : ", (++j)));
        }
    }
}
