package com.sedec.arib.b10.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class AutostartPriorityDescriptor extends Descriptor {

    private byte autostart_priority;

    public AutostartPriorityDescriptor(BitReadWriter brw) {
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
