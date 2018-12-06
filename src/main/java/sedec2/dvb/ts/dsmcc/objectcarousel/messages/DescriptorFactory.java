package sedec2.dvb.ts.dsmcc.objectcarousel.messages;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.descriptors.LocationDescriptor;
import sedec2.dvb.ts.si.descriptors.UnknownDescriptor;

public class DescriptorFactory {
    public static final int LOCATION_DESCRIPTOR = 0x06;

    public static Descriptor createDescriptor(BitReadWriter brw) {
        int descriptor_tag = brw.getCurrentBuffer()[0] & 0x0000ff;

        switch ( descriptor_tag ) {
        case LOCATION_DESCRIPTOR:
            return new LocationDescriptor(brw);
        default:
            return new UnknownDescriptor(brw);
        }
    }

    private DescriptorFactory() {

    }
}
