package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class MH_InfoDescriptor extends Descriptor {
    private byte[] ISO_639_language_code = new byte[3];
    private int text_char_length;
    private byte[] text_char = new byte[256];
    
    public MH_InfoDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            ISO_639_language_code[0] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code[1] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code[2] = (byte) brw.ReadOnBuffer(8);
            
            for ( int i=0; i<(descriptor_length-3); i++ ) {
                text_char[i] = (byte) brw.ReadOnBuffer(8);
            }
        }
    }

    public byte[] GetLanguageCode() {
        return ISO_639_language_code;
    }
    
    public int GetTextCharLength() {
        return text_char_length;
    }
    
    public byte[] GetTextChar() {
        return text_char;
    }
    
    public void SetLanguageCode(byte[] value) {
        ISO_639_language_code[0] = value[0];
        ISO_639_language_code[1] = value[1];
        ISO_639_language_code[2] = value[2];
    }
    
    public void SetTextCharLength(int value) {
        text_char_length = value;
    }
    
    public void SetTextChar(byte[] value) {
        System.arraycopy(value, 0, text_char, 0, value.length);
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
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
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            brw.WriteOnBuffer(ISO_639_language_code[0], 8);
            brw.WriteOnBuffer(ISO_639_language_code[1], 8);
            brw.WriteOnBuffer(ISO_639_language_code[2], 8);
            
            for ( int i=0; i<(descriptor_length-3); i++ ) {
                brw.WriteOnBuffer(text_char[i], 8);
            }
        }
    }

    
}
