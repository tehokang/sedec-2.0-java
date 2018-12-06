package sedec2.dvb.ts.dsmcc.datacarousel.messages;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.descriptors.LocationDescriptor;
import sedec2.dvb.ts.si.descriptors.UnknownDescriptor;

public class DescriptorFactory {
    public static final int TYPE_DESCRIPTOR = 0x01;
    public static final int NAME_DESCRIPTOR = 0x02;
    public static final int INFO_DESCRIPTOR = 0x03;
    public static final int MODULE_LINK_DESCRIPTOR = 0x04;
    public static final int CRC32_DESCRIPTOR = 0x05;
    public static final int LOCATION_DESCRIPTOR = 0x06;
    public static final int EST_DOWNLOAD_TIME_DESCRIPTOR = 0x07;
    public static final int GROUP_LINK_DESCRIPTOR = 0x08;
    public static final int COMPRESSED_MODULE_DESCRIPTOR = 0x09;
    public static final int SSU_MODULE_TYPE_DESCRIPTOR = 0x0a;
    public static final int SUB_GROUP_ASSOCIATION_DESCRIPTOR = 0x0b;

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
