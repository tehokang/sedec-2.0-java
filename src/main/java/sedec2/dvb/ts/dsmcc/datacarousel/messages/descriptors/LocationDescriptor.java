package sedec2.dvb.ts.dsmcc.datacarousel.messages.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

public class LocationDescriptor extends Descriptor {
    protected byte location_tag;

    public LocationDescriptor(BitReadWriter brw) {
        super(brw);

        location_tag = (byte) brw.readOnBuffer(8);
    }

    public byte getLocationTag() {
        return location_tag;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t location_tag : 0x%x \n", location_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }
}
