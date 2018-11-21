package sedec2.arib.b10;

import sedec2.arib.b10.descriptors.ApplicationBoundaryAndPermissionDescriptor;
import sedec2.arib.b10.descriptors.ApplicationDescriptor;
import sedec2.arib.b10.descriptors.ApplicationNameDescriptor;
import sedec2.arib.b10.descriptors.ApplicationRecordingDescriptor;
import sedec2.arib.b10.descriptors.ApplicationUsageDescriptor;
import sedec2.arib.b10.descriptors.AutostartPriorityDescriptor;
import sedec2.arib.b10.descriptors.CAServiceDescriptor;
import sedec2.arib.b10.descriptors.CacheControlInfoDescriptor;
import sedec2.arib.b10.descriptors.ConditionalAccessDescriptor;
import sedec2.arib.b10.descriptors.ConnectionRequirementDescriptor;
import sedec2.arib.b10.descriptors.DataComponentDescriptor;
import sedec2.arib.b10.descriptors.Descriptor;
import sedec2.arib.b10.descriptors.DigitalCopyControlDescriptor;
import sedec2.arib.b10.descriptors.ParentalRatingDescriptor;
import sedec2.arib.b10.descriptors.RandomizedLatencyDescriptor;
import sedec2.arib.b10.descriptors.SimpleApplicationBoundaryDescriptor;
import sedec2.arib.b10.descriptors.SimpleApplicationLocationDescriptor;
import sedec2.arib.b10.descriptors.StreamIdentifierDescriptor;
import sedec2.arib.b10.descriptors.TransportProtocolDescriptor;
import sedec2.arib.b10.descriptors.UnknownDescriptor;
import sedec2.arib.b10.descriptors.VideoDecodeControlDescriptor;
import sedec2.base.BitReadWriter;

public class DescriptorFactory {
    public final static int APPLICATION_DESCRIPTOR = 0x00;
    public final static int APPLICATION_NAME_DESCRIPTOR = 0x01;
    public final static int TRANSPORT_PROTOCOL_DESCRIPTOR = 0x02;
    public final static int APPLICATION_RECORDING_DESCRIPTOR = 0x06;
    public final static int CONDITIONAL_ACCESS_DESCRIPTOR = 0x09;
    public final static int SIMPLE_APPLICATION_LOCATION_DESCRIPTOR = 0x15;
    public final static int APPLICATION_USAGE_DESCRIPTOR = 0x16;
    public final static int SIMPLE_APPLICATION_BOUNDARY_DESCRIPTOR = 0x17;
    public final static int APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR = 0x30;
    public final static int AUTOSTART_PRIORITY_DESCRIPTOR = 0X31;
    public final static int CACHE_CONTROL_INFO_DESCRIPTOR = 0x32;
    public final static int RANDOMIZED_LATENCY_DESCRIPTOR = 0x33;
    public final static int STREAM_IDENTIFIER_DESCRIPTOR = 0x52;
    public final static int PARENTAL_RATING_DESCRIPTOR = 0x55;
    public final static int CONNECTION_REQUIREMENT_DESCRIPTOR = 0x72;
    public final static int DIGITALCOPY_CONTROL_DESCRIPTOR = 0xc1;
    public final static int CA_SERVICE_DESCRIPTOR = 0xcc;
    public final static int VIDEODECODE_CONTROL_DESCRIPTOR = 0xc8;
    public final static int DATA_COMPONENT_DESCRIPTOR = 0xfd;
    public final static int UNKNOWN_DESCRIPTOR = 0xff;

    public static Descriptor createDescriptor(BitReadWriter brw) {
        int descriptor_tag = brw.getCurrentBuffer()[0] & 0x0000ff;

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
