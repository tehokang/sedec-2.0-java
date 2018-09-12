package arib.b39;

import base.Table;
import base.UnknownTable;
import arib.b39.tables.AddressMapTable;
import arib.b39.tables.LayoutConfigurationTable;
import arib.b39.tables.MPEGH_ApplicationInformationTable;
import arib.b39.tables.MPEGH_CommonDataTable;
import arib.b39.tables.MPEGH_EventInformationTable;
import arib.b39.tables.MPEGH_ServiceDescriptionTable;
import arib.b39.tables.MPEGH_TimeOffsetTable;
import arib.b39.tables.TLV_NetworkInformationTable;

public class TableFactory {
    /** TLV-NIT */
    public final static int ACTUAL_TLV_NETWORK_INFORMATION_TABLE = 0x40;
    public final static int OTHER_TLV_NETWORK_INFORMATION_TABLE = 0x41;
    
    /** MH-SDT */
    public final static int ACTUAL_MPEGH_SERVICE_DESCRIPTION_TABLE = 0x9f;
    public final static int OTHER_MPEGH_SERVICE_DESCRIPTION_TABLE = 0xa0;
    
    /** MH-AIT */
    public final static int MPEGH_APPLICATION_INFORMATION_TABLE = 0x9c;
    public final static int UNKNOWN_TABLE = 0xff;
    
    /** MH-CDT */
    public final static int MPEGH_COMMON_DATA_TABLE = 0xa2;
    
    /** MH-TOT */
    public final static int MPEGH_TIME_OFFSET_TABLE = 0xa1;
    
    /** LCT */
    public final static int LAYOUT_CONFIGURATION_TABLE = 0x81;
    
    /** AMT */
    public final static int ADDRESS_MAP_TABLE = 0xfe;
    
    /** MH-EIT 0x8b, 0x8c~0x9b */
    public final static int ACTUAL_MPEGH_EVENT_INFORMATION_TABLE = 0x8b;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_1 = 0x8c;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_2 = 0x8d;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_3 = 0x8e;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_4 = 0x8f;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_5 = 0x90;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_6 = 0x91;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_7 = 0x92;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_8 = 0x93;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_9 = 0x94;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_10 = 0x95;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_11 = 0x96;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_12 = 0x97;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_13 = 0x98;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_14 = 0x99;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_15 = 0x9a;
    public final static int AUTO_MPEGH_EVENT_INFORMATION_TABLE_16 = 0x9b;
    
    /**
     * @todo
     *  - CRI 0x21
     *  - DCI 0x22
     *  - PLT 0x80
     *  - ECM 0x82 0x83
     *  - EMM 0x84 0x85
     *  - CAT 0x86
     *  - DCM 0x87 0x88
     *  - DMM 0x89 0x8a
     *  - MH-BIT 0x9d
     *  - MH-SDTT 0x9e
     *  - DDM 0xa3
     *  - DAM 0xa4
     *  - DCC 0xa5
     *  - EMT 0xa6
     *  - TLV-NIT 0x40 0x41
     */
    public static Table CreateTable(byte[] buffer) {
        int table_id = (buffer[0] & 0xff);
        
        switch(table_id)
        {
            case ACTUAL_TLV_NETWORK_INFORMATION_TABLE:
            case OTHER_TLV_NETWORK_INFORMATION_TABLE:
                return new TLV_NetworkInformationTable(buffer);
            case ACTUAL_MPEGH_SERVICE_DESCRIPTION_TABLE:
            case OTHER_MPEGH_SERVICE_DESCRIPTION_TABLE:
                return new MPEGH_ServiceDescriptionTable(buffer);
            case MPEGH_APPLICATION_INFORMATION_TABLE:
                return new MPEGH_ApplicationInformationTable(buffer);
            case MPEGH_COMMON_DATA_TABLE:
                return new MPEGH_CommonDataTable(buffer);
            case MPEGH_TIME_OFFSET_TABLE:
                return new MPEGH_TimeOffsetTable(buffer);
            case LAYOUT_CONFIGURATION_TABLE:
                return new LayoutConfigurationTable(buffer);
            case ACTUAL_MPEGH_EVENT_INFORMATION_TABLE:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_1:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_2:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_3:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_4:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_5:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_6:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_7:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_8:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_9:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_10:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_11:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_12:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_13:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_14:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_15:
            case AUTO_MPEGH_EVENT_INFORMATION_TABLE_16:
                return new MPEGH_EventInformationTable(buffer);
            case ADDRESS_MAP_TABLE:
                return new AddressMapTable(buffer);
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
