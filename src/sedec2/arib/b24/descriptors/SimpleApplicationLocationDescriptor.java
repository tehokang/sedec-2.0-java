package sedec2.arib.b24.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * @brief SimpleApplicationLocationDescriptor
 * @note Verified
 */
public class SimpleApplicationLocationDescriptor extends Descriptor {
    private byte[] initial_path_bytes;
    
    public SimpleApplicationLocationDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            initial_path_bytes = new byte[descriptor_length];
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
        descriptor_length = initial_path_bytes.length;
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
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t initial_path_bytes : %s \n", new String(initial_path_bytes)));
    }

    
}
