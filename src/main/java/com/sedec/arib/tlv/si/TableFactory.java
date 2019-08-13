package com.sedec.arib.tlv.si;

import com.sedec.arib.tlv.si.tables.AddressMapTable;
import com.sedec.arib.tlv.si.tables.TLV_NetworkInformationTable;
import com.sedec.base.Table;
import com.sedec.base.UnknownTable;

/**
 * Factory to obtain a kind of table of TLV-SI like below.
 * <ul>
 * <li> {@link AddressMapTable}
 * <li> {@link TLV_NetworkInformationTable}
 * </ul>
 */
public class TableFactory {
    /** TLV-NIT */
    public final static byte ACTUAL_TLV_NETWORK_INFORMATION_TABLE = (byte) 0x40;
    public final static byte TLV_NIT_ACTUAL = ACTUAL_TLV_NETWORK_INFORMATION_TABLE;

    public final static byte OTHER_TLV_NETWORK_INFORMATION_TABLE = (byte) 0x41;
    public final static byte TLV_NIT_OTHER = OTHER_TLV_NETWORK_INFORMATION_TABLE;

    /** AMT */
    public final static byte ADDRESS_MAP_TABLE = (byte) 0xfe;
    public final static byte AMT = ADDRESS_MAP_TABLE;

    public final static byte UNKNOWN_TABLE = (byte) 0xff;

    public static Table createTable(byte[] buffer) {
        byte table_id = (byte)(buffer[0] & 0xff);

        switch ( table_id ) {
            case ACTUAL_TLV_NETWORK_INFORMATION_TABLE:
            case OTHER_TLV_NETWORK_INFORMATION_TABLE:
                return new TLV_NetworkInformationTable(buffer);
            case ADDRESS_MAP_TABLE:
                return new AddressMapTable(buffer);
            case UNKNOWN_TABLE:
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
