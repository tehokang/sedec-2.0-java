package sedec2.base;

import java.io.FileOutputStream;
import java.util.Arrays;

import sedec2.util.Logger;

public abstract class Table extends BitReadWriter {
    protected byte table_id;
    protected byte section_syntax_indicator;
    protected int section_length;
    protected byte[] m_crc;
    protected int checksum_CRC32;
    
    protected boolean is_unknown_table = false;
    
    public Table(byte[] buffer) {
        super(buffer);

        __decode_table_header__();
    }

    public boolean isUnknownTable() {
        return is_unknown_table;
    }
    
    public byte getTableId() {
        return table_id;
    }

    public int getSectionSyntaxIndicator() {
        return section_syntax_indicator;
    }

    public int getSectionLength() {
        return section_length;
    }

    public void encode() {
        __encode_update_table_length__();
        __encode_prepare_table__();

        __encode_prepare_buffer__();
        __encode_write_table_header__();
        __encode_write_table_body__();
        __encode_make_crc__();
    }

    public void save(String filepath) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(m_buffer);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print() {
        Logger.d(String.format("======= Section Header ======= (%s)\n", getClass().getName()));
        Logger.d(String.format("table_id : 0x%x \n", table_id));
        Logger.d(String.format("section_syntax_indicator : 0x%x \n", section_syntax_indicator));
        Logger.d(String.format("section_length : 0x%x (%d) \n", section_length, section_length));
        Logger.d("------------------------------ \n");
    }

    public int getTableLength() {
        return 3 + section_length;
    }

    /**
     * @note internal functions to decode
     */
    protected void __decode_table_header__() {
        table_id = (byte) readOnBuffer(8);
        section_syntax_indicator = (byte) readOnBuffer(1);
        skipOnBuffer(3);
        section_length = readOnBuffer(12);
    }

    protected abstract void __decode_table_body__();

    /**
     * @note internal functions to encode
     */
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
        writeOnBuffer( table_id, 8 );
        writeOnBuffer( section_syntax_indicator, 1);
        writeOnBuffer( 1, 1);
        writeOnBuffer( 3, 2);
        writeOnBuffer( section_length, 12);
    }
    protected void __encode_write_table_body__() {};
    protected void __encode_make_crc__() {
        m_crc = Arrays.copyOf(m_buffer, section_length-1);
        checksum_CRC32 = calculateCRC32( m_crc, section_length-1 );
        writeOnBuffer(checksum_CRC32, 32);
    }
}
