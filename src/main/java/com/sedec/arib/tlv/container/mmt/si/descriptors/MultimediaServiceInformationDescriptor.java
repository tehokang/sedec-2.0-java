package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MultimediaServiceInformationDescriptor extends Descriptor {
    protected int data_component_id;
    protected int component_tag;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte text_length;
    protected byte[] text_char;
    protected byte associated_contents_flag;
    protected byte selector_length;
    protected byte[] selector_byte;

    public MultimediaServiceInformationDescriptor(BitReadWriter brw) {
        super(brw);

        data_component_id = brw.readOnBuffer(16);

        if ( data_component_id == 0x0020 ) {
            component_tag = brw.readOnBuffer(16);
            ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);
            text_length = (byte) brw.readOnBuffer(8);

            text_char = new byte[text_length];
            for ( int i=0; i<text_char.length; i++ ) {
                text_char[i] = (byte) brw.readOnBuffer(8);
            }
        }

        if ( data_component_id == 0x0021 ) {
            associated_contents_flag = (byte) brw.readOnBuffer(1);
            brw.skipOnBuffer(7);
        }

        selector_length = (byte) brw.readOnBuffer(8);
        selector_byte = new byte[selector_length];
        for ( int i=0; i<selector_byte.length; i++ ) {
            selector_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getDataComponentId() {
        return data_component_id;
    }

    public int getComponentTag() {
        return component_tag;
    }

    public byte[] getISO639LanguageCode() {
        return ISO_639_language_code;
    }

    public byte getTextLength() {
        return text_length;
    }

    public byte[] getTextChar() {
        return text_char;
    }

    public byte getAssociatedContentsFlag() {
        return associated_contents_flag;
    }

    public byte getSelectorLength() {
        return selector_length;
    }

    public byte[] getSelectorByte() {
        return selector_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t data_component_id : 0x%x \n", data_component_id));

        if ( data_component_id == 0x0020 ) {
            Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
            Logger.d(String.format("\t ISO_639_language_code : %s \n",
                    new String(ISO_639_language_code)));
            Logger.d(String.format("\t text_length : 0x%x \n", text_length));
            Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
        }

        if ( data_component_id == 0x0021 ) {
            Logger.d(String.format("\t associated_contents_flag : 0x%x \n",
                    associated_contents_flag));
        }

        Logger.d(String.format("\t selector_length : 0x%x \n", selector_length));
        for ( int i=0; i<selector_byte.length; i++ ) {
            Logger.d(String.format("\t [%d] selector_byte : 0x%x \n", i, selector_byte[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;

        if ( data_component_id == 0x0020 ) {
            descriptor_length += 6 + text_char.length;
        }

        if ( data_component_id == 0x0021 ) {
            descriptor_length += 1;
        }

        descriptor_length += (1 + selector_byte.length);
    }
}
