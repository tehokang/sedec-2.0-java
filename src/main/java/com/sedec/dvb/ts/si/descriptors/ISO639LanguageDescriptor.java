package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class ISO639LanguageDescriptor extends Descriptor {
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte audio_type;

    public ISO639LanguageDescriptor(BitReadWriter brw) {
        super(brw);

        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);
        audio_type = (byte) brw.readOnBuffer(8);

    }

    public byte[] getISO639LanguageCode() {
        return ISO_639_language_code;
    }

    public byte getAudioType() {
        return audio_type;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t ISO_639_language_code : %s \n",
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t audio_type : 0x%x \n", audio_type));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4;
    }
}
