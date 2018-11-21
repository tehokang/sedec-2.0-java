package sedec2.arib.b10;

import sedec2.arib.b10.tables.ApplicationInformationTable;
import sedec2.arib.b10.tables.BouquetAssociationTable;
import sedec2.arib.b10.tables.BroadcasterInformationTable;
import sedec2.arib.b10.tables.ConditionalAccessTable;
import sedec2.arib.b10.tables.DiscontinuityInformationTable;
import sedec2.arib.b10.tables.DownloadControlMessage;
import sedec2.arib.b10.tables.DownloadTable;
import sedec2.arib.b10.tables.EntitlementControlMessage;
import sedec2.arib.b10.tables.EntitlementManagementMessage;
import sedec2.arib.b10.tables.EventInformationTable;
import sedec2.arib.b10.tables.EventRelationTable;
import sedec2.arib.b10.tables.IndexTransmissionTable;
import sedec2.arib.b10.tables.LinkedDescriptionTable;
import sedec2.arib.b10.tables.LocalEventInformationTable;
import sedec2.arib.b10.tables.NetworkBoardInformationTable;
import sedec2.arib.b10.tables.NetworkInformationTable;
import sedec2.arib.b10.tables.PartialContentAnnouncementTable;
import sedec2.arib.b10.tables.ProgramAssociationTable;
import sedec2.arib.b10.tables.ProgramMapTable;
import sedec2.arib.b10.tables.RunningStatusTable;
import sedec2.arib.b10.tables.SelectionInformationTable;
import sedec2.arib.b10.tables.ServiceDescriptionTable;
import sedec2.arib.b10.tables.SoftwareDownloadTriggerTable;
import sedec2.arib.b10.tables.StuffingTable;
import sedec2.arib.b10.tables.TimeDateTable;
import sedec2.arib.b10.tables.TimeOffsetTable;
import sedec2.base.Table;

public class TableFactory {
    /** @note PAT */
    public final static byte PROGRAM_ASSOCIATION_TABLE = (byte) 0x00;

    /** @note CAT */
    public final static byte CONDITIONAL_ACCESS_TABLE = (byte) 0x01;

    /** @note PMT */
    public final static byte PROGRAM_MAP_TABLE = (byte) 0x02;

    /** @note NIT for actual */
    public final static byte ACTUAL_NETWORK_INFORMATION_TABLE = (byte) 0x40;

    /** @note NIT for other */
    public final static byte OTHER_NETWORK_INFORMATION_TABLE = (byte) 0x41;

    /** @note SDT for actual */
    public final static byte ACTUAL_SERVICE_DESCRIPTION_TABLE = (byte) 0x42;

    /** @note SDT for other */
    public final static byte OTHER_SERVICE_DESCRIPTION_TABLE = (byte) 0x46;

    /** @note BAT */
    public final static byte BOUQUET_ASSOCIATION_TABLE = (byte) 0x4a;

    /** @note EIT for actual present */
    public final static byte ACTUAL_TS_PRESENT_EVENT_INFORMATION_TABLE = (byte) 0x4e;

    /** @note EIT for other */
    public final static byte OTHER_TS_PRESENT_EVENT_INFORMATION_TABLE = (byte) 0x4f;

    /** @note TDT */
    public final static byte TIME_DATE_TABLE = (byte) 0x70;

    /** @note RST */
    public final static byte RUNNING_STATUS_TABLE = (byte) 0x71;

    /** @note ST */
    public final static byte STUFFING_TABLE = (byte) 0x72;

    /** @note TOT */
    public final static byte TIME_OFFSET_TABLE = (byte) 0x73;

    /** @note AIT */
    public final static byte APPLICATION_INFORMATION_TABLE = (byte) 0x74;

    /** @note DIT */
    public final static byte DISCONTINUITY_INFORMATION_TABLE = (byte) 0x7e;

    /** @note SIT */
    public final static byte SELECTION_INFORMATION_TABLE = (byte) 0x7f;

    /** @note ECM */
    public final static byte ENTITLEMENT_CONTROL_MESSAGE_1 = (byte) 0x82;
    public final static byte ENTITLEMENT_CONTROL_MESSAGE_2 = (byte) 0x83;

    /** @note EMM */
    public final static byte ENTITLEMENT_MANAGEMENT_MESSAGE_1 = (byte) 0x84;
    public final static byte ENTITLEMENT_MANAGEMENT_MESSAGE_2 = (byte) 0x85;

    /** @note DCM */
    public final static byte DOWNLOAD_CONTROL_TABLE = (byte) 0xc0;

    /** @note DT */
    public final static byte DOWNLOAD_TABLE = (byte) 0xc1;

