package sedec2.dvb;

import sedec2.dvb.tables.BouquetAssociationTable;
import sedec2.dvb.tables.DiscontinuityInformationTable;
import sedec2.dvb.tables.EventInformationTable;
import sedec2.dvb.tables.NetworkInformationTable;
import sedec2.dvb.tables.RunningStatusTable;
import sedec2.dvb.tables.SelectionInformationTable;
import sedec2.dvb.tables.ServiceDescriptionTable;
import sedec2.dvb.tables.StuffingTable;
import sedec2.dvb.tables.TimeDateTable;
import sedec2.dvb.tables.TimeOffsetTable;
import sedec2.base.Table;
import sedec2.dvb.tables.ApplicationInformationTable;
import sedec2.dvb.tables.ConditionalAccessTable;
import sedec2.dvb.tables.ProgramAssociationTable;
import sedec2.dvb.tables.ProgramMapTable;

public class TableFactory {
    public final static int PROGRAM_ASSOCIATION_TABLE = 0x00;
    public final static int CONDITIONAL_ACCESS_TABLE = 0x01;
    public final static int PROGRAM_MAP_TABLE = 0x02;
    public final static int APPLICATION_INFORMATION_TABLE = 0x74;
    public final static int ACTUAL_NETWORK_INFORMATION_TABLE = 0x40;
    public final static int OTHER_NETWORK_INFORMATION_TABLE = 0x41;
    public final static int ACTUAL_SERVICE_DESCRIPTON_TABLE = 0x42;
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
    public final static int UNKNOWN_TABLE = 0xff;
    
    public static Table createTable(byte[] buffer) {
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
            case ACTUAL_SERVICE_DESCRIPTON_TABLE:
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
