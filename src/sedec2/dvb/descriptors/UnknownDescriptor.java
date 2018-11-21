package sedec2.dvb.descriptors;

import sedec2.base.BitReadWriter;

public class UnknownDescriptor extends Descriptor {

    public UnknownDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(descriptor_length*8);
    }

    @Override
    protected void updateDescriptorLength() {
        /**
         * @note NOTHING TO DO
         */
    }

    @Override
    public void print() {
        super._print_();
    }

}
