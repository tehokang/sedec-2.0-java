package sedec2.arib.tlv.container.mmt.si.tables.dsmcc;

import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.descriptors.NTPEndpointDescriptor;
import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.descriptors.NTPReferenceDescriptor;
import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.descriptors.StreamEventDescriptor;
import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.descriptors.StreamModeDescriptor;
import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.base.UnknownDescriptor;

public class DescriptorFactory {
    public static final int NPT_REFERENCE_DESCRIPTOR = 0x01;
    public static final int NPT_ENDPOINT_DESCRIPTOR = 0x02;
    public static final int STREAM_MODE_DESCRIPTOR = 0x03;
    public static final int STREAM_EVENT_DESCRIPTOR = 0x04;

    public static Descriptor createDescriptor(BitReadWriter brw) {
        int descriptor_tag = brw.getCurrentBuffer()[0] & 0x0000ff;

        switch ( descriptor_tag ) {
        case NPT_REFERENCE_DESCRIPTOR:
            return new NTPReferenceDescriptor(brw);
        case NPT_ENDPOINT_DESCRIPTOR:
            return new NTPEndpointDescriptor(brw);
        case STREAM_MODE_DESCRIPTOR:
            return new StreamModeDescriptor(brw);
        case STREAM_EVENT_DESCRIPTOR:
            return new StreamEventDescriptor(brw);
        default:
            return new UnknownDescriptor(brw);
        }
    }

    private DescriptorFactory() {

    }
}
