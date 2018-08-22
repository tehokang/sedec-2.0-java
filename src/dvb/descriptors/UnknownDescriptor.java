package dvb.descriptors;

import base.BitReadWriter;

public class UnknownDescriptor extends Descriptor {

    public UnknownDescriptor(BitReadWriter brw) {
        super(brw);
        
        brw.SkipOnBuffer(descriptor_length*8);
    }

    @Override
    protected void updateDescriptorLength() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptor_("Unknown");
    }

}
