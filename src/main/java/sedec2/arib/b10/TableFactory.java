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
import sedec2.arib.b10.tables.dsmcc.DSMCCSection;
import sedec2.base.Table;
import sedec2.base.UnknownTable;


/**
 * Factory to obtain a kind of table of ARIB B10 like below.
 * <ul>
 * <li> {@link ApplicationInformationTable}
 * <li> {@link BouquetAssociationTable}
 * <li> {@link BroadcasterInformationTable}
 * <li> {@link ConditionalAccessTable}
 * <li> {@link DiscontinuityInformationTable}
 * <li> {@link DownloadControlMessage}
 * <li> {@link DownloadTable}
 * <li> {@link EntitlementControlMessage}
 * <li> {@link EntitlementManagementMessage}
 * <li> {@link EventInformationTable}
 * <li> {@link EventRelationTable}
 * <li> {@link IndexTransmissionTable}
 * <li> {@link LinkedDescriptionTable}
 * <li> {@link LocalEventInformationTable}
 * <li> {@link NetworkBoardInformationTable}
 * <li> {@link NetworkInformationTable}
 * <li> {@link PartialContentAnnouncementTable}
 * <li> {@link ProgramAssociationTable}
 * <li> {@link ProgramMapTable}
 * <li> {@link RunningStatusTable}
 * <li> {@link SelectionInformationTable}
 * <li> {@link ServiceDescriptionTable}
 * <li> {@link SoftwareDownloadTriggerTable}
 * <li> {@link StuffingTable}
 * <li> {@link TimeDateTable}
 * <li> {@link TimeOffsetTable}
 * </ul>
 */
public class TableFactory {
    public final static byte PROGRAM_ASSOCIATION_TABLE = (byte) 0x00;
    public final static byte PAT = PROGRAM_ASSOCIATION_TABLE;

    public final static byte CONDITIONAL_ACCESS_TABLE = (byte) 0x01;
    public final static byte CAT = CONDITIONAL_ACCESS_TABLE;

    public final static byte PROGRAM_MAP_TABLE = (byte) 0x02;
    public final static byte PMT = PROGRAM_MAP_TABLE;

    public final static byte ACTUAL_NETWORK_INFORMATION_TABLE = (byte) 0x40;
    public final static byte NIT_ACTUAL = ACTUAL_NETWORK_INFORMATION_TABLE;

    public final static byte OTHER_NETWORK_INFORMATION_TABLE = (byte) 0x41;
    public final static byte NIT_OTHER = OTHER_NETWORK_INFORMATION_TABLE;

    public final static byte ACTUAL_SERVICE_DESCRIPTION_TABLE = (byte) 0x42;
    public final static byte SDT_ACTUAL = ACTUAL_SERVICE_DESCRIPTION_TABLE;

    public final static byte OTHER_SERVICE_DESCRIPTION_TABLE = (byte) 0x46;
    public final static byte SDT_OTHER = OTHER_SERVICE_DESCRIPTION_TABLE;

    public final static byte BOUQUET_ASSOCIATION_TABLE = (byte) 0x4a;
    public final static byte BAT = BOUQUET_ASSOCIATION_TABLE;

    public final static byte ACTUAL_TS_PRESENT_EVENT_INFORMATION_TABLE = (byte) 0x4e;
    public final static byte EIT_ACTUAL = ACTUAL_TS_PRESENT_EVENT_INFORMATION_TABLE;

    public final static byte OTHER_TS_PRESENT_EVENT_INFORMATION_TABLE = (byte) 0x4f;
    public final static byte EIT_OTHER = OTHER_TS_PRESENT_EVENT_INFORMATION_TABLE;

    public final static byte TIME_DATE_TABLE = (byte) 0x70;
    public final static byte TDT = TIME_DATE_TABLE;

    public final static byte RUNNING_STATUS_TABLE = (byte) 0x71;
    public final static byte RST = RUNNING_STATUS_TABLE;

    public final static byte STUFFING_TABLE = (byte) 0x72;
    public final static byte ST = STUFFING_TABLE;

