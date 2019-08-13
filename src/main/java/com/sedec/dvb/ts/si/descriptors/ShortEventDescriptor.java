package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class ShortEventDescriptor extends Descriptor {
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte event_name_length;
    protected byte[] event_name_char;
    protected byte text_length;
    protected byte[] text_char;

    public ShortEventDescriptor(BitReadWriter brw) {
        super(brw);

        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

        event_name_length = (byte) brw.readOnBuffer(8);
        event_name_char = new byte[event_name_length];

        for ( int i=0; i<event_name_char.length; i++ ) {
            event_name_char[i] = (byte) brw.readOnBuffer(8);
        }

        text_length = (byte) brw.readOnBuffer(8);
        text_char = new byte[text_length];

        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte[] getISO639LanguageCode() {
        return ISO_639_language_code;
    }

    public byte[] getEventNameChar() {
        return event_name_char;
    }

    public byte[] getTextChar() {
        return text_char;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t ISO_639_langauge_code : %s \n",
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t event_name : %s \n", new String(event_name_char)));
        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5 + event_name_char.length + text_char.length;
    }
}
