package sedec2.arib.b10.tables;

import sedec2.base.Table;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class EntitlementControlMessage extends Table {
    protected int table_id_extension;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected byte[] ECM_data;

    public EntitlementControlMessage(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }
    
    @Override
    protected void __decode_table_body__() {
        table_id_extension = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        
        ECM_data = new byte[section_length - 5 - 4];
        for ( int i=0; i<ECM_data.length; i++ ) {
            ECM_data[i] = (byte) readOnBuffer(8);
        }
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("table_id_extension : 0x%x \n", table_id_extension));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("ECM_data : \n"));
        
        BinaryLogger.print(ECM_data);

        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
