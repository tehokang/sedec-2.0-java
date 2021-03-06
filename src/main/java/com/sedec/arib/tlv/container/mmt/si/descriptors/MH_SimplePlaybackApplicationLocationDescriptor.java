package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_SimplePlaybackApplicationLocationDescriptor extends Descriptor {
    protected byte[] initial_path_byte;

    public MH_SimplePlaybackApplicationLocationDescriptor(BitReadWriter brw) {
        super(brw);

        initial_path_byte = new byte[descriptor_length];

        for ( int i=0; i<descriptor_length; i++ ) {
            initial_path_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte[] getInitialPathByte() {
        return initial_path_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t initial_path : %s \n", new String(initial_path_byte)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = initial_path_byte.length;
    }
}
