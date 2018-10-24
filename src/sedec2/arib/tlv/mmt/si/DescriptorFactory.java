package sedec2.arib.tlv.mmt.si;

import sedec2.arib.tlv.mmt.si.descriptors.AccessControlDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.ApplicationServiceDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.AssetGroupDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.BackgroundColorDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.ContentCopyControlDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.ContentUsageControlDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.DependencyDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.arib.tlv.mmt.si.descriptors.EmergencyInformationDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.EmergencyNewsDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.EventMessageDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.EventPackageDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.IpDataFlowDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.LinkedPuDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.LockedCacheDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ApplicationBoundaryAndPermissionDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ApplicationDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ApplicationExpirationDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_AudioComponentDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_AutostartPriorityDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_BroadcasterNameDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_CAContractInfoDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_CAServiceDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_CAStartupDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_CacheControlInfoDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ComponentGroupDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_CompressionTypeDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ContentDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_DataComponentDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_DownloadProtectionDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_EventGroupDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ExpireDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ExtendedEventDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ExtendedTimestampDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ExternalApplicationControlDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_HEVCDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_HierachyDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_InfoDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_LinkageDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_LocalTimeOffsetDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_LogoTransmissionDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_MPEG4AudioDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_MPEG4AudioExtensionDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_NetworkDownloadContentDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ParentalRatingDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_PlaybackApplicationDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_RandomizedLatencyDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_SeriesDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ServiceDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ServiceListDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_ShortEventDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_SiParameterDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_SimpleApplicationLocationDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_SimplePlaybackApplicationLocationDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_StreamIdentifierDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_TargetRegionDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_TransportProtocolDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MH_TypeDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MPU_DownloadContentDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MPU_NodeDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MPU_PresentationRegionDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MPU_TimestampDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MessageAuthenticationMethodDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MultimediaServiceInformationDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.PUStructureDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.RelatedBroadcasterDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.ScramblerDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.UnknownDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.UnlockedCacheDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.UtcNptReferenceDescriptor;
import sedec2.arib.tlv.mmt.si.descriptors.VideoComponentDescriptor;
import sedec2.base.BitReadWriter;

public class DescriptorFactory {
    public final static int MPU_TIMESTAMP_DESCRIPTOR = 0x0001;
    public final static int DEPENDENCY_DESCRIPTOR = 0x0002;
    
    public final static int ASSET_GROUP_DESCRIPTOR = 0x8000;
    public final static int EVENT_PACKAGE_DESCRIPTOR = 0x8001;
    public final static int BACKGROUND_COLOR_DESCRIPTOR = 0x8002;
    public final static int MPU_PRESENTATION_REGION_DESCRIPTOR = 0x8003;
    public final static int ACCESS_CONTROL_DESCRIPTOR = 0x8004;
    public final static int SCRAMBLER_DESCRIPTOR = 0x8005;
    public final static int MESSAGE_AUTHENTICATION_METHOD_DESCRIPTOR = 0x8006;
    public final static int EMERGENCY_INFORMATION_DESCRIPTOR = 0x8007;
    public final static int MH_MPEG4_AUDIO_DESCRIPTOR = 0x8008;
    public final static int MH_MPEG4_AUDIO_EXTENSION_DESCRIPTOR = 0x8009;
    public final static int MH_HEVC_DESCRIPTOR = 0x800a;
    public final static int MH_EVENT_GROUP_DESCRIPTOR = 0x800c;
    public final static int MH_SERVICE_LIST_DESCRIPTOR = 0x800d;
    
