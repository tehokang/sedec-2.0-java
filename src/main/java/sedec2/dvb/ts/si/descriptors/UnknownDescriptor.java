package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;

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
    }

}
