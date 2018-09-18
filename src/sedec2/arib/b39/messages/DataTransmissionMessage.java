package sedec2.arib.b39.messages;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.b39.TableFactory;
import sedec2.arib.b39.tables.Table;

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
        
        __decode_message_body__();
    }

    @Override
    protected void __decode_message_body__() {
        number_of_tables = (byte) ReadOnBuffer(8);
        
        for ( int i=0; i<number_of_tables; i++ ) {
            TableInfo table_info = new TableInfo();
            table_info.table_id = (byte) ReadOnBuffer(8);
            table_info.table_version = (byte) ReadOnBuffer(8);
            table_info.table_length = ReadOnBuffer(16);
            table_infos.add(table_info);
        }
        
        for ( int j=(length-(number_of_tables*4)); j>0; ) {
            Table table = (Table) TableFactory.CreateTable(GetCurrentBuffer());
            tables.add(table);
        }
    }
}
