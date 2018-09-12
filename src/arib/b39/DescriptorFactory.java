package arib.b39;

import base.BitReadWriter;
import arib.b39.descriptors.UnknownDescriptor;
import arib.b39.descriptors.UnlockedCacheDescriptor;
import arib.b39.descriptors.ApplicationBoundaryAndPermissionDescriptor;
import arib.b39.descriptors.ApplicationDescriptor;
import arib.b39.descriptors.AutostartPriorityDescriptor;
import arib.b39.descriptors.CacheControlInfoDescriptor;
import arib.b39.descriptors.CompressionTypeDescriptor;
import arib.b39.descriptors.Descriptor;
import arib.b39.descriptors.ExpireDescriptor;
import arib.b39.descriptors.InfoDescriptor;
import arib.b39.descriptors.LinkedPuDescriptor;
import arib.b39.descriptors.LockedCacheDescriptor;
import arib.b39.descriptors.RandomizedLatencyDescriptor;
import arib.b39.descriptors.SimpleApplicationLocationDescriptor;
import arib.b39.descriptors.TransportProtocolDescriptor;
import arib.b39.descriptors.TypeDescriptor;

public class DescriptorFactory {
    public final static int MPEGH_APPLICATION_DESCRIPTOR = 0x8029;
    public final static int MPEGH_TRANSPORT_PROTOCOL_DESCRIPTOR = 0x802a;
    public final static int MPEGH_SIMPLE_APPLICATION_LOCATION_DESCRIPTOR = 0x802b;
    public final static int MPEGH_APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR = 0x802c;
    public final static int MPEGH_AUTOSTART_PRIORITY_DESCRIPTOR = 0x802d;
    public final static int MPEGH_CACHE_CONTROL_INFO_DESCRIPTOR = 0x802e;
    public final static int MPEGH_RANDOMIZED_LATENCY_DESCRIPTOR = 0x802f;
    public final static int MPEGH_TYPE_DESCRIPTOR = 0x801c;
    public final static int MPEGH_INFO_DESCRIPTOR = 0x801d;
    public final static int MPEGH_EXPIRE_DESCRIPTOR = 0x801e;
    public final static int MPEGH_COMPRESSION_TYPE_DESCRIPTOR = 0x801f;
    public final static int LINKED_PU_DESCRIPTOR = 0x8030;
    public final static int LOCKED_CACHE_DESCRIPTOR = 0x8031;
    public final static int UNLOCKED_CACHE_DESCRIPTOR = 0x8032;
    
    public final static int UNKNOWN_DESCRIPTOR = 0xffff;
    
    public static Descriptor CreateDescriptor(BitReadWriter brw) {
        int descriptor_tag = 
                (((brw.GetCurrentBuffer()[0] & 0xff) << 8) |
                (brw.GetCurrentBuffer()[1] & 0xff));
        
        switch ( descriptor_tag ) {
            case MPEGH_APPLICATION_DESCRIPTOR:
                return new ApplicationDescriptor(brw);
            case MPEGH_TRANSPORT_PROTOCOL_DESCRIPTOR:
                return new TransportProtocolDescriptor(brw);
            case MPEGH_SIMPLE_APPLICATION_LOCATION_DESCRIPTOR:
                return new SimpleApplicationLocationDescriptor(brw);
            case MPEGH_APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR:
                return new ApplicationBoundaryAndPermissionDescriptor(brw);
            case MPEGH_AUTOSTART_PRIORITY_DESCRIPTOR:
                return new AutostartPriorityDescriptor(brw);
            case MPEGH_CACHE_CONTROL_INFO_DESCRIPTOR:
                return new CacheControlInfoDescriptor(brw);
            case MPEGH_RANDOMIZED_LATENCY_DESCRIPTOR:
                return new RandomizedLatencyDescriptor(brw);
            case MPEGH_TYPE_DESCRIPTOR:
                return new TypeDescriptor(brw);
            case MPEGH_INFO_DESCRIPTOR:
                return new InfoDescriptor(brw);
            case MPEGH_EXPIRE_DESCRIPTOR:
                return new ExpireDescriptor(brw);
            case MPEGH_COMPRESSION_TYPE_DESCRIPTOR:
                return new CompressionTypeDescriptor(brw);
            case LINKED_PU_DESCRIPTOR:
                return new LinkedPuDescriptor(brw);
            case LOCKED_CACHE_DESCRIPTOR:
                return new LockedCacheDescriptor(brw);
            case UNLOCKED_CACHE_DESCRIPTOR:
                return new UnlockedCacheDescriptor(brw);
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }
    
    private DescriptorFactory() {
        
    }
}
