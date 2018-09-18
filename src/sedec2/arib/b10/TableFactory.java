package sedec2.arib.b10;

import sedec2.arib.b10.tables.NetworkBoardInformationTable;
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
    public final static int PROGRAM_ASSOCIATION_TABLE = 0x00;
    
    /** @note CAT */
    public final static int CONDITIONAL_ACCESS_TABLE = 0x01;
    
    /** @note PMT */
    public final static int PROGRAM_MAP_TABLE = 0x02;
    
    /** @note AIT */
    public final static int APPLICATION_INFORMATION_TABLE = 0x74;
    
    /** @note NIT for actual */
    public final static int ACTUAL_NETWORK_INFORMATION_TABLE = 0x40;
    
    /** @note NIT for other */
    public final static int OTHER_NETWORK_INFORMATION_TABLE = 0x41;
    
    /** @note SDT for actual */
    public final static int ACTUAL_SERVICE_DESCRIPTION_TABLE = 0x42;
    
    /** @note SDT for other */
    public final static int OTHER_SERVICE_DESCRIPTION_TABLE = 0x46;
    
    /** @note BAT */
    public final static int BOUQUET_ASSOCIATION_TABLE = 0x4a;
    
    /** @note EIT for actual present */
    public final static int ACTUAL_TS_PRESENT_EVENT_INFORMATION_TABLE = 0x4e;
    
    /** @note EIT for other */
    public final static int OTHER_TS_PRESENT_EVENT_INFORMATION_TABLE = 0x4f;
    
    /** @note TDT */
    public final static int TIME_DATE_TABLE = 0x70;
    
    /** @note RST */
    public final static int RUNNING_STATUS_TABLE = 0x71;
    
    /** @note ST */
    public final static int STUFFING_TABLE = 0x72;
    
    /** @note TOT */
    public final static int TIME_OFFSET_TABLE = 0x73;
    
    /** @note DIT */
    public final static int DISCONTINUITY_INFORMATION_TABLE = 0x7e;
    
    /** @note SIT */
    public final static int SELECTION_INFORMATION_TABLE = 0x7f;
    
    /** @note ECM */
    public final static int ENTITLEMENT_CONTROL_MESSAGE_1 = 0x82;
    public final static int ENTITLEMENT_CONTROL_MESSAGE_2 = 0x83;

    /** @note EMM */
    public final static int ENTITLEMENT_MANAGEMENT_MESSAGE_1 = 0x84;
    public final static int ENTITLEMENT_MANAGEMENT_MESSAGE_2 = 0x85;

    /** @note DCM */
    public final static int DOWNLOAD_CONTROL_TABLE = 0xc0;
    
    /** @note DT */
    public final static int DOWNLOAD_TABLE = 0xc1;
    
    /** @note PCAT */
    public final static int PARTIAL_CONTENT_ANNOUNCEMENT_TABLE = 0xc2;
    
    /** @note BIT */
    public final static int NETWORK_BOARD_INFORMATION_TABLE_1 = 0xc5;
    public final static int NETWORK_BOARD_INFORMATION_TABLE_2 = 0xc6;

    /** @note LDT */
    public final static int LINKED_DESCRIPTION_TABLE = 0xc7;

    /** @note LIT */
    public final static int LOCAL_EVENT_INFORMATION_TABLE = 0xd0;
    
    /** @note ERT */
    public final static int EVENT_RELATION_TABLE = 0xd1;
    
    /** @note ITT */
    public final static int INDEX_TRANSMISSION_TABLE = 0xd2;
    
    /** @note SDTT */
    public final static int SOFTWARE_DOWNLOAD_TRIGGER_TABLE = 0xc3;
    
    /** @note BIT */
    public final static int BROADCASTER_INFORMATION_TABLE = 0xc4;
    
    public final static int UNKNOWN_TABLE = 0xff;
    
    public static Table CreateTable(byte[] buffer) {
        int table_id = (buffer[0] & 0xff);
        
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
