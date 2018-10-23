package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MH_ShortEventDescriptor extends Descriptor {
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte event_name_length;
    protected byte[] event_name_char;
    protected int text_length;
    protected byte[] text_char;
    
    public MH_ShortEventDescriptor(BitReadWriter brw) {
        super();
        
        descriptor_tag = brw.ReadOnBuffer(16);
        descriptor_length = brw.ReadOnBuffer(16);
        
        ISO_639_language_code[0] = (byte) brw.ReadOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.ReadOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.ReadOnBuffer(8);
        
        event_name_length = (byte) brw.ReadOnBuffer(8);
        event_name_char = new byte[event_name_length];
        
        for ( int i=0; i<event_name_char.length; i++ ) {
            event_name_char[i] = (byte) brw.ReadOnBuffer(8);
        }
        
        text_length = brw.ReadOnBuffer(16);
        text_char = new byte[text_length];
        
        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t ISO_639_language_code : %s \n", new String(ISO_639_language_code)));
        Logger.d(String.format("\t event_name_length : 0x%x \n", event_name_length));
        Logger.d(String.format("\t event_name_char : %s \n", new String(event_name_char)));
        Logger.d(String.format("\t text_length : 0x%x \n", text_length));
        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    public int GetDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 4;
    }
    
    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + event_name_char.length + 2 + text_char.length;
    }
}