    public final static int VIDEO_COMPONENT_DESCRIPTOR = 0x8010;
    public final static int MH_STREAM_IDENTIFIER_DESCRIPTOR = 0x8011;
    public final static int MH_CONTENT_DESCRIPTOR = 0x8012;
    public final static int MH_PARENTAL_RATING_DESCRIPTOR = 0x8013;
    public final static int MH_AUDIO_COMPONENT_DESCRIPTOR = 0x8014;
    public final static int MH_TARGET_REGION_DESCRIPTOR = 0x8015;
    public final static int MH_SERIES_DESCRIPTOR = 0x8016;
    public final static int MH_SI_PARAMETER_DESCRIPTOR = 0x8017;
    public final static int MH_BROADCASTER_NAME_DESCRIPTOR = 0x8018;
    public final static int MH_SERVICE_DESCRIPTOR = 0x8019;
    public final static int IP_DATA_FLOW_DESCRIPTOR = 0x801a;
    public final static int MH_CA_STARTUP_DESCRIPTOR = 0x801b;
    public final static int MH_TYPE_DESCRIPTOR = 0x801c;
    public final static int MH_INFO_DESCRIPTOR = 0x801d;
    public final static int MH_EXPIRE_DESCRIPTOR = 0x801e;
    public final static int MH_COMPRESSION_TYPE_DESCRIPTOR = 0x801f;
    public final static int MH_DATA_COMPONENT_DESCRIPTOR = 0x8020;
    public final static int UTC_NPT_REFERENCE_DESCRIPTOR = 0x8021;
    
    public final static int MH_LOCAL_TIME_OFFSET_DESCRIPTOR = 0x8023;
    public final static int MH_COMPONENT_GROUP_DESCRIPTOR = 0x8024;
    public final static int MH_LOGO_TRANSMISSION_DESCRIPTOR = 0x8025;
    public final static int MH_EXTENDED_TIMESTAMP_DESCRIPTOR = 0x8026;
    public final static int MPU_DOWNLOAD_CONTENT_DESCRIPTOR = 0x8027;
    public final static int MH_NETWORK_DOWNLOAD_CONTENT_DESCRIPTOR = 0x8028;
    public final static int MH_APPLICATION_DESCRIPTOR = 0x8029;
    public final static int MH_TRANSPORT_PROTOCOL_DESCRIPTOR = 0x802a;
    public final static int MH_SIMPLE_APPLICATION_LOCATION_DESCRIPTOR = 0x802b;
    public final static int MH_APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR = 0x802c;
    public final static int MH_AUTOSTART_PRIORITY_DESCRIPTOR = 0x802d;
    public final static int MH_CACHE_CONTROL_INFO_DESCRIPTOR = 0x802e;
    public final static int MH_RANDOMIZED_LATENCY_DESCRIPTOR = 0x802f;
    public final static int LINKED_PU_DESCRIPTOR = 0x8030;
    public final static int LOCKED_CACHE_DESCRIPTOR = 0x8031;
    public final static int UNLOCKED_CACHE_DESCRIPTOR = 0x8032;
    public final static int MH_DOWNLOAD_PROTECTION_DESCRIPTOR = 0x8033;
    public final static int APPLICATION_SERVICE_DESCRIPTOR = 0x8034;
    public final static int MPU_NODE_DESCRIPTOR = 0x8035;
    public final static int PU_STRUCTURE_DESCRIPTOR = 0x8036;
    public final static int MH_HIERACHY_DESCRIPTOR = 0x8037;
    public final static int CONTENT_COPY_CONTROL_DESCRIPTOR = 0x8038;
    
    public final static int CONTENT_USAGE_CONTROL_DESCRIPTOR = 0x8039;
    public final static int MH_EXTERNAL_APPLICATION_CONTROL_DESCRIPTOR = 0x803a;
    public final static int MH_PLAYBACK_APPLICATION_DESCRIPTOR = 0x803b;
    public final static int MH_SIMPLE_PLAYBACK_APPLICATION_LOCATION_DESCRIPTOR = 0x803c;
    public final static int MH_APPLICATION_EXPIRATION_DESCRIPTOR = 0x803d;
    public final static int RELATED_BROADCASTER_DESCRIPTOR = 0x803e;
    public final static int MULTIMEDIA_SERVICE_INFORMATION_DESCRIPTOR = 0x803f;
    public final static int EMERGENCY_NEWS_DESCRIPTOR = 0x8040;
    public final static int MH_CA_CONTRACT_INFORMATION_DESCRIPTOR = 0x8041;
    public final static int MH_CA_SERVICE_DESCRIPTOR= 0x8042;
    
