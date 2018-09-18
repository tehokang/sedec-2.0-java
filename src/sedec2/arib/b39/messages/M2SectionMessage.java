package sedec2.arib.b39.messages;

import sedec2.arib.b39.TableFactory;
import sedec2.arib.b39.tables.Table;

public class M2SectionMessage extends Message {
    protected Table table;

    public M2SectionMessage(byte[] buffer) {
        super(buffer);
        
        __decode_message_body__();
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
