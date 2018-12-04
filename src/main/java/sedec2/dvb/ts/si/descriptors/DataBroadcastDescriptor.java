package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DataBroadcastDescriptor extends Descriptor {
    protected int data_broadcast_id;
    protected byte component_tag;
    protected byte selector_length;
    protected byte[] selector_byte;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte text_length;
    protected byte[] text_char;

    public DataBroadcastDescriptor(BitReadWriter brw) {
        super(brw);

        data_broadcast_id = brw.readOnBuffer(16);
        component_tag = (byte) brw.readOnBuffer(8);
        selector_length = (byte) brw.readOnBuffer(8);
        selector_byte = new byte[selector_length];

        for ( int i=0; i<selector_byte.length; i++ ) {
            selector_byte[i] = (byte) brw.readOnBuffer(8);
        }

        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

        text_length = (byte) brw.readOnBuffer(8);
        text_char = new byte[text_length];
        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getDataBroadcastId() {
        return data_broadcast_id;
    }

    public byte getComponentTag() {
        return component_tag;
    }

    public byte[] getSelectorByte() {
        return selector_byte;
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

        Logger.d(String.format("\t data_broadcast_id : 0x%x \n", data_broadcast_id));
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
        Logger.d(String.format("\t selector_length : 0x%x \n", selector_length));
        Logger.d(String.format("\t selector_byte : n"));
        BinaryLogger.print(selector_byte);
        Logger.d(String.format("\t ISO_639_language_code : %s \n",
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t text_length : 0x%x \n", text_length));
        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3 + selector_byte.length + 4 + text_char.length;
    }
}
