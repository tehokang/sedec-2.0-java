package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class StuffingDescriptor extends Descriptor {
    protected byte[] stuffing_byte;

    public StuffingDescriptor(BitReadWriter brw) {
        super(brw);

        stuffing_byte = new byte[descriptor_length];
        for ( int i=0; i<stuffing_byte.length; i++ ) {
            stuffing_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte[] getStuffingByte() {
        return stuffing_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t stuffing_byte : \n"));
        BinaryLogger.print(stuffing_byte);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = stuffing_byte.length;
    }
}
