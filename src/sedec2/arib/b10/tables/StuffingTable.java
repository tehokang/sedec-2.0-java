package sedec2.arib.b10.tables;

import sedec2.base.Table;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

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
            data_byte[i] = (byte) readOnBuffer(8);
        }
    }

    public byte[] getDataByte() {
        return data_byte;
    }
    
    @Override
    public void print() {
        super.print();
        
        Logger.d("data_byte : \n");
        
        BinaryLogger.print(data_byte);
    }
}
