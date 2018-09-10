package arib.b10;

import arib.b10.tables.BouquetAssociationTable;
import arib.b10.tables.BroadcasterInformationTable;
import arib.b10.tables.ConditionalAccessTable;
import arib.b10.tables.DiscontinuityInformationTable;
import arib.b10.tables.EventInformationTable;
import arib.b10.tables.NetworkInformationTable;
import arib.b10.tables.ProgramAssociationTable;
import arib.b10.tables.ProgramMapTable;
import arib.b10.tables.RunningStatusTable;
import arib.b10.tables.SelectionInformationTable;
import arib.b10.tables.ServiceDescriptionTable;
import arib.b10.tables.StuffingTable;
import arib.b10.tables.TimeDateTable;
import arib.b10.tables.TimeOffsetTable;
import base.Table;

public class TableFactory {
    public final static int PROGRAM_ASSOCIATION_TABLE = 0x00;
    public final static int CONDITIONAL_ACCESS_TABLE = 0x01;
    public final static int PROGRAM_MAP_TABLE = 0x02;
    public final static int APPLICATION_INFORMATION_TABLE = 0x74;
    public final static int ACTUAL_NETWORK_INFORMATION_TABLE = 0x40;
    public final static int OTHER_NETWORK_INFORMATION_TABLE = 0x41;
    public final static int ACTUAL_SERVICE_DESCRIPTION_TABLE = 0x42;
    public final static int OTHER_SERVICE_DESCRIPTION_TABLE = 0x46;
    public final static int BOUQUET_ASSOCIATION_TABLE = 0x4a;
    public final static int ACTUAL_TS_PRESENT_EVENT_INFORMATION_TABLE = 0x4e;
    public final static int OTHER_TS_PRESENT_EVENT_INFORMATION_TABLE = 0x4f;
    public final static int TIME_DATE_TABLE = 0x70;
    public final static int RUNNING_STATUS_TABLE = 0x71;
    public final static int STUFFING_TABLE = 0x72;
    public final static int TIME_OFFSET_TABLE = 0x73;
    public final static int DISCONTINUITY_INFORMATION_TABLE = 0x7e;
    public final static int SELECTION_INFORMATION_TABLE = 0x7f;
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
            case BROADCASTER_INFORMATION_TABLE:
                return new BroadcasterInformationTable(buffer);
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
