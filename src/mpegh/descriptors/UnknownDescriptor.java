package mpegh.descriptors;

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
        super._PrintDescriptor_("Unknown");
        Logger.d("\n");
    }

}
