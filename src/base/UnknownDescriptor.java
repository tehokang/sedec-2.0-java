package base;

public class UnknownDescriptor extends Descriptor {

    public UnknownDescriptor(BitReadWriter brw) {
        super(brw);
        
        brw.SkipOnBuffer(descriptor_length*8);
    }

    @Override
    protected void updateDescriptorLength() {
        /**
         * @note Nothing to do
         */
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
    }

}
