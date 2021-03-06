package com.sedec.arib.tlv.container.mmt.si;

import com.sedec.arib.tlv.container.mmt.si.descriptors.AccessControlDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.ApplicationServiceDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.AssetGroupDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.BackgroundColorDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.ContentCopyControlDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.ContentUsageControlDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.DependencyDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.Descriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.EmergencyInformationDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.EmergencyNewsDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.EventMessageDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.EventPackageDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.IpDataFlowDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.LinkedPuDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.LockedCacheDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ApplicationBoundaryAndPermissionDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ApplicationDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ApplicationExpirationDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_AudioComponentDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_AutostartPriorityDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_BroadcasterNameDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_CAContractInfoDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_CAServiceDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_CAStartupDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_CacheControlInfoDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ComponentGroupDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_CompressionTypeDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ContentDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_DataComponentDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_DownloadProtectionDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_EventGroupDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ExpireDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ExtendedEventDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ExternalApplicationControlDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_HEVCDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_HierachyDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_InfoDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_LinkageDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_LocalTimeOffsetDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_LogoTransmissionDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_MPEG4AudioDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_MPEG4AudioExtensionDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_NetworkDownloadContentDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ParentalRatingDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_PlaybackApplicationDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_RandomizedLatencyDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_SeriesDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ServiceDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ServiceListDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_ShortEventDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_SiParameterDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_SimpleApplicationLocationDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_SimplePlaybackApplicationLocationDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_StreamIdentifierDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_TargetRegionDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_TransportProtocolDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MH_TypeDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MPU_DownloadContentDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MPU_ExtendedTimestampDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MPU_NodeDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MPU_PresentationRegionDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MPU_TimestampDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MessageAuthenticationMethodDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.MultimediaServiceInformationDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.PUStructureDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.RelatedBroadcasterDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.ScramblerDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.UnknownDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.UnlockedCacheDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.UtcNptReferenceDescriptor;
import com.sedec.arib.tlv.container.mmt.si.descriptors.VideoComponentDescriptor;
import com.sedec.base.BitReadWriter;

/**
 * Factory to obtain a kind of descriptors of MMT-SI like below.
 * <ul>
 * <li> {@link AccessControlDescriptor}
 * <li> {@link ApplicationServiceDescriptor}
 * <li> {@link AssetGroupDescriptor}
 * <li> {@link BackgroundColorDescriptor}
 * <li> {@link ContentCopyControlDescriptor}
 * <li> {@link ContentUsageControlDescriptor}
 * <li> {@link DependencyDescriptor}
 * <li> {@link EmergencyInformationDescriptor}
 * <li> {@link EmergencyNewsDescriptor}
 * <li> {@link EventMessageDescriptor}
 * <li> {@link EventPackageDescriptor}
 * <li> {@link IpDataFlowDescriptor}
 * <li> {@link LinkedPuDescriptor}
 * <li> {@link LockedCacheDescriptor}
 * <li> {@link MH_ApplicationBoundaryAndPermissionDescriptor}
 * <li> {@link MH_ApplicationDescriptor}
 * <li> {@link MH_ApplicationExpirationDescriptor}
 * <li> {@link MH_AudioComponentDescriptor}
 * <li> {@link MH_AutostartPriorityDescriptor}
 * <li> {@link MH_BroadcasterNameDescriptor}
 * <li> {@link MH_CAContractInfoDescriptor}
 * <li> {@link MH_CAServiceDescriptor}
 * <li> {@link MH_CAStartupDescriptor}
 * <li> {@link MH_CacheControlInfoDescriptor}
 * <li> {@link MH_ComponentGroupDescriptor}
 * <li> {@link MH_CompressionTypeDescriptor}
 * <li> {@link MH_ContentDescriptor}
 * <li> {@link MH_DataComponentDescriptor}
 * <li> {@link MH_DownloadProtectionDescriptor}
 * <li> {@link MH_EventGroupDescriptor}
 * <li> {@link MH_ExpireDescriptor}
 * <li> {@link MH_ExtendedEventDescriptor}
 * <li> {@link MH_ExternalApplicationControlDescriptor}
 * <li> {@link MH_HEVCDescriptor}
 * <li> {@link MH_HierachyDescriptor}
 * <li> {@link MH_InfoDescriptor}
 * <li> {@link MH_LinkageDescriptor}
 * <li> {@link MH_LocalTimeOffsetDescriptor}
 * <li> {@link MH_LogoTransmissionDescriptor}
 * <li> {@link MH_MPEG4AudioDescriptor}
 * <li> {@link MH_MPEG4AudioExtensionDescriptor}
 * <li> {@link MH_NetworkDownloadContentDescriptor}
 * <li> {@link MH_ParentalRatingDescriptor}
 * <li> {@link MH_PlaybackApplicationDescriptor}
 * <li> {@link MH_RandomizedLatencyDescriptor}
 * <li> {@link MH_SeriesDescriptor}
 * <li> {@link MH_ServiceDescriptor}
 * <li> {@link MH_ServiceListDescriptor}
 * <li> {@link MH_ShortEventDescriptor}
 * <li> {@link MH_SiParameterDescriptor}
 * <li> {@link MH_SimpleApplicationLocationDescriptor}
 * <li> {@link MH_SimplePlaybackApplicationLocationDescriptor}
 * <li> {@link MH_StreamIdentifierDescriptor}
 * <li> {@link MH_TargetRegionDescriptor}
 * <li> {@link MH_TransportProtocolDescriptor}
 * <li> {@link MH_TypeDescriptor}
 * <li> {@link MPU_DownloadContentDescriptor}
 * <li> {@link MPU_ExtendedTimestampDescriptor}
 * <li> {@link MPU_NodeDescriptor}
 * <li> {@link MPU_PresentationRegionDescriptor}
 * <li> {@link MPU_TimestampDescriptor}
 * <li> {@link MessageAuthenticationMethodDescriptor}
 * <li> {@link MultimediaServiceInformationDescriptor}
 * <li> {@link PUStructureDescriptor}
 * <li> {@link RelatedBroadcasterDescriptor}
 * <li> {@link ScramblerDescriptor}
 * <li> {@link UnlockedCacheDescriptor}
 * <li> {@link UtcNptReferenceDescriptor}
 * <li> {@link VideoComponentDescriptor}
 * <li> {@link UnknownDescriptor}
 *
 * </ul>
 */
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
    public final static int MPU_EXTENDED_TIMESTAMP_DESCRIPTOR = 0x8026;
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
            case MPU_EXTENDED_TIMESTAMP_DESCRIPTOR:
                return new MPU_ExtendedTimestampDescriptor(brw);
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
