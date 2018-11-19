package sedec2.arib.tlv.container.mmtp.mfu;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MFU_IndexItem extends BitReadWriter {
    protected int num_of_items;
    protected List<Item> items = new ArrayList<>();
    
    public class Item {
        public int item_id;
        public int item_size;
        public byte item_version;
        public byte file_name_length;
        public byte[] file_name_byte;
        public byte checksum_flag;
        public int item_checksum;
        public byte item_type_length;
        public byte[] item_type_byte;
        public byte compression_type;
        public int original_size;
    }
    
    /**
     * @note Table 10-4 configuration of index item in ARIB B60
     */
    public MFU_IndexItem(byte[] buffer) {
        super(buffer);
        
        num_of_items = readOnBuffer(16);
        
        for ( int i=0; i<num_of_items; i++ ) {
            Item item = new Item();
            item.item_id = readOnBuffer(32);
            item.item_size = readOnBuffer(32);
            item.item_version = (byte) readOnBuffer(8);
            item.file_name_length = (byte) readOnBuffer(8);
            item.file_name_byte = new byte[item.file_name_length];
            
            for ( int j=0; j<item.file_name_byte.length; j++ ) {
                item.file_name_byte[j] = (byte) readOnBuffer(8);
            }
            
            item.checksum_flag = (byte) readOnBuffer(1);
            skipOnBuffer(7);
            
            if ( item.checksum_flag == 1) {
                item.item_checksum = readOnBuffer(32);
            }
            
            item.item_type_length = (byte) readOnBuffer(8);
            item.item_type_byte = new byte[item.item_type_length];
            for ( int j=0; j<item.item_type_byte.length; j++ ) {
                item.item_type_byte[j] = (byte) readOnBuffer(8);
            }
            
            item.compression_type = (byte) readOnBuffer(8);
            if ( item.compression_type != 0xff ) {
                item.original_size = readOnBuffer(32);
            }
            items.add(item);
        }
    }
    
    public int getNumOfItems() {
        return num_of_items;
    }
    
    public List<Item> getItems() {
        return items;
    }
    
    public void print() {
        Logger.d(String.format("- MFU_data_byte (Index_item) (%s)\n", getClass().getName()));
        Logger.d(String.format("num_of_items : 0x%x (%d) \n", num_of_items, num_of_items));
        
        for ( int i=0; i<items.size(); i++ ) {
            Item item = items.get(i);
            
            Logger.d(String.format("[%d] item_id : 0x%x \n", i, item.item_id));
            Logger.d(String.format("[%d] item_size : 0x%x (%d) \n", 
                    i, item.item_size, item.item_size));
            Logger.d(String.format("[%d] item_version : 0x%x \n", i, item.item_version));
            Logger.d(String.format("[%d] file_name_length : 0x%x (%d) \n", 
                    i, item.file_name_length, item.file_name_length));
            Logger.d(String.format("[%d] file_name_byte : %s \n", 
                    i, new String(item.file_name_byte)));
            Logger.d(String.format("[%d] checksum_flag : 0x%x \n", i, item.checksum_flag));
            
            if ( item.checksum_flag == 1 ) {
                Logger.d(String.format("[%d] item_checksum : 0x%x \n", 
                        i, item.item_checksum));
            }
            
            Logger.d(String.format("[%d] item_type_length : 0x%x (%d) \n", 
                    i, item.item_type_length, item.item_type_length));
            Logger.d(String.format("[%d] item_type_byte : %s \n", 
                    i, new String(item.item_type_byte)));
            Logger.d(String.format("[%d] compression_type : 0x%x \n", 
                    i, item.compression_type));
            
            if ( item.compression_type != 0xff ) {
                Logger.d(String.format("[%d] original_size : 0x%x (%d) \n", 
                        i, item.original_size, item.original_size));
            }
        }
    }
}
