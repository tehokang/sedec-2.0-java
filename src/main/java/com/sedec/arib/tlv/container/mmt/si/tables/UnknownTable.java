package com.sedec.arib.tlv.container.mmt.si.tables;

public class UnknownTable extends Table {

    public UnknownTable(byte[] buffer) {
        super(buffer);

        is_unknown_table = true;
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        skipOnBuffer(length*8);
    }
}
