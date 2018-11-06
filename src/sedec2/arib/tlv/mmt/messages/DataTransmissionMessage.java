package sedec2.arib.tlv.mmt.messages;

import sedec2.arib.tlv.mmt.si.TableFactory;
import sedec2.base.Table;

public class DataTransmissionMessage extends Message {
    
    public DataTransmissionMessage(byte[] buffer) {
        super(buffer);
        
        message_id = readOnBuffer(16);
        version = readOnBuffer(8);
        length = readOnBuffer(32);
        
        __decode_message_body__();
    }

    @Override
    public int getMessageLength() {
        return length + 7;
    }
    
    @Override
    protected void __decode_message_body__() {
        tables.add( (Table) TableFactory.createTable(getCurrentBuffer()) );
    }

    @Override
    public void print() {
        super.print();
        
        for ( int i=0; i<tables.size(); i++ ) {
            tables.get(i).print();
        }
    }
}
