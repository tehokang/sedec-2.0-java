package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class StreamIdentifierDescriptor extends Descriptor {
    protected byte component_tag;

    public StreamIdentifierDescriptor(BitReadWriter brw) {
        super(brw);

        component_tag = (byte) brw.readOnBuffer(8);
    }

    public byte getComponentTag() {
        return component_tag;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }

}
