package com.sedec.arib.b10.tables;

import com.sedec.base.Table;
import com.sedec.util.Logger;

/**
 * ARIB-B16 Download Section
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

    public byte getMakerId() {
        return maker_id;
    }

    public byte getModelId() {
        return model_id;
    }

    public byte getVersionId() {
        return version_id;
    }

    public int getSectionNumber() {
        return section_number;
    }

    public int getLastSectionNumber() {
        return last_section_number;
    }

    public byte[] getModelInfo() {
        return model_info;
    }
    public byte[] getCodeDataByte() {
        return code_data_byte;
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
