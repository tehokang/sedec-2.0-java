package sedec2.arib.mmt.si.messages;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.mmt.si.TableFactory;
import sedec2.arib.mmt.si.tables.Table;

public class CAMessage extends Message {
    protected List<Table> tables = new ArrayList<>();
    
    public CAMessage(byte[] buffer) {
        super(buffer);
        
        __decode_message_body__();
    }
    
    public List<Table> GetTables() {
        return tables;
    }
    
    @Override
    protected void __decode_message_body__() {
        for ( int i=length; i>0; ) {
            Table table = (Table) TableFactory.CreateTable(GetCurrentBuffer());
            i-=table.GetTableLength();
            tables.add(table);
        }
    }
}