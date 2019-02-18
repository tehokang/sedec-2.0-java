package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class EmergencyNewsDescriptor extends Descriptor {
    protected long transmit_timestamp;

    public EmergencyNewsDescriptor(BitReadWriter brw) {
        super(brw);

        transmit_timestamp = brw.readLongOnBuffer(64);
        brw.skipOnBuffer(8);
    }

    public long getTransmitTimestamp() {
        return transmit_timestamp;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t transmit_timestamp : 0x%x \n", transmit_timestamp));

    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 9;
    }
}
