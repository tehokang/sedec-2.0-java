package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;

public abstract class Descriptor extends com.sedec.base.Descriptor {

    public Descriptor() {
        /**
         * NOTHING TO DO, THIS IS ONLY FOR LEGACY DESCRIPTOR
         * WHICH HAS DESCRIPTOR_TAG 8 bits and DESCRIPTOR_LENGTH 8 bits
         * */
    }

    public Descriptor(BitReadWriter brw) {
        descriptor_tag = brw.readOnBuffer(16);
        descriptor_length = brw.readOnBuffer(8);
    }

    @Override
    public int getDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 3;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        brw.writeOnBuffer(descriptor_tag, 16);
        brw.writeOnBuffer(descriptor_length, 8);
    }
}
