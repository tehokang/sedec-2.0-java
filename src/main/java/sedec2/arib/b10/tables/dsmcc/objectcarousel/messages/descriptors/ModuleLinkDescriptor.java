package sedec2.arib.b10.tables.dsmcc.objectcarousel.messages.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

public class ModuleLinkDescriptor extends Descriptor {
    protected byte position;
    protected int module_id;

    public ModuleLinkDescriptor(BitReadWriter brw) {
        super(brw);

        position = (byte) brw.readOnBuffer(8);
        module_id = brw.readOnBuffer(16);
    }

    public byte getPosition() {
        return position;
    }

    public int getModuleId() {
        return module_id;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t position : 0x%x \n", position));
        Logger.d(String.format("\t module_id : 0x%x \n", module_id));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3;
    }
}
