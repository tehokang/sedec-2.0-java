package sedec2.arib.tlv.mmt.messages;

import sedec2.arib.tlv.mmt.si.TableFactory;
import sedec2.base.Table;

public class M2ShortSectionMessage extends Message {
    protected Table table;
    
    public M2ShortSectionMessage(byte[] buffer) {
        super(buffer);
        
        message_id = readOnBuffer(16);
        version = readOnBuffer(8);
        length = readOnBuffer(16);
        
        __decode_message_body__();
    }

    public Table getTable() {
        return table;
    }
    
    @Override
    protected void __decode_message_body__() {
        table = (Table) TableFactory.createTable(getCurrentBuffer());
    }

    @Override
    public void print() {
        super.print();
        
        if ( table != null ) {
            table.print();
        }
    }
}
