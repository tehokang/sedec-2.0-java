package arib.b10;

import base.BitReadWriter;
import arib.b10.descriptors.UnknownDescriptor;
import arib.b10.descriptors.Descriptor;

public class DescriptorFactory {
    public final static int APPLICATION_DESCRIPTOR = 0x00;
    public final static int APPLICATION_NAME_DESCRIPTOR = 0x01;
    public final static int TRANSPORT_PROTOCOL_DESCRIPTOR = 0x02;
    public final static int APPLICATION_RECORDING_DESCRIPTOR = 0x06;
    public final static int SIMPLE_APPLICATION_LOCATION_DESCRIPTOR = 0x15;
    public final static int APPLICATION_USAGE_DESCRIPTOR = 0x16;
    public final static int SIMPLE_APPLICATION_BOUNDARY_DESCRIPTOR = 0x17;
    public final static int APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR = 0x30;
    public final static int AUTOSTART_PRIORITY_DESCRIPTOR = 0X31;
    public final static int CACHE_CONTROL_INFO_DESCRIPTOR = 0x32;
    public final static int RANDOMIZED_LATENCY_DESCRIPTOR = 0x33;
    public final static int PARENTAL_RATING_DESCRIPTOR = 0x55;
    public final static int CONNECTION_REQUIREMENT_DESCRIPTOR = 0x72;
    public final static int UNKNOWN_DESCRIPTOR = 0xff;
    
    public static Descriptor CreateDescriptor(BitReadWriter brw) {
        int descriptor_tag = brw.GetCurrentBuffer()[0] & 0x0000ff;
        
        switch ( descriptor_tag ) {
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }
    
    private DescriptorFactory() {
        
    }
}
