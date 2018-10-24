package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_InfoDescriptor extends Descriptor {
    private byte[] ISO_639_language_code = new byte[3];
    private int text_char_length;
    private byte[] text_char = new byte[256];
    
    public MH_InfoDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);
            
            for ( int i=0; i<(descriptor_length-3); i++ ) {
                text_char[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public byte[] getLanguageCode() {
        return ISO_639_language_code;
    }
    
    public int getTextCharLength() {
        return text_char_length;
    }
    
    public byte[] getTextChar() {
        return text_char;
    }
    
    public void setLanguageCode(byte[] value) {
        ISO_639_language_code[0] = value[0];
        ISO_639_language_code[1] = value[1];
        ISO_639_language_code[2] = value[2];
    }
    
    public void setTextCharLength(int value) {
        text_char_length = value;
    }
    
    public void setTextChar(byte[] value) {
        System.arraycopy(value, 0, text_char, 0, value.length);
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t ISO_639_language_code : %c%c%c \n", 
                ISO_639_language_code[0], 
                ISO_639_language_code[1], ISO_639_language_code[2]));
        
        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3 + text_char.length;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            brw.writeOnBuffer(ISO_639_language_code[0], 8);
            brw.writeOnBuffer(ISO_639_language_code[1], 8);
            brw.writeOnBuffer(ISO_639_language_code[2], 8);
            
            for ( int i=0; i<(descriptor_length-3); i++ ) {
                brw.writeOnBuffer(text_char[i], 8);
            }
        }
    }

    
}
