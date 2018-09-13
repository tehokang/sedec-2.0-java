package arib.b39;

import arib.b39.descriptors.AccessControlDescriptor;
import arib.b39.descriptors.AssetGroupDescriptor;
import arib.b39.descriptors.BackgroundColorDescriptor;
import arib.b39.descriptors.DependencyDescriptor;
import arib.b39.descriptors.Descriptor;
import arib.b39.descriptors.EmergencyInformationDescriptor;
import arib.b39.descriptors.EventPackageDescriptor;
import arib.b39.descriptors.LinkedPuDescriptor;
import arib.b39.descriptors.LockedCacheDescriptor;
import arib.b39.descriptors.MPEGH_ApplicationBoundaryAndPermissionDescriptor;
import arib.b39.descriptors.MPEGH_ApplicationDescriptor;
import arib.b39.descriptors.MPEGH_AutostartPriorityDescriptor;
import arib.b39.descriptors.MPEGH_BroadcasterNameDescriptor;
import arib.b39.descriptors.MPEGH_CacheControlInfoDescriptor;
import arib.b39.descriptors.MPEGH_CompressionTypeDescriptor;
import arib.b39.descriptors.MPEGH_ContentDescriptor;
import arib.b39.descriptors.MPEGH_EventGroupDescriptor;
import arib.b39.descriptors.MPEGH_ExpireDescriptor;
import arib.b39.descriptors.MPEGH_HEVCDescriptor;
import arib.b39.descriptors.MPEGH_InfoDescriptor;
import arib.b39.descriptors.MPEGH_LogoTransmissionDescriptor;
import arib.b39.descriptors.MPEGH_MPEG4AudioDescriptor;
import arib.b39.descriptors.MPEGH_MPEG4AudioExtensionDescriptor;
import arib.b39.descriptors.MPEGH_ParentalRatingDescriptor;
import arib.b39.descriptors.MPEGH_RandomizedLatencyDescriptor;
import arib.b39.descriptors.MPEGH_ServiceDescriptor;
import arib.b39.descriptors.MPEGH_ServiceListDescriptor;
import arib.b39.descriptors.MPEGH_SimpleApplicationLocationDescriptor;
import arib.b39.descriptors.MPEGH_StreamIdentifierDescriptor;
import arib.b39.descriptors.MPEGH_TransportProtocolDescriptor;
import arib.b39.descriptors.MPEGH_TypeDescriptor;
import arib.b39.descriptors.MPU_PresentationRegionDescriptor;
import arib.b39.descriptors.MPU_TimestampDescriptor;
import arib.b39.descriptors.MessageAuthenticationMethodDescriptor;
import arib.b39.descriptors.NetworkNameDescriptor;
import arib.b39.descriptors.SateliteDeliverySystemDescriptor;
import arib.b39.descriptors.ScramblerDescriptor;
import arib.b39.descriptors.ServiceListDescriptor;
import arib.b39.descriptors.SystemManagementDescriptor;
import arib.b39.descriptors.UnknownDescriptor;
import arib.b39.descriptors.UnlockedCacheDescriptor;
import arib.b39.descriptors.VideoComponentDescriptor;
import base.BitReadWriter;

public class DescriptorFactory {
    public final static int NETWORK_NAME_DESCRIPTOR = 0x40;
    public final static int SERVICE_LIST_DESCRIPTOR = 0x41;
    public final static int SATELITE_DELIVERY_SYSTEM_DESCRIPTOR = 0x43;
    public final static int SYSTEM_MANAGEMENT_DESCRIPTOR = 0xfe;
    
