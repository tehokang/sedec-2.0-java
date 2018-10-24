package sedec2.arib.tlv.mmt.messages;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.TableFactory;
import sedec2.arib.tlv.mmt.si.tables.Table;

public class CAMessage extends Message {
    protected List<Table> tables = new ArrayList<>();
    
    public CAMessage(byte[] buffer) {
        super(buffer);
        
        message_id = readOnBuffer(16);
        version = readOnBuffer(8);
        length = readOnBuffer(16);
        
        __decode_message_body__();
    }
    
    public List<Table> getTables() {
        return tables;
    }
    
    @Override
    protected void __decode_message_body__() {
        for ( int i=length; i>0; ) {
            Table table = (Table) TableFactory.createTable(getCurrentBuffer());
            i-=table.getTableLength();
            tables.add(table);
        }
    }
    
    @Override
    public void print() {
        super.print();
        
        for ( int i=0; i<tables.size(); i++ ) {
            tables.get(i).print();
        }
    }
}
