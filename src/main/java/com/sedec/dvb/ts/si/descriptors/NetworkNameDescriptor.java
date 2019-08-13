package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class NetworkNameDescriptor extends Descriptor {
    protected byte[] characters;

    public NetworkNameDescriptor(BitReadWriter brw) {
        super(brw);

        characters = new byte[descriptor_length];
        for ( int i=0; i<characters.length; i++ ) {
            characters[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte[] getChar() {
        return characters;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t char : %s \n",
                new String(characters)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = characters.length;
    }

}
