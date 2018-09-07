package arib.b10;

import arib.b10.tables.BouquetAssociationTable;
import arib.b10.tables.ConditionalAccessTable;
import arib.b10.tables.NetworkInformationTable;
import arib.b10.tables.ProgramAssociationTable;
import arib.b10.tables.ProgramMapTable;
import base.Table;

public class TableFactory {
    public final static int PROGRAM_ASSOCIATION_TABLE = 0x00;
    public final static int CONDITIONAL_ACCESS_TABLE = 0x01;
    public final static int PROGRAM_MAP_TABLE = 0x02;
    public final static int APPLICATION_INFORMATION_TABLE = 0x74;
    public final static int ACTUAL_NETWORK_INFORMATION_TABLE = 0x40;
    public final static int OTHER_NETWORK_INFORMATION_TABLE = 0x41;
    public final static int BOUQUET_ASSOCIATION_TABLE = 0x4a;
    public final static int ACTUAL_TS_EVENT_INFORMATION_TABLE = 0x4e;
    public final static int OTHER_TS_EVENT_INFORMATION_TABLE = 0x4f;
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
            case BOUQUET_ASSOCIATION_TABLE:
                return new BouquetAssociationTable(buffer);
            case ACTUAL_NETWORK_INFORMATION_TABLE:
            case OTHER_NETWORK_INFORMATION_TABLE:
                return new NetworkInformationTable(buffer);
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
