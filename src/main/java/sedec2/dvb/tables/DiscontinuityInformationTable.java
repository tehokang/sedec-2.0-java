package sedec2.dvb.tables;

import sedec2.base.Table;
import sedec2.util.Logger;

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

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("transition_flag : 0x%x \n", transition_flag));
    }

}
