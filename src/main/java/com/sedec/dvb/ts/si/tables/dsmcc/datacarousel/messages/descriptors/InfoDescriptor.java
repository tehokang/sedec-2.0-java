package com.sedec.dvb.ts.si.tables.dsmcc.datacarousel.messages.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.base.Descriptor;
import com.sedec.util.Logger;

public class InfoDescriptor extends Descriptor {
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte[] text_char;

    public InfoDescriptor(BitReadWriter brw) {
        super(brw);

        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

        text_char = new byte[descriptor_length - 3];
        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte[] getISO639LanguageCode() {
        return ISO_639_language_code;
    }

    public byte[] getTextChar() {
        return text_char;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t ISO_639_language_code : %s \n",
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3 + text_char.length;
    }
}
