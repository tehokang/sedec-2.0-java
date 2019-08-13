package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_TypeDescriptor extends Descriptor {
    private byte[] text_char = new byte[256];

    public MH_TypeDescriptor(BitReadWriter brw) {
        super(brw);

        if ( 0 < descriptor_length ) {
            for ( int i=0; i<descriptor_length; i++ ) {
                text_char[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public byte[] getTextChar() {
        return text_char;
    }

    public void setTextChar(byte[] value) {
        text_char = value;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = text_char.length;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        if ( 0 < descriptor_length ) {
            for ( int i=0;i<descriptor_length; i++ ) {
                brw.writeOnBuffer(text_char[i], 8);
            }
        }
    }
}
