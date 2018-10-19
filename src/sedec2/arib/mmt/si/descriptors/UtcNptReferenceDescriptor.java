package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

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
        
        Logger.d(String.format("\t UTC_Reference : 0x%x \n", UTC_Reference));
        Logger.d(String.format("\t NPT_Reference : 0x%x \n", NPT_Reference));
        Logger.d(String.format("\t scale : 0x%x \n", scale));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 17;
    }

}
