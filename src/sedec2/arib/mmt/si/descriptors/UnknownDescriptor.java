package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

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
