package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class ComponentDescriptor extends Descriptor {
    protected byte stream_content;
    protected byte component_type;
    protected byte component_tag;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte[] text_char;

    public ComponentDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(4);
        stream_content = (byte) brw.readOnBuffer(4);
        component_type = (byte) brw.readOnBuffer(8);
        component_tag = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

        text_char = new byte[descriptor_length - 6];
        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte getStreamContent() {
        return stream_content;
    }

    public byte getComponentType() {
        return component_type;
    }

    public byte getComponentTag() {
        return component_tag;
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

        Logger.d(String.format("\t stream_content : 0x%d \n", stream_content));
        Logger.d(String.format("\t component_type : 0x%d \n", component_type));
        Logger.d(String.format("\t component_tag : 0x%d \n", component_tag));
        Logger.d(String.format("\t ISO_639_language_code : %s \n",
                new String(ISO_639_language_code)));

        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 6 + text_char.length;
    }
}
