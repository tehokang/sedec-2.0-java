package sedec2.arib.tlv.si;

import sedec2.arib.tlv.si.tables.AddressMapTable;
import sedec2.arib.tlv.si.tables.TLV_NetworkInformationTable;
import sedec2.base.Table;
import sedec2.base.UnknownTable;

public class TableFactory {
    /** TLV-NIT */
    public final static int ACTUAL_TLV_NETWORK_INFORMATION_TABLE = 0x40;
    public final static int OTHER_TLV_NETWORK_INFORMATION_TABLE = 0x41;
      
    /** AMT */
    public final static int ADDRESS_MAP_TABLE = 0xfe;
    
    public final static int UNKNOWN_TABLE = 0xff;

    public static Table createTable(byte[] buffer) {
        int table_id = (buffer[0] & 0xff);
        
        switch(table_id) {
            case ACTUAL_TLV_NETWORK_INFORMATION_TABLE:
            case OTHER_TLV_NETWORK_INFORMATION_TABLE:
                return new TLV_NetworkInformationTable(buffer);
            case ADDRESS_MAP_TABLE:
                return new AddressMapTable(buffer);
            case UNKNOWN_TABLE:
                return new UnknownTable(buffer);
            default:
                break;
        }
        return null;
    }
    
    private TableFactory() {
        /**
         * @warning Nothing to do since this factory isn't working as instance
         */
    }
}
