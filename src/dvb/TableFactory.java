package dvb;

import base.Table;
import dvb.tables.ApplicationInformationTable;

public class TableFactory {
    public final static int APPLICATION_INFORMATION_TABLE = 0x74;
    public final static int UNKNOWN_TABLE = 0xff;
    
    public static Table CreateTable(byte[] buffer) {
        int table_id = (buffer[0] & 0xff);
        
        Table section = null;

        switch(table_id)
        {
            /**
             * @note It is related in ETSI TS 102 809 v1.4.1
             **/
            case APPLICATION_INFORMATION_TABLE:
                section = new ApplicationInformationTable(buffer);
                break;
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
