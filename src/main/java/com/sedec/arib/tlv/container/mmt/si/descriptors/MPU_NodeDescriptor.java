package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MPU_NodeDescriptor extends Descriptor {
    protected int node_tag;

    public MPU_NodeDescriptor(BitReadWriter brw) {
        super(brw);

        node_tag = brw.readOnBuffer(16);
    }

    public int getNodeTag() {
        return node_tag;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t node_tag : 0x%x \n", node_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }
}
