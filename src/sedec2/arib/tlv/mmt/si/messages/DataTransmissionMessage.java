package sedec2.arib.tlv.mmt.si.messages;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.TableFactory;
import sedec2.arib.tlv.mmt.si.tables.Table;

public class DataTransmissionMessage extends Message {
    protected byte number_of_tables;
    protected List<TableInfo> table_infos = new ArrayList<>();
    protected List<Table> tables = new ArrayList<>();
    
    class TableInfo {
        public byte table_id;
        public byte table_version;
        public int table_length;
    }
    
    public DataTransmissionMessage(byte[] buffer) {
        super(buffer);
        
        message_id = readOnBuffer(16);
        version = readOnBuffer(8);
        length = readOnBuffer(32);
        
        __decode_message_body__();
    }

    public byte getNumberOfTables() {
        return number_of_tables;
    }
    
    public List<TableInfo> getTableInfo() {
        return table_infos;
    }
    
    public List<Table> getTables() {
        return tables;
    }
    
    @Override
    protected void __decode_message_body__() {
        number_of_tables = (byte) readOnBuffer(8);
        
        for ( int i=0; i<number_of_tables; i++ ) {
            TableInfo table_info = new TableInfo();
            table_info.table_id = (byte) readOnBuffer(8);
            table_info.table_version = (byte) readOnBuffer(8);
            table_info.table_length = readOnBuffer(16);
            table_infos.add(table_info);
        }
        
        for ( int j=(length-1-(number_of_tables*4)); j>0; ) {
            Table table = (Table) TableFactory.createTable(getCurrentBuffer());
            tables.add(table);
            j-=table.getTableLength();
        }
    }
}
