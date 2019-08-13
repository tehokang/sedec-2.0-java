package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class PrivateDataSpecifierDescriptor extends Descriptor {
    protected int private_data_specifier;

    public PrivateDataSpecifierDescriptor(BitReadWriter brw) {
        super(brw);

        private_data_specifier = brw.readOnBuffer(32);
    }

    public int getPrivateDataSpecifier() {
        return private_data_specifier;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t private_dta_specifier : 0x%x \n", private_data_specifier));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4;
    }
}
