package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_AutostartPriorityDescriptor extends Descriptor {
    private byte autostart_priority;

    public MH_AutostartPriorityDescriptor(BitReadWriter brw) {
        super(brw);

        autostart_priority = (byte) brw.readOnBuffer(8);
    }

    public byte getAutostartPriority() {
        return autostart_priority;
    }

    public void setAutostartPriority(byte value) {
        autostart_priority = value;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t autostart_priority : 0x%x \n", autostart_priority));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        brw.writeOnBuffer(autostart_priority, 8);
    }
}
