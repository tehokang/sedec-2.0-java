package sedec2.arib.b24.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * @brief SimpleApplicationBoundaryDescriptor
 * @note Verified
 */
public class SimpleApplicationBoundaryDescriptor extends Descriptor {
    private byte boundary_extension_count;
    private byte[] boundary_extension_length = new byte[256];
    private byte[][] boundary_extension_byte = new byte[256][256];
    
    public SimpleApplicationBoundaryDescriptor(BitReadWriter brw) {
        super(brw);
        
        boundary_extension_count = (byte) brw.ReadOnBuffer(8);
        
        for ( int i=0;i<boundary_extension_count; i++ ) {
            boundary_extension_length[i] = (byte) brw.ReadOnBuffer(8);
            for ( int j=0;j<boundary_extension_length[i]; j++) {
                boundary_extension_byte[i][j] = (byte) brw.ReadOnBuffer(8);
            }
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length=1;
        for ( int i=0;i<boundary_extension_count;i++ ) {
            descriptor_length+=1;
            descriptor_length+=boundary_extension_length[i];
        }
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(boundary_extension_count, 8);
        for(int i=0;i<boundary_extension_count;i++) {
            brw.WriteOnBuffer(boundary_extension_length[i], 8);
            for(int j=0;j<boundary_extension_length[i];j++) {
                brw.WriteOnBuffer(boundary_extension_byte[i][j], 8);
            }
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t boundary_extension_count : 0x%x \n", 
                boundary_extension_count));
     
        for ( int i=0; i<boundary_extension_count; i++) {
            Logger.d(String.format("\t boundary_extension_length[%d] : 0x%x \n",  
                    i, boundary_extension_length[i]));
            Logger.d(String.format("\t boundary_extension_byte[%d] : %s \n",
                    i, new String(boundary_extension_byte[i])));
        }
    }

    
}
