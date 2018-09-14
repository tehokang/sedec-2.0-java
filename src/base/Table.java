package base;

import java.util.Arrays;

import util.Logger;

public abstract class Table extends BitReadWriter {   
    protected int table_id;
    protected int section_syntax_indicator;
    protected int section_length;
    protected byte[] m_crc;
    protected int checksum_CRC32;
    
    public Table(byte[] buffer) {
        super(buffer);
        
        __decode_table_header__();
    }
    
    public void EncodeTable() {
        __encode_update_table_length__();
        __encode_prepare_table__();

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
        Logger.d(String.format("section_syntax_indicator : 0x%x \n", section_syntax_indicator));
        Logger.d(String.format("section_length : 0x%x (%d) \n", section_length, section_length));
        Logger.d("------------------------------ \n");
    }

    protected void __decode_table_header__() {
        table_id = ReadOnBuffer(8);
        section_syntax_indicator = ReadOnBuffer(1);
        SkipOnBuffer(3);
        section_length = ReadOnBuffer(12);
    }
    
    protected abstract void __decode_table_body__();
    
    protected void __encode_update_table_length__() {};
    protected void __encode_prepare_table__() {};
    protected void __encode_prepare_buffer__() {
        if(m_buffer != null)
        {
            m_buffer = null;
        }
        m_buffer = new byte[section_length + 3];

        Arrays.fill(m_buffer, (byte)0xff);
    }
    
    protected void __encode_write_table_header__() {
        WriteOnBuffer( table_id, 8 );
        WriteOnBuffer( section_syntax_indicator, 1);
        WriteOnBuffer( 1, 1);
        WriteOnBuffer( 3, 2);
        WriteOnBuffer( section_length, 12);
    }
    protected void __encode_write_table_body__() {};
    protected void __encode_make_crc__() {
        m_crc = Arrays.copyOf(m_buffer, section_length-1);
        checksum_CRC32 = CalculateCRC32( m_crc, section_length-1 );
        WriteOnBuffer(checksum_CRC32, 32);
    }
}
