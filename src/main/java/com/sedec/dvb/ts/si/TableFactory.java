package com.sedec.dvb.ts.si;

import com.sedec.base.Table;
import com.sedec.base.UnknownTable;
import com.sedec.dvb.ts.si.tables.ApplicationInformationTable;
import com.sedec.dvb.ts.si.tables.BouquetAssociationTable;
import com.sedec.dvb.ts.si.tables.ConditionalAccessTable;
import com.sedec.dvb.ts.si.tables.DiscontinuityInformationTable;
import com.sedec.dvb.ts.si.tables.EventInformationTable;
import com.sedec.dvb.ts.si.tables.NetworkInformationTable;
import com.sedec.dvb.ts.si.tables.ProgramAssociationTable;
import com.sedec.dvb.ts.si.tables.ProgramMapTable;
import com.sedec.dvb.ts.si.tables.RunningStatusTable;
import com.sedec.dvb.ts.si.tables.SelectionInformationTable;
import com.sedec.dvb.ts.si.tables.ServiceDescriptionTable;
import com.sedec.dvb.ts.si.tables.StuffingTable;
import com.sedec.dvb.ts.si.tables.TimeDateTable;
import com.sedec.dvb.ts.si.tables.TimeOffsetTable;
import com.sedec.dvb.ts.si.tables.dsmcc.DSMCCSection;

/**
 * Factory to obtain a kind of table of DVB like below.
 * <ul>
 * <li> {@link DSMCCSection}
 * <li> {@link ApplicationInformationTable}
 * <li> {@link BouquetAssociationTable}
 * <li> {@link ConditionalAccessTable}
 * <li> {@link DiscontinuityInformationTable}
 * <li> {@link EventInformationTable}
 * <li> {@link NetworkInformationTable}
 * <li> {@link ProgramAssociationTable}
 * <li> {@link ProgramMapTable}
 * <li> {@link RunningStatusTable}
 * <li> {@link SelectionInformationTable}
 * <li> {@link ServiceDescriptionTable}
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

    public final static byte DSMCC_UN_MESSAGE_TABLE = 0x3b;
    public final static byte DSMCC_DOWNLOAD_DATA_MESSAGE_TABLE = 0x3c;
    public final static byte DSMCC_STREAM_DESCRIPTORS_TABLE = 0x3d;
    public final static byte DSMCC_PRIVATE_DATA_TABLE = 0x3e;

    public final static byte APPLICATION_INFORMATION_TABLE = (byte) 0x74;
    public final static byte AIT = APPLICATION_INFORMATION_TABLE;

    public final static byte ACTUAL_NETWORK_INFORMATION_TABLE = (byte) 0x40;
    public final static byte NIT_ACTUAL = ACTUAL_NETWORK_INFORMATION_TABLE;

    public final static byte OTHER_NETWORK_INFORMATION_TABLE = (byte) 0x41;
    public final static byte NIT_OTHER = OTHER_NETWORK_INFORMATION_TABLE;

    public final static byte ACTUAL_SERVICE_DESCRIPTON_TABLE = (byte) 0x42;
    public final static byte SDT_ACTUAL = ACTUAL_SERVICE_DESCRIPTON_TABLE;

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

    public final static byte DISCONTINUITY_INFORMATION_TABLE = (byte) 0x7e;
    public final static byte DIT = DISCONTINUITY_INFORMATION_TABLE;

    public final static byte SELECTION_INFORMATION_TABLE = (byte) 0x7f;
    public final static byte SIT = SELECTION_INFORMATION_TABLE;

    public final static byte UNKNOWN_TABLE = (byte) 0xff;

    public static Table createTable(byte[] buffer) {
        byte table_id = (byte)(buffer[0] & 0xff);

        switch(table_id) {
            case DSMCC_UN_MESSAGE_TABLE:
            case DSMCC_DOWNLOAD_DATA_MESSAGE_TABLE:
            case DSMCC_STREAM_DESCRIPTORS_TABLE:
            case DSMCC_PRIVATE_DATA_TABLE:
                return new DSMCCSection(buffer);
            case PROGRAM_ASSOCIATION_TABLE:
                return new ProgramAssociationTable(buffer);
            case CONDITIONAL_ACCESS_TABLE:
                return new ConditionalAccessTable(buffer);
            case PROGRAM_MAP_TABLE:
                return new ProgramMapTable(buffer);
            /**
             * It's related in ETSI TS 102 809 v1.4.1
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
