package sedec2.base;

public class UnknownDescriptor extends Descriptor {

    public UnknownDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(descriptor_length*8);
    }

    @Override
    protected void updateDescriptorLength() {
        /**
         * Nothing to do
         */
    }

    @Override
    public void print() {
        super._print_();

    }

}
