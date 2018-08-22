package dvb.descriptors;

import java.util.Arrays;

import base.BitReadWriter;
import util.Logger;

public class SimpleApplicationLocationDescriptor extends Descriptor {
    private byte[] initial_path_bytes = new byte[256];
    
    public SimpleApplicationLocationDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            for ( int i=0; i<descriptor_length; i++ ) {
                initial_path_bytes[i] = (byte) brw.ReadOnBuffer(8);
            }
        }
    }

    public byte[] GetInitialPathBytes() {
        return initial_path_bytes;
    }
    
    public void SetInitialPathBytes(byte[] value) {
        initial_path_bytes = null;
        System.arraycopy(value, 0, initial_path_bytes, 0, value.length);
    }
    
    
    @Override
    protected void updateDescriptorLength() {
        descriptor_length = Arrays.toString(initial_path_bytes).length();
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            for ( int i=0; i<descriptor_length; i++ ) {
                brw.WriteOnBuffer(initial_path_bytes[i], 8);
            }
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptor_("SimpleApplicationLocationDescriptor");
        
        Logger.d("\t initial_path_bytes : " + Arrays.toString(initial_path_bytes) + "\n");
    }

    
}
