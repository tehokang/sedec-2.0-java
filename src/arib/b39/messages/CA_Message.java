package arib.b39.messages;

import java.util.ArrayList;
import java.util.List;

import arib.b39.TableFactory;
import arib.b39.tables.Table;

public class CA_Message extends Message {
    protected List<Table> tables = new ArrayList<>();
    
    public CA_Message(byte[] buffer) {
        super(buffer);
        
        __decode_message_body__();
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
