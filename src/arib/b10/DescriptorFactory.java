package arib.b10;

import base.BitReadWriter;
import arib.b10.descriptors.UnknownDescriptor;
import arib.b10.descriptors.VideoDecodeControlDescriptor;
import arib.b10.descriptors.CAServiceDescriptor;
import arib.b10.descriptors.ConditionalAccessDescriptor;
import arib.b10.descriptors.DataComponentDescriptor;
import arib.b10.descriptors.Descriptor;
import arib.b10.descriptors.DigitalCopyControlDescriptor;
import arib.b10.descriptors.StreamIdentifierDescriptor;

public class DescriptorFactory {
    public final static int CONDITIONAL_ACCESS_DESCRIPTOR = 0x09;
    public final static int STREAM_IDENTIFIER_DESCRIPTOR = 0x52;
    public final static int DIGITALCOPY_CONTROL_DESCRIPTOR = 0xc1;
    public final static int CA_SERVICE_DESCRIPTOR = 0xcc;
    public final static int VIDEODECODE_CONTROL_DESCRIPTOR = 0xc8;
    public final static int DATA_COMPONENT_DESCRIPTOR = 0xfd;
    public final static int UNKNOWN_DESCRIPTOR = 0xff;
    
    public static Descriptor CreateDescriptor(BitReadWriter brw) {
        int descriptor_tag = brw.GetCurrentBuffer()[0] & 0x0000ff;
        
        switch ( descriptor_tag ) {
            case CA_SERVICE_DESCRIPTOR:
                return new CAServiceDescriptor(brw);
            case VIDEODECODE_CONTROL_DESCRIPTOR:
                return new VideoDecodeControlDescriptor(brw);
            case DIGITALCOPY_CONTROL_DESCRIPTOR:
                return new DigitalCopyControlDescriptor(brw);
            case CONDITIONAL_ACCESS_DESCRIPTOR:
                return new ConditionalAccessDescriptor(brw);
            case STREAM_IDENTIFIER_DESCRIPTOR:
                return new StreamIdentifierDescriptor(brw);
            case DATA_COMPONENT_DESCRIPTOR:
                return new DataComponentDescriptor(brw);
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }
    
    private DescriptorFactory() {
        
    }
}
