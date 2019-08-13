package com.sedec.arib.tlv.container.mmtp.messages;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.tlv.container.mmt.si.TableFactory;
import com.sedec.base.Table;
import com.sedec.util.Logger;

/**
 * Class to describe as PA Message of Table 7-1 of ARIB B60
 * which contains MPT, PLT, LCT of MMT-SI.
 */
public class PAMessage extends Message {
    protected byte number_of_tables;
    protected List<TableInfo> table_infos = new ArrayList<>();

    public class TableInfo {
        public byte table_id;
        public byte table_version;
        public int table_length;
    }

    /**
     * Constructor to decode PA Message of Table 7-1
     * @param buffer message_byte of MMTP payload
     */
    public PAMessage(byte[] buffer) {
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

    /**
     * Gets number_of_tables of Table 7-1 of ARIB B60
     * @return number_of_tables
     */
    public byte getNumberOfTables() {
        return number_of_tables;
    }

    /**
     * Gets list of table in Table 7-1 of ARIB B60 which contains table_id, table_version, table_length
     * @return table_infos list of table
     */
    public List<TableInfo> getTableInfo() {
        return table_infos;
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
            Table table = TableFactory.createTable(getCurrentBuffer());
            tables.add(table);
            j-=table.getTableLength();
        }
    }

    @Override
    public void print() {
        super.print();

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
            tables.get(i).print();
        }
    }
}
