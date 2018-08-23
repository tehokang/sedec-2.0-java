package hybridcast;

import base.BitReadWriter;
import hybridcast.descriptors.UnknownDescriptor;
import hybridcast.descriptors.ApplicationBoundaryAndPermissionDescriptor;
import hybridcast.descriptors.ApplicationDescriptor;
import hybridcast.descriptors.ApplicationNameDescriptor;
import hybridcast.descriptors.ApplicationRecordingDescriptor;
import hybridcast.descriptors.ApplicationUsageDescriptor;
import hybridcast.descriptors.AutostartPriorityDescriptor;
import hybridcast.descriptors.CacheControlInfoDescriptor;
import hybridcast.descriptors.ConnectionRequirementDescriptor;
import hybridcast.descriptors.Descriptor;
import hybridcast.descriptors.ParentalRatingDescriptor;
import hybridcast.descriptors.RandomizedLatencyDescriptor;
import hybridcast.descriptors.SimpleApplicationBoundaryDescriptor;
import hybridcast.descriptors.SimpleApplicationLocationDescriptor;
import hybridcast.descriptors.TransportProtocolDescriptor;

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
            case APPLICATION_DESCRIPTOR:
                return new ApplicationDescriptor(brw);
            case APPLICATION_NAME_DESCRIPTOR:
                return new ApplicationNameDescriptor(brw);
            case TRANSPORT_PROTOCOL_DESCRIPTOR:
                return new TransportProtocolDescriptor(brw);
            case APPLICATION_RECORDING_DESCRIPTOR:
                return new ApplicationRecordingDescriptor(brw);
            case SIMPLE_APPLICATION_LOCATION_DESCRIPTOR:
                return new SimpleApplicationLocationDescriptor(brw);
            case APPLICATION_USAGE_DESCRIPTOR:
                return new ApplicationUsageDescriptor(brw);
            case SIMPLE_APPLICATION_BOUNDARY_DESCRIPTOR:
                return new SimpleApplicationBoundaryDescriptor(brw);
            case PARENTAL_RATING_DESCRIPTOR:
                return new ParentalRatingDescriptor(brw);
            case CONNECTION_REQUIREMENT_DESCRIPTOR:
                return new ConnectionRequirementDescriptor(brw);
            case APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR:
                return new ApplicationBoundaryAndPermissionDescriptor(brw);
            case AUTOSTART_PRIORITY_DESCRIPTOR:
                return new AutostartPriorityDescriptor(brw);
            case CACHE_CONTROL_INFO_DESCRIPTOR:
                return new CacheControlInfoDescriptor(brw);
            case RANDOMIZED_LATENCY_DESCRIPTOR:
                return new RandomizedLatencyDescriptor(brw);
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }
    
    private DescriptorFactory() {
        
    }
}
