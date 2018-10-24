package sedec2.arib.b10.tables;

import sedec2.base.Table;
import sedec2.util.Logger;

/**
 * @brief ARIB-B16 Download Section
 */
public class DownloadTable extends Table {
    protected byte maker_id;
    protected byte model_id;
    protected byte version_id;
    protected int section_number;
    protected int last_section_number;
    protected byte []model_info = new byte[145];
    protected byte []code_data_byte = new byte[2048];
    
    public DownloadTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        maker_id = (byte) readOnBuffer(8);
        model_id = (byte) readOnBuffer(8);
        section_number = readOnBuffer(16);
        last_section_number = readOnBuffer(16);
        
        for ( int i=0; i<model_info.length; i++ ) {
            model_info[i] = (byte) readOnBuffer(8);
        }
        
        for ( int i=0; i<code_data_byte.length; i++ ) {
            code_data_byte[i] = (byte) readOnBuffer(8);
        }
        
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("maker_id : 0x%x \n", maker_id));
        Logger.d(String.format("model_id : 0x%x \n", model_id));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("model_info : %s \n", new String(model_info)));
    }
}
