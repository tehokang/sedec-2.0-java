package sedec2.dvb.ts.si;

import sedec2.base.BitReadWriter;
import sedec2.dvb.ts.si.descriptors.AC3Descriptor;
import sedec2.dvb.ts.si.descriptors.ApplicationDescriptor;
import sedec2.dvb.ts.si.descriptors.ApplicationNameDescriptor;
import sedec2.dvb.ts.si.descriptors.ApplicationRecordingDescriptor;
import sedec2.dvb.ts.si.descriptors.ApplicationSignallingDescriptor;
import sedec2.dvb.ts.si.descriptors.ApplicationUsageDescriptor;
import sedec2.dvb.ts.si.descriptors.BouquetNameDescriptor;
import sedec2.dvb.ts.si.descriptors.CableDeliverySystemDescriptor;
import sedec2.dvb.ts.si.descriptors.CarouselIdentifierDescriptor;
import sedec2.dvb.ts.si.descriptors.CellListDescriptor;
import sedec2.dvb.ts.si.descriptors.ConditionalAccessDescriptor;
import sedec2.dvb.ts.si.descriptors.ConnectionRequirementDescriptor;
import sedec2.dvb.ts.si.descriptors.DataBroadcastDescriptor;
import sedec2.dvb.ts.si.descriptors.DataBroadcastIdDescriptor;
import sedec2.dvb.ts.si.descriptors.DataComponentDescriptor;
import sedec2.dvb.ts.si.descriptors.Descriptor;
import sedec2.dvb.ts.si.descriptors.DigitalCopyControlDescriptor;
import sedec2.dvb.ts.si.descriptors.FrequencyListDescriptor;
import sedec2.dvb.ts.si.descriptors.ISO639LanguageDescriptor;
import sedec2.dvb.ts.si.descriptors.LinkageDescriptor;
import sedec2.dvb.ts.si.descriptors.NetworkNameDescriptor;
import sedec2.dvb.ts.si.descriptors.ParentalRatingDescriptor;
import sedec2.dvb.ts.si.descriptors.S2SateliteDeliverySystemDescriptor;
import sedec2.dvb.ts.si.descriptors.SateliteDeliverySystemDescriptor;
import sedec2.dvb.ts.si.descriptors.ServiceDescriptor;
import sedec2.dvb.ts.si.descriptors.ServiceListDescriptor;
import sedec2.dvb.ts.si.descriptors.SimpleApplicationBoundaryDescriptor;
import sedec2.dvb.ts.si.descriptors.SimpleApplicationLocationDescriptor;
import sedec2.dvb.ts.si.descriptors.StreamIdentifierDescriptor;
import sedec2.dvb.ts.si.descriptors.SubtitlingDescriptor;
import sedec2.dvb.ts.si.descriptors.TeletextDescriptor;
import sedec2.dvb.ts.si.descriptors.TerrestrialDeliverySystemDescriptor;
import sedec2.dvb.ts.si.descriptors.TransportProtocolDescriptor;
import sedec2.dvb.ts.si.descriptors.UnknownDescriptor;
import sedec2.dvb.ts.si.descriptors.VideoDecodeControlDescriptor;

/**
 * Factory to obtain a kind of descriptors of DVB like below.
 * <ul>
 * <li> {@link ApplicationDescriptor}
 * <li> {@link ApplicationNameDescriptor}
 * <li> {@link ApplicationRecordingDescriptor}
 * <li> {@link ApplicationUsageDescriptor}
 * <li> {@link ConditionalAccessDescriptor}
 * <li> {@link ConnectionRequirementDescriptor}
 * <li> {@link DataComponentDescriptor}
 * <li> {@link DigitalCopyControlDescriptor}
 * <li> {@link ParentalRatingDescriptor}
 * <li> {@link SimpleApplicationBoundaryDescriptor}
 * <li> {@link SimpleApplicationLocationDescriptor}
 * <li> {@link StreamIdentifierDescriptor}
 * <li> {@link TransportProtocolDescriptor}
 * <li> {@link VideoDecodeControlDescriptor}
 * </ul>
 */
