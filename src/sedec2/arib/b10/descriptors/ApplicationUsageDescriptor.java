package sedec2.arib.b10.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * @brief ApplicationUsageDescriptor
 * @note Verified
 */
public class ApplicationUsageDescriptor extends Descriptor {
    private byte usage_type;
    
    public ApplicationUsageDescriptor(BitReadWriter brw) {
        super(brw);
        
        usage_type = (byte) brw.ReadOnBuffer(8);
    }

    public byte GetUsageType() {
        return usage_type;
    }
    
    public void SetUsageType(byte value) {
        usage_type = value;
    }
    
    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(usage_type, 8);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t usage_type : 0x%x \n", usage_type));
    }
}