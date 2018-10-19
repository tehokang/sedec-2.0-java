package sedec2.arib.mmt.si.messages;

import sedec2.arib.mmt.si.TableFactory;
import sedec2.arib.mmt.si.tables.Table;

public class M2SectionMessage extends Message {
    protected Table table;

    public M2SectionMessage(byte[] buffer) {
        super(buffer);
        
        __decode_message_body__();
    }
    
    public Table GetTable() {
        return table;
    }
    
    @Override
    protected void __decode_message_body__() {
        table = (Table) TableFactory.CreateTable(GetCurrentBuffer());
    }

    @Override
    public void PrintMessage() {
        super.PrintMessage();
        
        table.PrintTable();
    }

}