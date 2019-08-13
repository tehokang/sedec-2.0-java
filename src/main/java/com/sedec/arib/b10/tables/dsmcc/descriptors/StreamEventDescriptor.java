package com.sedec.arib.b10.tables.dsmcc.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.base.Descriptor;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

/**
 * Descriptor to describe Table 8-7 Stream Event Descriptor of ISO 13818-6
 */
public class StreamEventDescriptor extends Descriptor {
    protected int eventId;
    protected int eventNPT;
    protected byte[] privateDataByte;

    public StreamEventDescriptor(BitReadWriter brw) {
        super(brw);

        eventId = brw.readOnBuffer(16);
        brw.skipOnBuffer(31);
        eventNPT = brw.readOnBuffer(33);

        privateDataByte = new byte[descriptor_length - 10];
        for ( int i=0; i<privateDataByte.length; i++ ) {
            privateDataByte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t eventId : 0x%x \n", eventId));
        Logger.d(String.format("\t eventNPT : 0x%x \n", eventNPT));
        Logger.d(String.format("\t privateDataByte.length : 0x%x \n", privateDataByte.length));
        BinaryLogger.print(privateDataByte);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 10 + privateDataByte.length;
    }
}
