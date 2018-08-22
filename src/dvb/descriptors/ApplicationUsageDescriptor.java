package dvb.descriptors;

import base.BitReadWriter;
import util.Logger;

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
        super._PrintDescriptor_("ApplicationUsageDescriptor");
        
        Logger.d("\tusage_type : 0x" + Integer.toHexString(usage_type) + "\n");
    }
}
