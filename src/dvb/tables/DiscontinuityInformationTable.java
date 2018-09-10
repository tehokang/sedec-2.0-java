package dvb.tables;

import base.Table;
import util.Logger;

public class DiscontinuityInformationTable extends Table {
    protected byte transition_flag;
    
    public DiscontinuityInformationTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        transition_flag = (byte) ReadOnBuffer(1);
        SkipOnBuffer(7);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("transition_flag : 0x%x \n", transition_flag));
    }

}
