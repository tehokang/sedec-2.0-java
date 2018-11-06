package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DataAssetManagementTable extends sedec2.base.Table {
    protected byte data_transmission_session_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int transaction_id;
    protected int component_tag;
    protected int download_id;
    protected byte num_of_mpus;
    protected List<MPU> mpus = new ArrayList<>();
    protected byte component_info_length;
    protected byte[] component_info_byte;
    
    class MPU {
        public int mpu_sequence_number;
        public int mpu_size;
        public byte index_item_flag;
        public byte index_item_id_flag;
        public byte index_item_compression_type;
        public int index_item_id;
        public int num_of_items;
        public List<Item> items = new ArrayList<>();
        public byte mpu_info_length;
        public byte[] mpu_info_byte;
    }
    
    class Item {
        public int node_tag;
        public int item_id;
        public int item_size;
        public byte item_version;
        public byte checksum_flag;
        public int item_checksum;
        public byte item_info_length;
        public byte[] item_info_byte;
    }
    
    public DataAssetManagementTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        data_transmission_session_id = (byte) readOnBuffer(8);
        skipOnBuffer(10);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        transaction_id = readOnBuffer(32);
        component_tag = readOnBuffer(16);
        download_id = readOnBuffer(32);
        num_of_mpus = (byte) readOnBuffer(8);

        for ( int i=0; i<num_of_mpus; i++ ) {
            MPU mpu = new MPU();
            mpu.mpu_sequence_number = readOnBuffer(32);
            mpu.mpu_size = readOnBuffer(32);
            mpu.index_item_flag = (byte) readOnBuffer(1);
            mpu.index_item_id_flag = (byte) readOnBuffer(1);
            mpu.index_item_compression_type = (byte) readOnBuffer(2);
            skipOnBuffer(4);
            if ( mpu.index_item_flag == 1 && mpu.index_item_id_flag == 1 ) {
                mpu.index_item_id = readOnBuffer(32);
            }
            
            mpu.num_of_items = readOnBuffer(16);
            
            for ( int k=0; k<mpu.num_of_items; k++ ) {
                Item item = new Item();
                item.node_tag = readOnBuffer(16);
                
                if ( mpu.index_item_flag == 0 ) {
                    item.item_id = readOnBuffer(32);
                    item.item_size = readOnBuffer(32);
                    item.item_version = (byte) readOnBuffer(8);
                    item.checksum_flag = (byte) readOnBuffer(1);
                    skipOnBuffer(7);
                    
                    if ( item.checksum_flag == 1 ) {
                        item.item_checksum = readOnBuffer(32);
                    }
                    item.item_info_length = (byte) readOnBuffer(8);
                    item.item_info_byte = new byte[item.item_info_length];
                    for ( int n=0; n<item.item_info_byte.length; n++ ) {
                        item.item_info_byte[n] = (byte) readOnBuffer(8);
                    }
                }
                mpu.items.add(item);
            }
            mpu.mpu_info_length = (byte) readOnBuffer(8);
            mpu.mpu_info_byte = new byte[mpu.mpu_info_length];
            for ( int k=0; k<mpu.mpu_info_byte.length; k++ ) {
                mpu.mpu_info_byte[k] = (byte) readOnBuffer(8);
            }
            mpus.add(mpu);
        }
        
        component_info_length = (byte) readOnBuffer(8);
        component_info_byte = new byte[component_info_length];
        
        for ( int i=0; i<component_info_byte.length; i++ ) {
            component_info_byte[i] = (byte) readOnBuffer(8);
        }
        
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("data_transmission_session_id : 0x%x \n", 
                data_transmission_session_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("transaction_id : 0x%x \n", transaction_id));
        Logger.d(String.format("component_tag : 0x%x \n", component_tag));
        Logger.d(String.format("download_id : 0x%x \n",  download_id));
        Logger.d(String.format("num_of_mpus : 0x%x (%d) \n", num_of_mpus, num_of_mpus));

        for ( int i=0; i<mpus.size(); i++ ) {
            MPU mpu = mpus.get(i);
            Logger.d(String.format("[%d] mpu_sequence_number : 0x%x \n",  
                    i, mpu.mpu_sequence_number));
            Logger.d(String.format("[%d] mpu_size : 0x%x (%d) \n", 
                    i, mpu.mpu_size, mpu.mpu_size));
            Logger.d(String.format("[%d] index_item_flag : 0x%x \n", i, mpu.index_item_flag));
            Logger.d(String.format("[%d] index_item_id_flag : 0x%x \n", 
                    i, mpu.index_item_id_flag));
            Logger.d(String.format("[%d] index_item_compression_type : 0x%x \n", 
                    i, mpu.index_item_compression_type));
            if ( mpu.index_item_flag == 1 && mpu.index_item_id_flag == 1 ) {
                Logger.d(String.format("[%d] index_item_id : 0x%x \n", i, mpu.index_item_id));
            }
            
            Logger.d(String.format("[%d] num_of_items : 0x%x (%d) \n", 
                    i, mpu.items.size(), mpu.items.size()));
            
            for ( int k=0; k<mpu.items.size(); k++ ) {
                Item item = mpu.items.get(k);
                Logger.d(String.format("\t [%d] node_tag : 0x%x \n", 
                        k, item.node_tag));
                
                if ( mpu.index_item_flag == 0 ) {
                    Logger.d(String.format("\t [%d] item_id : 0x%x \n", 
                            k, item.item_id));
                    Logger.d(String.format("\t [%d] item_size : 0x%x (%d) \n", 
                            k, item.item_size, item.item_size));
                    Logger.d(String.format("\t [%d] item_version : 0x%x \n", 
                            k, item.item_version));
                    
                    if ( item.checksum_flag == 1 ) {
                        Logger.d(String.format("\t [%d] item_checksum : 0x%x \n", 
                                k, item.item_checksum));
                    }
                    Logger.d(String.format("\t [%d] item_info_length : 0x%x (%d) \n", 
                            k, item.item_info_length, item.item_info_length));
                    Logger.d(String.format("\t [%d] item_info_byte : %s \n", 
                            k, new String(item.item_info_byte)));
                }
            }
            Logger.d(String.format("[%d] mpu_info_length : 0x%x (%d) \n", 
                    i, mpu.mpu_info_length, mpu.mpu_info_length));
            BinaryLogger.print(mpu.mpu_info_byte);
        }
        
        Logger.d(String.format("component_info_length : 0x%x (%d) \n", 
                component_info_length, component_info_length));
        Logger.d("component_info_byte : \n");
        BinaryLogger.print(component_info_byte);
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
