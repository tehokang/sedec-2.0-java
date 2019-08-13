package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;

public class UnknownDescriptor extends Descriptor {

    public UnknownDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(descriptor_length*8);
    }

    @Override
    protected void updateDescriptorLength() {
        /**
         * NOTHING TO DO
         */
    }

    @Override
    public void print() {
        super._print_();
    }

}
