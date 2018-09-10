package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

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
        Logger.d("\n");
    }

}