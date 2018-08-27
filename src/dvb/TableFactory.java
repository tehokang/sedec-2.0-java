package dvb;

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
