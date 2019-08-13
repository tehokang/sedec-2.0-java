package com.sedec.arib.b10.tables.dsmcc.objectcarousel.biop;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class ServiceContext {
    protected int context_id;
    protected int context_data_length;
    protected byte[] context_data_byte;

    public ServiceContext(BitReadWriter brw) {
        context_id = brw.readOnBuffer(32);
        context_data_length = brw.readOnBuffer(16);
        context_data_byte = new byte[context_data_length];
        for ( int k=0; k<context_data_byte.length; k++ ) {
            context_data_byte[k] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getLength() {
        return 6 + context_data_byte.length;
    }

    public void print() {
        Logger.d(String.format("\t context_id : 0x%x \n", context_id));
        Logger.d(String.format("\t context_data_length : 0x%x \n", context_data_length));
        Logger.d(String.format("\t context_data_byte : \n"));
        BinaryLogger.print(context_data_byte);
    }
}
