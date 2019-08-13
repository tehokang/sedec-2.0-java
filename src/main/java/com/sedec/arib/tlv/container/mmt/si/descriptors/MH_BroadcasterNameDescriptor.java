package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_BroadcasterNameDescriptor extends Descriptor {
    protected byte[] broadcaster_name;

    public MH_BroadcasterNameDescriptor(BitReadWriter brw) {
        super(brw);

        broadcaster_name = new byte[descriptor_length];
        for ( int i=0; i<descriptor_length; i++ ) {
            broadcaster_name[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public String getBroadcasterName() {
        return new String(broadcaster_name);
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t char : %s \n", new String(broadcaster_name)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = broadcaster_name.length;
    }
}
