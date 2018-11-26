package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ShortEventDescriptor extends Descriptor {
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte event_name_length;
    protected byte[] event_name_char;
    protected int text_length;
    protected byte[] text_char;

    public MH_ShortEventDescriptor(BitReadWriter brw) {
        super();

        descriptor_tag = brw.readOnBuffer(16);
        descriptor_length = brw.readOnBuffer(16);

        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

        event_name_length = (byte) brw.readOnBuffer(8);
        event_name_char = new byte[event_name_length];

        for ( int i=0; i<event_name_char.length; i++ ) {
            event_name_char[i] = (byte) brw.readOnBuffer(8);
        }

        text_length = brw.readOnBuffer(16);
        text_char = new byte[text_length];

        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte[] getISO639LanguageCode() {
        return ISO_639_language_code ;
    }

    public byte getEventNameLength() {
        return event_name_length;
    }

    public byte[] getEventNameChar() {
        return event_name_char;
    }

    public int getTextLength() {
        return text_length;
    }

    public byte[] getTextChar() {
        return text_char;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t ISO_639_language_code : %s \n",
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t event_name_length : 0x%x (%d) \n",
                event_name_length, event_name_length));
        Logger.d(String.format("\t event_name_char : %s \n", new String(event_name_char)));
        Logger.d(String.format("\t text_length : 0x%x (%d) \n", text_length, text_length));
        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    public int getDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 4;
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + event_name_char.length + 2 + text_char.length;
    }
}
