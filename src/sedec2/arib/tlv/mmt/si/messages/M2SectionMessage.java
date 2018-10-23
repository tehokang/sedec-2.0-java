package sedec2.arib.tlv.mmt.si.messages;

import sedec2.arib.tlv.mmt.si.TableFactory;
import sedec2.base.Table;

public class M2SectionMessage extends Message {
    protected Table table;

    public M2SectionMessage(byte[] buffer) {
        super(buffer);
        
        message_id = ReadOnBuffer(16);
        version = ReadOnBuffer(8);
        length = ReadOnBuffer(16);
        
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
