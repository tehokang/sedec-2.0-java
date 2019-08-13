package com.sedec.arib.b10.tables.dsmcc.objectcarousel.messages.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.base.Descriptor;
import com.sedec.util.Logger;

public class LocationDescriptor extends Descriptor {
    protected byte location_tag;

    public LocationDescriptor(BitReadWriter brw) {
        super(brw);

        location_tag = (byte) brw.readOnBuffer(8);
    }

    public byte getLocationTag() {
        return location_tag;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t location_tag : 0x%x \n", location_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }
}
