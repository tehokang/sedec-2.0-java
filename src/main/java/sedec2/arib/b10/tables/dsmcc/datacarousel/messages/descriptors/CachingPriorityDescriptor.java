package sedec2.arib.b10.tables.dsmcc.datacarousel.messages.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

public class CachingPriorityDescriptor extends Descriptor {
    protected byte priority_value;
    protected byte transparency_level;

    public CachingPriorityDescriptor(BitReadWriter brw) {
        super(brw);

        priority_value = (byte) brw.readOnBuffer(8);
        transparency_level = (byte) brw.readOnBuffer(8);
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t priority_value : 0x%x \n", priority_value));
        Logger.d(String.format("\t transparency_level : 0x%x \n", transparency_level));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }
}
