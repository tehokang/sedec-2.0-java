package sedec2.arib.tlv.mmt.si.tables;

import sedec2.base.Table;
import sedec2.util.Logger;

public class EntitlementManagementMessage extends Table {
    protected int table_id_extension;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected byte[] EMM_data;

    public EntitlementManagementMessage(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }
    
    public int GetTableIdExtension() {
        return table_id_extension;
    }
    
    public byte GetVersionNumber() {
        return version_number;
    }
    
    public byte GetCurrentNextIndicator() {
        return current_next_indicator;
    }
    
    public byte GetSectionNumber() {
        return section_number;
    }
    
    public byte GetLastSectionNumber() {
        return last_section_number;
    }
    
    public byte[] GetEMMData() {
        return EMM_data;
    }
    
    @Override
    protected void __decode_table_body__() {
        table_id_extension = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        
        EMM_data = new byte[section_length - 5 - 4];
        for ( int i=0; i<EMM_data.length; i++ ) {
            EMM_data[i] = (byte) ReadOnBuffer(8);
        }
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("table_id_extension : 0x%x \n", table_id_extension));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        
        int j=1;
        Logger.p(String.format("%03d : ", j));
        for(int i=0; i<EMM_data.length; i++)
        {
            Logger.p(String.format("%02x ", EMM_data[i]));
            if(i%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
