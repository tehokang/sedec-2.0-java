package sedec2.arib.b10.tables.dsmcc.datacarousel.messages.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

public class GroupLinkDescriptor extends Descriptor {
    protected byte position;
    protected int group_id;

    public GroupLinkDescriptor(BitReadWriter brw) {
        super(brw);

        position = (byte) brw.readOnBuffer(8);
        group_id = brw.readOnBuffer(32);
    }

    public byte getPosition() {
        return position;
    }

    public int getGroupId() {
        return group_id;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t position : 0x%x \n", position));
        Logger.d(String.format("\t group_id : 0x%x \n", group_id));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5;
    }
}
