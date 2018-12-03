package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ApplicationUsageDescriptor extends Descriptor {
    private byte usage_type;

    public ApplicationUsageDescriptor(BitReadWriter brw) {
        super(brw);

        usage_type = (byte) brw.readOnBuffer(8);
    }

    public byte getUsageType() {
        return usage_type;
    }

    public void setUsageType(byte value) {
        usage_type = value;
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        brw.writeOnBuffer(usage_type, 8);
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t usage_type : 0x%x \n", usage_type));
    }
}
