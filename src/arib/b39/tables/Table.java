package arib.b39.tables;

import util.Logger;

public abstract class Table extends base.Table {   
    protected int table_id;
    protected int version;
    protected int length;
    protected byte[] m_crc;
    protected int checksum_CRC32;
    
    public Table(byte[] buffer) {
        super(buffer);
        
        __decode_table_header__();
    }
    
    public void EncodeTable() {
        __encode_update_table_length__();
        __encode_preprare_table__();

        __encode_prepare_buffer__();
        __encode_write_table_header__();
        __encode_write_table_body__();
        __encode_make_crc__();
    }
    
    public void SaveTable() {
        
    }
         
    public void PrintTable() {
        Logger.d(String.format("======= Section Header ======= (%s)\n", getClass().getName()));
        Logger.d(String.format("table_id : 0x%x \n", table_id));
        Logger.d(String.format("version : 0x%x \n", version));
        Logger.d(String.format("length : 0x%x (%d) \n", length, length));
        Logger.d("------------------------------ \n");
    }

    @Override
    protected void __decode_table_header__() {
        table_id = ReadOnBuffer(8);
        version = ReadOnBuffer(8);
        length = ReadOnBuffer(16);
    }
    
    @Override
    protected void __encode_write_table_header__() {
        WriteOnBuffer( table_id, 8 );
        WriteOnBuffer( version, 8);
        WriteOnBuffer( length, 16);
    }
}
