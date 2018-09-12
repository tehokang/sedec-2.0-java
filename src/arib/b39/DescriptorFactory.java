package arib.b39;

import base.BitReadWriter;
import arib.b39.descriptors.UnknownDescriptor;
import arib.b39.descriptors.UnlockedCacheDescriptor;
import arib.b39.descriptors.MPEGH_ApplicationBoundaryAndPermissionDescriptor;
import arib.b39.descriptors.MPEGH_ApplicationDescriptor;
import arib.b39.descriptors.MPEGH_AutostartPriorityDescriptor;
import arib.b39.descriptors.MPEGH_CacheControlInfoDescriptor;
import arib.b39.descriptors.MPEGH_CompressionTypeDescriptor;
import arib.b39.descriptors.AccessControlDescriptor;
import arib.b39.descriptors.AssetGroupDescriptor;
import arib.b39.descriptors.BackgroundColorDescriptor;
import arib.b39.descriptors.DependencyDescriptor;
import arib.b39.descriptors.Descriptor;
import arib.b39.descriptors.EventPackageDescriptor;
import arib.b39.descriptors.MPEGH_ExpireDescriptor;
import arib.b39.descriptors.MPEGH_InfoDescriptor;
import arib.b39.descriptors.LinkedPuDescriptor;
import arib.b39.descriptors.LockedCacheDescriptor;
import arib.b39.descriptors.MPU_TimestampDescriptor;
import arib.b39.descriptors.ScramblerDescriptor;
import arib.b39.descriptors.MPEGH_RandomizedLatencyDescriptor;
import arib.b39.descriptors.MPEGH_SimpleApplicationLocationDescriptor;
import arib.b39.descriptors.MPEGH_TransportProtocolDescriptor;
import arib.b39.descriptors.MPEGH_TypeDescriptor;
import arib.b39.descriptors.MPU_PresentationRegionDescriptor;

public class DescriptorFactory {
    public final static int MPU_TIMESTAMP_DESCRIPTOR = 0x0000;
    public final static int DEPENDENCY_DESCRIPTOR = 0x0002;
    public final static int ASSET_GROUP_DESCRIPTOR = 0x8000;
    public final static int EVENT_PACKAGE_DESCRIPTOR = 0x8001;
    public final static int BACKGROUND_COLOR_DESCRIPTOR = 0x8002;
    public final static int MPU_PRESENTATION_REGION_DESCRIPTOR = 0x8003;
    public final static int ACCESS_CONTROL_DESCRIPTOR = 0x8004;
    public final static int SCRAMBLER_DESCRIPTOR = 0x8005;
    public final static int MESSAGE_AUTHENTICATION_METHOD_DESCRIPTOR = 0x8006;
    
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
            case MPU_TIMESTAMP_DESCRIPTOR:
                return new MPU_TimestampDescriptor(brw);
            case DEPENDENCY_DESCRIPTOR:
                return new DependencyDescriptor(brw);
            case ASSET_GROUP_DESCRIPTOR:
                return new AssetGroupDescriptor(brw);
            case EVENT_PACKAGE_DESCRIPTOR:
                return new EventPackageDescriptor(brw);
            case BACKGROUND_COLOR_DESCRIPTOR:
                return new BackgroundColorDescriptor(brw);
            case MPU_PRESENTATION_REGION_DESCRIPTOR:
                return new MPU_PresentationRegionDescriptor(brw);
            case ACCESS_CONTROL_DESCRIPTOR:
                return new AccessControlDescriptor(brw);
            case SCRAMBLER_DESCRIPTOR:
                return new ScramblerDescriptor(brw);
            case MPEGH_APPLICATION_DESCRIPTOR:
                return new MPEGH_ApplicationDescriptor(brw);
            case MPEGH_TRANSPORT_PROTOCOL_DESCRIPTOR:
                return new MPEGH_TransportProtocolDescriptor(brw);
            case MPEGH_SIMPLE_APPLICATION_LOCATION_DESCRIPTOR:
                return new MPEGH_SimpleApplicationLocationDescriptor(brw);
            case MPEGH_APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR:
                return new MPEGH_ApplicationBoundaryAndPermissionDescriptor(brw);
            case MPEGH_AUTOSTART_PRIORITY_DESCRIPTOR:
                return new MPEGH_AutostartPriorityDescriptor(brw);
            case MPEGH_CACHE_CONTROL_INFO_DESCRIPTOR:
                return new MPEGH_CacheControlInfoDescriptor(brw);
            case MPEGH_RANDOMIZED_LATENCY_DESCRIPTOR:
                return new MPEGH_RandomizedLatencyDescriptor(brw);
            case MPEGH_TYPE_DESCRIPTOR:
                return new MPEGH_TypeDescriptor(brw);
            case MPEGH_INFO_DESCRIPTOR:
                return new MPEGH_InfoDescriptor(brw);
            case MPEGH_EXPIRE_DESCRIPTOR:
                return new MPEGH_ExpireDescriptor(brw);
            case MPEGH_COMPRESSION_TYPE_DESCRIPTOR:
                return new MPEGH_CompressionTypeDescriptor(brw);
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