public class DescriptorFactory {
    public final static int APPLICATION_DESCRIPTOR = 0x00;
    public final static int APPLICATION_NAME_DESCRIPTOR = 0x01;
    public final static int TRANSPORT_PROTOCOL_DESCRIPTOR = 0x02;
    public final static int APPLICATION_RECORDING_DESCRIPTOR = 0x06;
    public final static int CONDITIONAL_ACCESS_DESCRIPTOR = 0x09;
    public final static int ISO_639_LANGUAGE_DESCRIPTOR = 0x0a;
    public final static int CAROUSEL_IDENTIFIER_DESCRIPTOR = 0x13;
    public final static int SIMPLE_APPLICATION_LOCATION_DESCRIPTOR = 0x15;
    public final static int APPLICATION_USAGE_DESCRIPTOR = 0x16;
    public final static int SIMPLE_APPLICATION_BOUNDARY_DESCRIPTOR = 0x17;
    public final static int NETWORK_NAME_DESCRIPTOR = 0x40;
    public final static int SERVICE_LIST_DESCRIPTOR = 0x41;
    public final static int SATELITE_DELIVERY_SYSTEM_DESCRIPTOR = 0x43;
    public final static int CABLE_DELIVERY_SYSTEM_DESCRIPTOR = 0x44;
    public final static int LINKAGE_DESCRIPTOR = 0x4a;
    public final static int BOUQUET_NAME_DESCRIPTOR = 0x47;
    public final static int SERVICE_DESCRIPTOR = 0x48;
    public final static int STREAM_IDENTIFIER_DESCRIPTOR = 0x52;
    public final static int PARENTAL_RATING_DESCRIPTOR = 0x55;
    public final static int TELETEXT_DESCRIPTOR = 0x56;
    public final static int SUBTITLING_DESCRIPTOR = 0x59;
    public final static int TERRESTRIAL_DELIVERY_SYSTEM_DESCRIPTOR = 0x5a;
    public final static int FREQUENCY_LIST_DESCRIPTOR = 0x62;
    public final static int DATA_BROADCAST_DESCRIPTOR = 0x64;
    public final static int DATA_BROADCAST_ID_DESCRIPTOR = 0x66;
    public final static int AC3_DESCRIPTOR = 0x6a;
    public final static int CELL_LIST_DESCRIPTOR = 0x6c;
    public final static int APPLICATION_SIGNALLING_DESCRIPTOR = 0x6f;
    public final static int CONNECTION_REQUIREMENT_DESCRIPTOR = 0x72;
    public final static int S2_SATELITE_DELIVERY_SYSTEM_DESCRIPTOR = 0x79;
    public final static int DIGITALCOPY_CONTROL_DESCRIPTOR = 0xc1;
    public final static int VIDEODECODE_CONTROL_DESCRIPTOR = 0xc8;
    public final static int DATA_COMPONENT_DESCRIPTOR = 0xfd;
    public final static int UNKNOWN_DESCRIPTOR = 0xff;

    public static Descriptor createDescriptor(BitReadWriter brw) {
        int descriptor_tag = brw.getCurrentBuffer()[0] & 0x0000ff;

        switch ( descriptor_tag ) {
            case DATA_BROADCAST_DESCRIPTOR:
                return new DataBroadcastDescriptor(brw);
            case AC3_DESCRIPTOR:
                return new AC3Descriptor(brw);
            case SUBTITLING_DESCRIPTOR:
                return new SubtitlingDescriptor(brw);
            case FREQUENCY_LIST_DESCRIPTOR:
                return new FrequencyListDescriptor(brw);
            case CELL_LIST_DESCRIPTOR:
                return new CellListDescriptor(brw);
            case TERRESTRIAL_DELIVERY_SYSTEM_DESCRIPTOR:
                return new TerrestrialDeliverySystemDescriptor(brw);
            case TELETEXT_DESCRIPTOR:
                return new TeletextDescriptor(brw);
            case SERVICE_DESCRIPTOR:
                return new ServiceDescriptor(brw);
            case BOUQUET_NAME_DESCRIPTOR:
                return new BouquetNameDescriptor(brw);
            case CABLE_DELIVERY_SYSTEM_DESCRIPTOR:
                return new CableDeliverySystemDescriptor(brw);
            case S2_SATELITE_DELIVERY_SYSTEM_DESCRIPTOR:
                return new S2SateliteDeliverySystemDescriptor(brw);
            case LINKAGE_DESCRIPTOR:
                return new LinkageDescriptor(brw);
            case SATELITE_DELIVERY_SYSTEM_DESCRIPTOR:
                return new SateliteDeliverySystemDescriptor(brw);
            case SERVICE_LIST_DESCRIPTOR:
                return new ServiceListDescriptor(brw);
            case NETWORK_NAME_DESCRIPTOR:
                return new NetworkNameDescriptor(brw);
            case ISO_639_LANGUAGE_DESCRIPTOR:
                return new ISO639LanguageDescriptor(brw);
            case CAROUSEL_IDENTIFIER_DESCRIPTOR:
                return new CarouselIdentifierDescriptor(brw);
            case APPLICATION_SIGNALLING_DESCRIPTOR:
                return new ApplicationSignallingDescriptor(brw);
            case DATA_BROADCAST_ID_DESCRIPTOR:
                return new DataBroadcastIdDescriptor(brw);
            case DATA_COMPONENT_DESCRIPTOR:
                return new DataComponentDescriptor(brw);
            case VIDEODECODE_CONTROL_DESCRIPTOR:
                return new VideoDecodeControlDescriptor(brw);
            case STREAM_IDENTIFIER_DESCRIPTOR:
                return new StreamIdentifierDescriptor(brw);
            case DIGITALCOPY_CONTROL_DESCRIPTOR:
                return new DigitalCopyControlDescriptor(brw);
            case CONDITIONAL_ACCESS_DESCRIPTOR:
                return new ConditionalAccessDescriptor(brw);
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
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }

    private DescriptorFactory() {

    }
}
