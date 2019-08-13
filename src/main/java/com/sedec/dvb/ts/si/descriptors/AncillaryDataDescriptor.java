package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class AncillaryDataDescriptor extends Descriptor {
    protected byte ancillary_data_identifier;

    public AncillaryDataDescriptor(BitReadWriter brw) {
        super(brw);

        ancillary_data_identifier = (byte) brw.readOnBuffer(8);
    }

    public byte getAncillaryDataIdentifier() {
        return ancillary_data_identifier;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t ancillary_data_identifier : 0x%x \n",
                ancillary_data_identifier));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }
}
