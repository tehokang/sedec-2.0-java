package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class UnknownDescriptor extends Descriptor {

    public UnknownDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(descriptor_length*8);
    }

    @Override
    protected void updateDescriptorLength() {
        /**
         * NOTHING TO DO
         */
    }

    @Override
    public void print() {
        super._print_();
        Logger.d("\n");
    }

}
