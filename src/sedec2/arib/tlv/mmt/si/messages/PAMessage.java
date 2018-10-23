package sedec2.arib.tlv.mmt.si.messages;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.TableFactory;
import sedec2.arib.tlv.mmt.si.tables.Table;
import sedec2.util.Logger;

public class PAMessage extends Message {
    protected byte number_of_tables;
    protected List<TableInfo> table_infos = new ArrayList<>();
    protected List<Table> tables = new ArrayList<>();
    
    class TableInfo {
        public byte table_id;
        public byte table_version;
        public int table_length;
    }
    
    public PAMessage(byte[] buffer) {
        super(buffer);
        
        message_id = ReadOnBuffer(16);
        version = ReadOnBuffer(8);
        length = ReadOnBuffer(32);
        
        __decode_message_body__();
    }

    public byte GetNumberOfTables() {
        return number_of_tables;
    }
    
    public List<TableInfo> GetTableInfo() {
        return table_infos;
    }
    
    public List<Table> GetTables() {
        return tables;
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
        
        for ( int j=(length-1-(number_of_tables*4)); j>0; ) {
            Table table = (Table) TableFactory.CreateTable(GetCurrentBuffer());
            tables.add(table);
            j-=table.GetTableLength();
        }
    }

    @Override
    public void PrintMessage() {
        super.PrintMessage();
        
        Logger.d(String.format("number_of_tables : %d \n", number_of_tables));
        
        for ( int i=0; i<table_infos.size(); i++ ) {
            TableInfo table_info = table_infos.get(i);
            
            Logger.d(String.format("[%d] table_id : 0x%x \n", i, table_info.table_id));
            Logger.d(String.format("[%d] table_version : 0x%x \n", 
                    i, table_info.table_version));
            Logger.d(String.format("[%d] table_length : 0x%x \n", 
                    i, table_info.table_length));
        }
        
        for ( int i=0; i<tables.size(); i++ ) {
            tables.get(i).PrintTable();
        }
    }
}
