package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class UtcNptReferenceDescriptor extends Descriptor {
    protected long UTC_Reference;
    protected long NPT_Reference;
    protected byte scale;
    
    public UtcNptReferenceDescriptor(BitReadWriter brw) {
        super(brw);
        
        UTC_Reference = brw.ReadOnBuffer(64);
        NPT_Reference = brw.ReadOnBuffer(64);
        scale = (byte) brw.ReadOnBuffer(2);
        brw.SkipOnBuffer(6);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("UTC_Reference : 0x%x \n", UTC_Reference));
        Logger.d(String.format("NPT_Reference : 0x%x \n", NPT_Reference));
        Logger.d(String.format("scale : 0x%x \n", scale));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 17;
    }

}
