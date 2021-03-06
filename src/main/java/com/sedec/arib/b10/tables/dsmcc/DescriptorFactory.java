package com.sedec.arib.b10.tables.dsmcc;

import com.sedec.arib.b10.descriptors.UnknownDescriptor;
import com.sedec.arib.b10.tables.dsmcc.descriptors.NTPEndpointDescriptor;
import com.sedec.arib.b10.tables.dsmcc.descriptors.NTPReferenceDescriptor;
import com.sedec.arib.b10.tables.dsmcc.descriptors.StreamEventDescriptor;
import com.sedec.arib.b10.tables.dsmcc.descriptors.StreamModeDescriptor;
import com.sedec.base.BitReadWriter;
import com.sedec.base.Descriptor;

/**
 * DescriptorFactory to provide descriptors of Table 8-2 descriptorTag field values in
 * 8.Stream Descriptors of ISO 13818-6.
 */
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
