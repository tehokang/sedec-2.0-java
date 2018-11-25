package sedec2.dvb;

import sedec2.base.Table;
import sedec2.base.UnknownTable;
import sedec2.dvb.tables.ApplicationInformationTable;
import sedec2.dvb.tables.BouquetAssociationTable;
import sedec2.dvb.tables.ConditionalAccessTable;
import sedec2.dvb.tables.DiscontinuityInformationTable;
import sedec2.dvb.tables.EventInformationTable;
import sedec2.dvb.tables.NetworkInformationTable;
import sedec2.dvb.tables.ProgramAssociationTable;
import sedec2.dvb.tables.ProgramMapTable;
import sedec2.dvb.tables.RunningStatusTable;
import sedec2.dvb.tables.SelectionInformationTable;
import sedec2.dvb.tables.ServiceDescriptionTable;
import sedec2.dvb.tables.StuffingTable;
import sedec2.dvb.tables.TimeDateTable;
import sedec2.dvb.tables.TimeOffsetTable;

public class TableFactory {
    public final static byte PROGRAM_ASSOCIATION_TABLE = (byte) 0x00;
    public final static byte CONDITIONAL_ACCESS_TABLE = (byte) 0x01;
    public final static byte PROGRAM_MAP_TABLE = (byte) 0x02;
    public final static byte APPLICATION_INFORMATION_TABLE = (byte) 0x74;
    public final static byte ACTUAL_NETWORK_INFORMATION_TABLE = (byte) 0x40;
    public final static byte OTHER_NETWORK_INFORMATION_TABLE = (byte) 0x41;
    public final static byte ACTUAL_SERVICE_DESCRIPTON_TABLE = (byte) 0x42;
    public final static byte OTHER_SERVICE_DESCRIPTION_TABLE = (byte) 0x46;
    public final static byte BOUQUET_ASSOCIATION_TABLE = (byte) 0x4a;
    public final static byte ACTUAL_TS_PRESENT_EVENT_INFORMATION_TABLE = (byte) 0x4e;
    public final static byte OTHER_TS_PRESENT_EVENT_INFORMATION_TABLE = (byte) 0x4f;
    public final static byte TIME_DATE_TABLE = (byte) 0x70;
    public final static byte RUNNING_STATUS_TABLE = (byte) 0x71;
    public final static byte STUFFING_TABLE = (byte) 0x72;
    public final static byte TIME_OFFSET_TABLE = (byte) 0x73;
    public final static byte DISCONTINUITY_INFORMATION_TABLE = (byte) 0x7e;
    public final static byte SELECTION_INFORMATION_TABLE = (byte) 0x7f;
    public final static byte UNKNOWN_TABLE = (byte) 0xff;

    public static Table createTable(byte[] buffer) {
        byte table_id = (byte)(buffer[0] & 0xff);

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
                return new UnknownTable(buffer);
        }
    }

    private TableFactory() {
        /**
         * @warning Nothing to do since this factory isn't working as instance
         */
    }
}
