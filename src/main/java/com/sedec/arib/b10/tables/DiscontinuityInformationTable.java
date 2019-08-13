package com.sedec.arib.b10.tables;

import com.sedec.base.Table;
import com.sedec.util.Logger;

public class DiscontinuityInformationTable extends Table {
    protected byte transition_flag;

    public DiscontinuityInformationTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        transition_flag = (byte) readOnBuffer(1);
        skipOnBuffer(7);
    }

    public byte getTransitionFlag() {
        return transition_flag;
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("transition_flag : 0x%x \n", transition_flag));
    }

}