    public final static int MPU_TIMESTAMP_DESCRIPTOR = 0x0000;
    public final static int DEPENDENCY_DESCRIPTOR = 0x0002;
    public final static int ASSET_GROUP_DESCRIPTOR = 0x8000;
    public final static int EVENT_PACKAGE_DESCRIPTOR = 0x8001;
    public final static int BACKGROUND_COLOR_DESCRIPTOR = 0x8002;
    public final static int MPU_PRESENTATION_REGION_DESCRIPTOR = 0x8003;
    public final static int ACCESS_CONTROL_DESCRIPTOR = 0x8004;
    public final static int SCRAMBLER_DESCRIPTOR = 0x8005;
    public final static int MESSAGE_AUTHENTICATION_METHOD_DESCRIPTOR = 0x8006;
    public final static int EMERGENCY_INFORMATION_DESCRIPTOR = 0x8007;
    public final static int MPEGH_MPEG4_AUDIO_DESCRIPTOR = 0x8008;
    public final static int MPEGH_MPEG4_AUDIO_EXTENSION_DESCRIPTOR = 0x8009;
    public final static int MPEGH_HEVC_DESCRIPTOR = 0x800a;
    public final static int MPEGH_EVENT_GROUP_DESCRIPTOR = 0x800c;
    public final static int MPEGH_SERVICE_LIST_DESCRIPTOR = 0x800d;
    public final static int VIDEO_COMPONENT_DESCRIPTOR = 0x8010;
    public final static int MPEGH_STREAM_IDENTIFIER_DESCRIPTOR = 0x8011;
    public final static int MPEGH_CONTENT_DESCRIPTOR = 0x8012;
    public final static int MPEGH_PARENTAL_RATING_DESCRIPTOR = 0x8013;
    
    public final static int MPEGH_TYPE_DESCRIPTOR = 0x801c;
    public final static int MPEGH_INFO_DESCRIPTOR = 0x801d;
    public final static int MPEGH_EXPIRE_DESCRIPTOR = 0x801e;
    public final static int MPEGH_COMPRESSION_TYPE_DESCRIPTOR = 0x801f;
    public final static int MPEGH_APPLICATION_DESCRIPTOR = 0x8029;
    public final static int MPEGH_TRANSPORT_PROTOCOL_DESCRIPTOR = 0x802a;
    public final static int MPEGH_SIMPLE_APPLICATION_LOCATION_DESCRIPTOR = 0x802b;
    public final static int MPEGH_APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR = 0x802c;
    public final static int MPEGH_AUTOSTART_PRIORITY_DESCRIPTOR = 0x802d;
    public final static int MPEGH_CACHE_CONTROL_INFO_DESCRIPTOR = 0x802e;
    public final static int MPEGH_RANDOMIZED_LATENCY_DESCRIPTOR = 0x802f;
    public final static int LINKED_PU_DESCRIPTOR = 0x8030;
    public final static int LOCKED_CACHE_DESCRIPTOR = 0x8031;
    public final static int UNLOCKED_CACHE_DESCRIPTOR = 0x8032;
    public final static int MPEGH_BROADCASTER_NAME_DESCRIPTOR = 0x8018;
    public final static int MPEGH_SERVICE_DESCRIPTOR = 0x8019;
    public final static int MPEGH_LOGO_TRANSMISSION_DESCRIPTOR = 0x8025;
    