    public final static byte TIME_OFFSET_TABLE = (byte) 0x73;
    public final static byte TOT = TIME_OFFSET_TABLE;

    public final static byte APPLICATION_INFORMATION_TABLE = (byte) 0x74;
    public final static byte AIT = APPLICATION_INFORMATION_TABLE;

    public final static byte DISCONTINUITY_INFORMATION_TABLE = (byte) 0x7e;
    public final static byte DIT = DISCONTINUITY_INFORMATION_TABLE;

    public final static byte SELECTION_INFORMATION_TABLE = (byte) 0x7f;
    public final static byte SIT = SELECTION_INFORMATION_TABLE;

    public final static byte ENTITLEMENT_CONTROL_MESSAGE_1 = (byte) 0x82;
    public final static byte ECM_1 = ENTITLEMENT_CONTROL_MESSAGE_1;

    public final static byte ENTITLEMENT_CONTROL_MESSAGE_2 = (byte) 0x83;
    public final static byte ECM_2 = ENTITLEMENT_CONTROL_MESSAGE_2;

    public final static byte ENTITLEMENT_MANAGEMENT_MESSAGE_1 = (byte) 0x84;
    public final static byte EMM_1 = ENTITLEMENT_MANAGEMENT_MESSAGE_1;

    public final static byte ENTITLEMENT_MANAGEMENT_MESSAGE_2 = (byte) 0x85;
    public final static byte EMM_2 = ENTITLEMENT_MANAGEMENT_MESSAGE_2;

    public final static byte DOWNLOAD_CONTROL_TABLE = (byte) 0xc0;
    public final static byte DCT = DOWNLOAD_CONTROL_TABLE;

    public final static byte DOWNLOAD_TABLE = (byte) 0xc1;
    public final static byte DT = DOWNLOAD_TABLE;

    public final static byte PARTIAL_CONTENT_ANNOUNCEMENT_TABLE = (byte) 0xc2;
    public final static byte PCAT = PARTIAL_CONTENT_ANNOUNCEMENT_TABLE;

    public final static byte NETWORK_BOARD_INFORMATION_TABLE_1 = (byte) 0xc5;
    public final static byte NBIT_1 = NETWORK_BOARD_INFORMATION_TABLE_1;

    public final static byte NETWORK_BOARD_INFORMATION_TABLE_2 = (byte) 0xc6;
    public final static byte NBIT_2 = NETWORK_BOARD_INFORMATION_TABLE_2;

    public final static byte LINKED_DESCRIPTION_TABLE = (byte) 0xc7;
    public final static byte LDT = LINKED_DESCRIPTION_TABLE;

    public final static byte LOCAL_EVENT_INFORMATION_TABLE = (byte) 0xd0;
    public final static byte LEIT = LOCAL_EVENT_INFORMATION_TABLE;

    public final static byte EVENT_RELATION_TABLE = (byte) 0xd1;
    public final static byte ERT = EVENT_RELATION_TABLE;

    public final static byte INDEX_TRANSMISSION_TABLE = (byte) 0xd2;
    public final static byte ITT = INDEX_TRANSMISSION_TABLE;

    public final static byte SOFTWARE_DOWNLOAD_TRIGGER_TABLE = (byte) 0xc3;
    public final static byte SDTT = SOFTWARE_DOWNLOAD_TRIGGER_TABLE;

    public final static byte BROADCASTER_INFORMATION_TABLE = (byte) 0xc4;
    public final static byte BIT = BROADCASTER_INFORMATION_TABLE;

    public final static byte UNKNOWN_TABLE = (byte) 0xff;

    /**
     * Creates specific table of a kind of tables in ARIB B10
     * @param buffer one table section
     * @return one table which is decoded
     */
    public static Table createTable(byte[] buffer) {
        byte table_id = (byte)(buffer[0] & 0xff);

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
                return new UnknownTable(buffer);
        }
    }

    private TableFactory() {
        /**
         * @warning Nothing to do since this factory isn't working as instance
         */
    }
}