    /** @note PCAT */
    public final static byte PARTIAL_CONTENT_ANNOUNCEMENT_TABLE = (byte) 0xc2;

    /** @note BIT */
    public final static byte NETWORK_BOARD_INFORMATION_TABLE_1 = (byte) 0xc5;
    public final static byte NETWORK_BOARD_INFORMATION_TABLE_2 = (byte) 0xc6;

    /** @note LDT */
    public final static byte LINKED_DESCRIPTION_TABLE = (byte) 0xc7;

    /** @note LIT */
    public final static byte LOCAL_EVENT_INFORMATION_TABLE = (byte) 0xd0;

    /** @note ERT */
    public final static byte EVENT_RELATION_TABLE = (byte) 0xd1;

    /** @note ITT */
    public final static byte INDEX_TRANSMISSION_TABLE = (byte) 0xd2;

    /** @note SDTT */
    public final static byte SOFTWARE_DOWNLOAD_TRIGGER_TABLE = (byte) 0xc3;

    /** @note BIT */
    public final static byte BROADCASTER_INFORMATION_TABLE = (byte) 0xc4;

    public final static byte UNKNOWN_TABLE = (byte) 0xff;

    public static Table createTable(byte[] buffer) {
        byte table_id = (byte)(buffer[0] & 0xff);

        Table section = null;

        switch ( table_id )
        {
            case PROGRAM_ASSOCIATION_TABLE:
                return new ProgramAssociationTable(buffer);
            case CONDITIONAL_ACCESS_TABLE:
                return new ConditionalAccessTable(buffer);
            case PROGRAM_MAP_TABLE:
                return new ProgramMapTable(buffer);
            case BOUQUET_ASSOCIATION_TABLE:
                return new BouquetAssociationTable(buffer);
            case ACTUAL_NETWORK_INFORMATION_TABLE:
            case OTHER_NETWORK_INFORMATION_TABLE:
                return new NetworkInformationTable(buffer);
            case ACTUAL_SERVICE_DESCRIPTION_TABLE:
            case OTHER_SERVICE_DESCRIPTION_TABLE:
                return new ServiceDescriptionTable(buffer);
            case ACTUAL_TS_PRESENT_EVENT_INFORMATION_TABLE:
            case OTHER_TS_PRESENT_EVENT_INFORMATION_TABLE:
                return new EventInformationTable(buffer);
            case TIME_DATE_TABLE:
                return new TimeDateTable(buffer);
            case RUNNING_STATUS_TABLE:
                return new RunningStatusTable(buffer);
            case STUFFING_TABLE:
                return new StuffingTable(buffer);
            case TIME_OFFSET_TABLE:
                return new TimeOffsetTable(buffer);
            case DISCONTINUITY_INFORMATION_TABLE:
                return new DiscontinuityInformationTable(buffer);
            case SELECTION_INFORMATION_TABLE:
                return new SelectionInformationTable(buffer);
            case ENTITLEMENT_CONTROL_MESSAGE_1:
            case ENTITLEMENT_CONTROL_MESSAGE_2:
                return new EntitlementControlMessage(buffer);
            case ENTITLEMENT_MANAGEMENT_MESSAGE_1:
            case ENTITLEMENT_MANAGEMENT_MESSAGE_2:
                return new EntitlementManagementMessage(buffer);
            case DOWNLOAD_CONTROL_TABLE:
                return new DownloadControlMessage(buffer);
            case DOWNLOAD_TABLE:
                return new DownloadTable(buffer);
            case BROADCASTER_INFORMATION_TABLE:
                return new BroadcasterInformationTable(buffer);
            case PARTIAL_CONTENT_ANNOUNCEMENT_TABLE:
                return new PartialContentAnnouncementTable(buffer);
            case NETWORK_BOARD_INFORMATION_TABLE_1:
            case NETWORK_BOARD_INFORMATION_TABLE_2:
                return new NetworkBoardInformationTable(buffer);
            case LINKED_DESCRIPTION_TABLE:
                return new LinkedDescriptionTable(buffer);
            case LOCAL_EVENT_INFORMATION_TABLE:
                return new LocalEventInformationTable(buffer);
            case EVENT_RELATION_TABLE:
                return new EventRelationTable(buffer);
            case INDEX_TRANSMISSION_TABLE:
                return new IndexTransmissionTable(buffer);
            case SOFTWARE_DOWNLOAD_TRIGGER_TABLE:
                return new SoftwareDownloadTriggerTable(buffer);
            case APPLICATION_INFORMATION_TABLE:
                return new ApplicationInformationTable(buffer);
            default:
                break;
        }
        return section;
    }

    private TableFactory() {
        /**
         * @warning Nothing to do since this factory isn't working as instance
         */
    }
}
