package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class UtcNptReferenceDescriptor extends Descriptor {
    protected long UTC_Reference;
    protected long NPT_Reference;
    protected byte scale;

    public UtcNptReferenceDescriptor(BitReadWriter brw) {
        super(brw);

        UTC_Reference = brw.readLongOnBuffer(64);
        NPT_Reference = brw.readLongOnBuffer(64);
        scale = (byte) brw.readOnBuffer(2);
        brw.skipOnBuffer(6);
    }

    public long getUTCReference() {
        return UTC_Reference;
    }

    public long getNPTReference() {
        return NPT_Reference;
    }

    public byte getScale() {
        return scale;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t UTC_Reference : 0x%x \n", UTC_Reference));
        Logger.d(String.format("\t NPT_Reference : 0x%x \n", NPT_Reference));
        Logger.d(String.format("\t scale : 0x%x \n", scale));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 17;
    }

}