    public final static int MH_LINKAGE_DESCRIPTOR = 0xf000;
    public final static int MH_SHORT_EVENT_DESCRIPTOR = 0xf001;
    public final static int MH_EXTENDED_EVENT_DESCRIPTOR = 0xf002;
    public final static int EVENT_MESSAGE_DESCRIPTOR = 0xf003;
    
    public final static int UNKNOWN_DESCRIPTOR = 0xffff;
    
    public static Descriptor createDescriptor(BitReadWriter brw) {
        int descriptor_tag = 
                (((brw.getCurrentBuffer()[0] & 0xff) << 8) |
                (brw.getCurrentBuffer()[1] & 0xff));

        switch ( descriptor_tag ) {
            case MH_CA_STARTUP_DESCRIPTOR:
                return new MH_CAStartupDescriptor(brw);
            case MH_COMPONENT_GROUP_DESCRIPTOR:
                return new MH_ComponentGroupDescriptor(brw);
            case MH_CA_SERVICE_DESCRIPTOR:
                return new MH_CAServiceDescriptor(brw);
            case MH_CA_CONTRACT_INFORMATION_DESCRIPTOR:
                return new MH_CAContractInfoDescriptor(brw);
            case MULTIMEDIA_SERVICE_INFORMATION_DESCRIPTOR:
                return new MultimediaServiceInformationDescriptor(brw);
            case RELATED_BROADCASTER_DESCRIPTOR:
                return new RelatedBroadcasterDescriptor(brw);
            case MH_APPLICATION_EXPIRATION_DESCRIPTOR:
                return new MH_ApplicationExpirationDescriptor(brw);
            case MH_SIMPLE_PLAYBACK_APPLICATION_LOCATION_DESCRIPTOR:
                return new MH_SimplePlaybackApplicationLocationDescriptor(brw);
            case MH_PLAYBACK_APPLICATION_DESCRIPTOR:
                return new MH_PlaybackApplicationDescriptor(brw);
            case EMERGENCY_NEWS_DESCRIPTOR:
                return new EmergencyNewsDescriptor(brw);
            case MH_EXTERNAL_APPLICATION_CONTROL_DESCRIPTOR:
                return new MH_ExternalApplicationControlDescriptor(brw);
            case CONTENT_USAGE_CONTROL_DESCRIPTOR:
                return new ContentUsageControlDescriptor(brw);
            case CONTENT_COPY_CONTROL_DESCRIPTOR:
                return new ContentCopyControlDescriptor(brw);
            case MH_HIERACHY_DESCRIPTOR:
                return new MH_HierachyDescriptor(brw);
            case PU_STRUCTURE_DESCRIPTOR:
                return new PUStructureDescriptor(brw);
            case MPU_NODE_DESCRIPTOR:
                return new MPU_NodeDescriptor(brw);
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
            case MH_MPEG4_AUDIO_DESCRIPTOR:
                return new MH_MPEG4AudioDescriptor(brw);
            case MH_MPEG4_AUDIO_EXTENSION_DESCRIPTOR:
                return new MH_MPEG4AudioExtensionDescriptor(brw);
            case MH_HEVC_DESCRIPTOR:
                return new MH_HEVCDescriptor(brw);
            case MH_EVENT_GROUP_DESCRIPTOR:
                return new MH_EventGroupDescriptor(brw);
            case MH_SERVICE_LIST_DESCRIPTOR:
                return new MH_ServiceListDescriptor(brw);
            case VIDEO_COMPONENT_DESCRIPTOR:
                return new VideoComponentDescriptor(brw);
            case MH_STREAM_IDENTIFIER_DESCRIPTOR:
                return new MH_StreamIdentifierDescriptor(brw);
            case MH_CONTENT_DESCRIPTOR:
                return new MH_ContentDescriptor(brw);
            case MH_PARENTAL_RATING_DESCRIPTOR:
                return new MH_ParentalRatingDescriptor(brw);
            case MH_AUDIO_COMPONENT_DESCRIPTOR:
                return new MH_AudioComponentDescriptor(brw);
            case MH_APPLICATION_DESCRIPTOR:
                return new MH_ApplicationDescriptor(brw);
            case MH_TRANSPORT_PROTOCOL_DESCRIPTOR:
                return new MH_TransportProtocolDescriptor(brw);
            case MH_SIMPLE_APPLICATION_LOCATION_DESCRIPTOR:
                return new MH_SimpleApplicationLocationDescriptor(brw);
            case MH_APPLICATION_BOUNDARY_AND_PERMISSION_DESCRIPTOR:
                return new MH_ApplicationBoundaryAndPermissionDescriptor(brw);
            case MH_AUTOSTART_PRIORITY_DESCRIPTOR:
                return new MH_AutostartPriorityDescriptor(brw);
            case MH_CACHE_CONTROL_INFO_DESCRIPTOR:
                return new MH_CacheControlInfoDescriptor(brw);
            case MH_RANDOMIZED_LATENCY_DESCRIPTOR:
                return new MH_RandomizedLatencyDescriptor(brw);
            case MH_TYPE_DESCRIPTOR:
                return new MH_TypeDescriptor(brw);
            case MH_INFO_DESCRIPTOR:
                return new MH_InfoDescriptor(brw);
            case MH_EXPIRE_DESCRIPTOR:
                return new MH_ExpireDescriptor(brw);
            case MH_COMPRESSION_TYPE_DESCRIPTOR:
                return new MH_CompressionTypeDescriptor(brw);
            case LINKED_PU_DESCRIPTOR:
                return new LinkedPuDescriptor(brw);
            case LOCKED_CACHE_DESCRIPTOR:
                return new LockedCacheDescriptor(brw);
            case UNLOCKED_CACHE_DESCRIPTOR:
                return new UnlockedCacheDescriptor(brw);
            case MH_BROADCASTER_NAME_DESCRIPTOR:
                return new MH_BroadcasterNameDescriptor(brw);
            case MH_SERIES_DESCRIPTOR:
                return new MH_SeriesDescriptor(brw);
            case MH_LOGO_TRANSMISSION_DESCRIPTOR:
                return new MH_LogoTransmissionDescriptor(brw);
            case MH_TARGET_REGION_DESCRIPTOR:
                return new MH_TargetRegionDescriptor(brw);
            case MH_SI_PARAMETER_DESCRIPTOR:
                return new MH_SiParameterDescriptor(brw);
            case IP_DATA_FLOW_DESCRIPTOR:
                return new IpDataFlowDescriptor(brw);
            case MH_DATA_COMPONENT_DESCRIPTOR:
                return new MH_DataComponentDescriptor(brw);
            case UTC_NPT_REFERENCE_DESCRIPTOR:
                return new UtcNptReferenceDescriptor(brw);
            case MH_LOCAL_TIME_OFFSET_DESCRIPTOR:
                return new MH_LocalTimeOffsetDescriptor(brw);
            case MH_SERVICE_DESCRIPTOR:
                return new MH_ServiceDescriptor(brw);
            case MH_EXTENDED_TIMESTAMP_DESCRIPTOR:
                return new MH_ExtendedTimestampDescriptor(brw);
            case MPU_DOWNLOAD_CONTENT_DESCRIPTOR:
                return new MPU_DownloadContentDescriptor(brw);
            case EVENT_MESSAGE_DESCRIPTOR:
                return new EventMessageDescriptor(brw);
            case MH_EXTENDED_EVENT_DESCRIPTOR:
                return new MH_ExtendedEventDescriptor(brw);
            case MH_SHORT_EVENT_DESCRIPTOR:
                return new MH_ShortEventDescriptor(brw);
            case MH_LINKAGE_DESCRIPTOR:
                return new MH_LinkageDescriptor(brw);
            case MH_DOWNLOAD_PROTECTION_DESCRIPTOR:
                return new MH_DownloadProtectionDescriptor(brw);
            case MH_NETWORK_DOWNLOAD_CONTENT_DESCRIPTOR:
                return new MH_NetworkDownloadContentDescriptor(brw);
            case APPLICATION_SERVICE_DESCRIPTOR:
                return new ApplicationServiceDescriptor(brw);
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }
    
    private DescriptorFactory() {
        
    }
}
