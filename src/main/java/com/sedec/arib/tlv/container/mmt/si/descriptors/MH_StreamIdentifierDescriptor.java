package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_StreamIdentifierDescriptor extends Descriptor {
    protected int component_tag;

    public MH_StreamIdentifierDescriptor(BitReadWriter brw) {
        super(brw);

        component_tag = brw.readOnBuffer(16);
    }

    public int getComponentTag() {
        return component_tag;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }
}
