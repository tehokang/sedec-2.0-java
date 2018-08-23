package mpegh.descriptors;

import base.BitReadWriter;
import util.Logger;

public class TypeDescriptor extends Descriptor {
    private byte[] text_char = new byte[256];
    
    public TypeDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            for ( int i=0; i<descriptor_length; i++ ) {
                text_char[i] = (byte) brw.ReadOnBuffer(8);
            }
        }
    }

    public byte[] GetTextChar() {
        return text_char;
    }
    
    public void SetTextChar(byte[] value) {
        text_char = value;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptor_("TypeDescriptor");
        
        Logger.d(String.format("\ttext_char : %s \n", new String(text_char)));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = text_char.length;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            for ( int i=0;i<descriptor_length; i++ ) {
                brw.WriteOnBuffer(text_char[i], 8);
            }
        }
    }
}
