package mpegh;

import base.BitReadWriter;
import mpegh.descriptors.UnknownDescriptor;
import mpegh.descriptors.UnlockedCacheDescriptor;
import mpegh.descriptors.ApplicationBoundaryAndPermissionDescriptor;
import mpegh.descriptors.ApplicationDescriptor;
import mpegh.descriptors.AutostartPriorityDescriptor;
import mpegh.descriptors.CacheControlInfoDescriptor;
import mpegh.descriptors.CompressionTypeDescriptor;
import mpegh.descriptors.Descriptor;
import mpegh.descriptors.ExpireDescriptor;
import mpegh.descriptors.InfoDescriptor;
import mpegh.descriptors.LinkedPuDescriptor;
import mpegh.descriptors.LockedCacheDescriptor;
import mpegh.descriptors.RandomizedLatencyDescriptor;
import mpegh.descriptors.SimpleApplicationLocationDescriptor;
import mpegh.descriptors.TransportProtocolDescriptor;
import mpegh.descriptors.TypeDescriptor;

public class DescriptorFactory {
    public final static int APPLICATION_DESCRIPTOR = 0x8029;
    public final static int TRANSPORT_PROTOCOL_DESCRIPTOR = 0x802a;
    public final static int SIMPLE_APPLICATION_LOCATION_DESCRIPTOR = 0x802b;
    public final static int APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR = 0x802c;
    public final static int AUTOSTART_PRIORITY_DESCRIPTOR = 0x802d;
    public final static int CACHE_CONTROL_INFO_DESCRIPTOR = 0x802e;
    public final static int RANDOMIZED_LATENCY_DESCRIPTOR = 0x802f;
    public final static int TYPE_DESCRIPTOR = 0x801c;
    public final static int INFO_DESCRIPTOR = 0x801d;
    public final static int EXPIRE_DESCRIPTOR = 0x801e;
    public final static int COMPRESSION_TYPE_DESCRIPTOR = 0x801f;
    public final static int LINKED_PU_DESCRIPTOR = 0x8030;
    public final static int LOCKED_CACHE_DESCRIPTOR = 0x8031;
    public final static int UNLOCKED_CACHE_DESCRIPTOR = 0x8032;
    
    public final static int UNKNOWN_DESCRIPTOR = 0xffff;
    
    public static Descriptor CreateDescriptor(BitReadWriter brw) {
        int descriptor_tag = 
                (((brw.GetCurrentBuffer()[0] & 0xff) << 8) |
                (brw.GetCurrentBuffer()[1] & 0xff));
        
        switch ( descriptor_tag ) {
            case APPLICATION_DESCRIPTOR:
                return new ApplicationDescriptor(brw);
            case TRANSPORT_PROTOCOL_DESCRIPTOR:
                return new TransportProtocolDescriptor(brw);
            case SIMPLE_APPLICATION_LOCATION_DESCRIPTOR:
                return new SimpleApplicationLocationDescriptor(brw);
            case APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR:
                return new ApplicationBoundaryAndPermissionDescriptor(brw);
            case AUTOSTART_PRIORITY_DESCRIPTOR:
                return new AutostartPriorityDescriptor(brw);
            case CACHE_CONTROL_INFO_DESCRIPTOR:
                return new CacheControlInfoDescriptor(brw);
            case RANDOMIZED_LATENCY_DESCRIPTOR:
                return new RandomizedLatencyDescriptor(brw);
            case TYPE_DESCRIPTOR:
                return new TypeDescriptor(brw);
            case INFO_DESCRIPTOR:
                return new InfoDescriptor(brw);
            case EXPIRE_DESCRIPTOR:
                return new ExpireDescriptor(brw);
            case COMPRESSION_TYPE_DESCRIPTOR:
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
