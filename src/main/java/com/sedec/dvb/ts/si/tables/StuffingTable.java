package com.sedec.dvb.ts.si.tables;

import com.sedec.base.Table;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

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

    @Override
    public void print() {
        super.print();

        Logger.d("data_byte : \n");

        BinaryLogger.print(data_byte);
    }
}
