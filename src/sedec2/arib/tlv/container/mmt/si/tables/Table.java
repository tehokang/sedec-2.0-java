package sedec2.arib.tlv.container.mmt.si.tables;

import sedec2.util.Logger;

public abstract class Table extends sedec2.base.Table {   
    protected byte version;
    protected int length;
    
    public Table(byte[] buffer) {
        super(buffer);
    }
    
    public byte getVersion() {
        return version;
    }
    
    public void encode() {
        __encode_update_table_length__();
        __encode_prepare_table__();

        __encode_prepare_buffer__();
        __encode_write_table_header__();
        __encode_write_table_body__();
        __encode_make_crc__();
    }
    
    public void print() {
        Logger.d(String.format("======= Section Header ======= (%s)\n", getClass().getName()));
        Logger.d(String.format("table_id : 0x%x \n", table_id));
        Logger.d(String.format("version : 0x%x \n", version));
        Logger.d(String.format("length : 0x%x (%d) \n", length, length));
        Logger.d("------------------------------ \n");
    }
    
    @Override
    public int getTableLength() {
        return 4 + length;
    }

    @Override
    protected void __decode_table_header__() {
        table_id = (byte) readOnBuffer(8);
        version = (byte) readOnBuffer(8);
        length = readOnBuffer(16);
    }
    
    @Override
    protected void __encode_write_table_header__() {
        writeOnBuffer( table_id, 8 );
        writeOnBuffer( version, 8);
        writeOnBuffer( length, 16);
    }
}
