package dvb;

import dvb.tables.BouquetAssociationTable;
import dvb.tables.DiscontinuityInformationTable;
import dvb.tables.EventInformationTable;
import dvb.tables.NetworkInformationTable;
import dvb.tables.RunningStatusTable;
import dvb.tables.SelectionInformationTable;
import dvb.tables.ServiceDescriptionTable;
import dvb.tables.StuffingTable;
import dvb.tables.TimeDateTable;
import dvb.tables.TimeOffsetTable;
import base.Table;
import dvb.tables.ApplicationInformationTable;
import dvb.tables.ConditionalAccessTable;
import dvb.tables.ProgramAssociationTable;
import dvb.tables.ProgramMapTable;

public class TableFactory {
    public final static int PROGRAM_ASSOCIATION_TABLE = 0x00;
    public final static int CONDITIONAL_ACCESS_TABLE = 0x01;
    public final static int PROGRAM_MAP_TABLE = 0x02;
    public final static int APPLICATION_INFORMATION_TABLE = 0x74;
    public final static int ACTUAL_NETWORK_INFORMATION_TABLE = 0x40;
    public final static int OTHER_NETWORK_INFORMATION_TABLE = 0x41;
    public final static int SERVICE_DESCRIPTION_TABLE = 0x46;
    public final static int BOUQUET_ASSOCIATION_TABLE = 0x4a;
    public final static int ACTUAL_TS_PRESENT_EVENT_INFORMATION_TABLE = 0x4e;
    public final static int OTHER_TS_PRESENT_EVENT_INFORMATION_TABLE = 0x4f;
    public final static int TIME_DATE_TABLE = 0x70;
    public final static int RUNNING_STATUS_TABLE = 0x71;
    public final static int STUFFING_TABLE = 0x72;
    public final static int TIME_OFFSET_TABLE = 0x73;
    public final static int DISCONTINUITY_INFORMATION_TABLE = 0x7e;
    public final static int SELECTION_INFORMATION_TABLE = 0x7f;
    public final static int UNKNOWN_TABLE = 0xff;
    
    public static Table CreateTable(byte[] buffer) {
        int table_id = (buffer[0] & 0xff);
        
        Table section = null;

        switch(table_id)
        {
            case PROGRAM_ASSOCIATION_TABLE:
                return new ProgramAssociationTable(buffer);
            case CONDITIONAL_ACCESS_TABLE:
                return new ConditionalAccessTable(buffer);
            case PROGRAM_MAP_TABLE:
                return new ProgramMapTable(buffer);
            /**
             * @note It is related in ETSI TS 102 809 v1.4.1
             **/
            case APPLICATION_INFORMATION_TABLE:
                return new ApplicationInformationTable(buffer);
            case BOUQUET_ASSOCIATION_TABLE:
                return new BouquetAssociationTable(buffer);
            case ACTUAL_NETWORK_INFORMATION_TABLE:
            case OTHER_NETWORK_INFORMATION_TABLE:
                return new NetworkInformationTable(buffer);
            case SERVICE_DESCRIPTION_TABLE:
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
