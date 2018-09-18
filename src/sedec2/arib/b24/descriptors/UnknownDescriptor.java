package sedec2.arib.b24.descriptors;

import sedec2.base.BitReadWriter;

public class UnknownDescriptor extends Descriptor {

    public UnknownDescriptor(BitReadWriter brw) {
        super(brw);
        
        brw.SkipOnBuffer(descriptor_length*8);
    }

    @Override
    protected void updateDescriptorLength() {
        /**
         * @note NOTHING TO DO
         */
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
    }

}
