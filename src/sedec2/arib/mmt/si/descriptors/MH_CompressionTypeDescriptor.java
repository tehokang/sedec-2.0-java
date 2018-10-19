package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_CompressionTypeDescriptor extends Descriptor {
    private int compression_type;
    private int original_size;
    
    public MH_CompressionTypeDescriptor(BitReadWriter brw) {
        super(brw);
        
        compression_type = brw.ReadOnBuffer(8);
        original_size = brw.ReadOnBuffer(32);
    }

    public void SetCompressionType(int value) {
        compression_type = value;
    }
    
    public void SetOriginalSize(int value) {
        original_size = value;
    }
    
    public int GetCompressionType() {
        return compression_type;
    }
    
    public int GetOriginalSize() {
        return original_size;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t compression_type : 0x%x \n",  compression_type));
        Logger.d(String.format("\t original_size : 0x%x (%d) \n", original_size, original_size));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            brw.WriteOnBuffer(compression_type, 8);
            brw.WriteOnBuffer(original_size, 32);
        }
    }

}