    /**
     * @todo
     */
    public final static int MPEGH_AUDIO_COMPONENT_DESCRIPTOR = 0x8014;
    public final static int MPEGH_TARGET_REGION_DESCRIPTOR = 0x8015;
    public final static int MPEGH_SERIES_DESCRIPTOR = 0x8016;
    public final static int MPEGH_SI_PARAMETER_DESCRIPTOR = 0x8017;
    public final static int IP_DATA_FLOW_DESCRIPTOR = 0x801a;
    public final static int MPEGH_CA_STARTUP_DESCRIPTOR = 0x801b;
    public final static int MPEGH_DATA_COMPONENT_DESCRIPTOR = 0x8020;
    public final static int UTC_NPT_REFERENCE_DESCRIPTOR = 0x8021;
    public final static int MPEGH_LOCAL_TIME_OFFSET_DESCRIPTOR = 0x8023;
    public final static int MPEGH_COMPONENT_GROUP_DESCRIPTOR = 0x8024;
    public final static int MPEGH_EXTENDED_TIMESTAMP_DESCRIPTOR = 0x8026;
    public final static int MPU_DOWNLOAD_CONTENT_DESCRIPTOR = 0x8027;
    public final static int MPEGH_NETWORK_DOWNLOAD_CONTENT_DESCRIPTOR = 0x8028;
    public final static int MPEGH_PROTECTION_DESCRIPTOR = 0x8033;
    public final static int APPLICATION_SERVICE_DESCRIPTOR = 0x8034;
    public final static int MPEGH_LINKAGE_DESCRIPTOR = 0xf000;
    public final static int MPEGH_SHORT_EVENT_DESCRIPTOR = 0xf001;
    public final static int MPEGH_EXTENED_EVENT_DECRIPTOR = 0xf002;
    public final static int EVENT_MESSAGE_DESCRIPTOR = 0xf003;
    
    public final static int UNKNOWN_DESCRIPTOR = 0xffff;
    
    public static Descriptor CreateDescriptor(BitReadWriter brw) {
        int descriptor_tag = 
                (((brw.GetCurrentBuffer()[0] & 0xff) << 8) |
                (brw.GetCurrentBuffer()[1] & 0xff));
        
        switch ( descriptor_tag ) {
            case SYSTEM_MANAGEMENT_DESCRIPTOR:
                return new SystemManagementDescriptor(brw);
            case SATELITE_DELIVERY_SYSTEM_DESCRIPTOR:
                return new SateliteDeliverySystemDescriptor(brw);
            case NETWORK_NAME_DESCRIPTOR:
                return new NetworkNameDescriptor(brw);
            case SERVICE_LIST_DESCRIPTOR:
                return new ServiceListDescriptor(brw);
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
            case MESSAGE_AUTHENTICATION_METHOD_DESCRIPTOR:
                return new MessageAuthenticationMethodDescriptor(brw);
            case EMERGENCY_INFORMATION_DESCRIPTOR:
                return new EmergencyInformationDescriptor(brw);
            case MPEGH_MPEG4_AUDIO_DESCRIPTOR:
                return new MPEGH_MPEG4AudioDescriptor(brw);
            case MPEGH_MPEG4_AUDIO_EXTENSION_DESCRIPTOR:
                return new MPEGH_MPEG4AudioExtensionDescriptor(brw);
            case MPEGH_HEVC_DESCRIPTOR:
                return new MPEGH_HEVCDescriptor(brw);
            case MPEGH_EVENT_GROUP_DESCRIPTOR:
                return new MPEGH_EventGroupDescriptor(brw);
            case MPEGH_SERVICE_LIST_DESCRIPTOR:
                return new MPEGH_ServiceListDescriptor(brw);
            case VIDEO_COMPONENT_DESCRIPTOR:
                return new VideoComponentDescriptor(brw);
            case MPEGH_STREAM_IDENTIFIER_DESCRIPTOR:
                return new MPEGH_StreamIdentifierDescriptor(brw);
            case MPEGH_CONTENT_DESCRIPTOR:
                return new MPEGH_ContentDescriptor(brw);
            case MPEGH_PARENTAL_RATING_DESCRIPTOR:
                return new MPEGH_ParentalRatingDescriptor(brw);
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
            case MPEGH_BROADCASTER_NAME_DESCRIPTOR:
                return new MPEGH_BroadcasterNameDescriptor(brw);
            case MPEGH_SERIES_DESCRIPTOR:
                return new MPEGH_ServiceDescriptor(brw);
            case MPEGH_LOGO_TRANSMISSION_DESCRIPTOR:
                return new MPEGH_LogoTransmissionDescriptor(brw);
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }
    
    private DescriptorFactory() {
        
    }
}
