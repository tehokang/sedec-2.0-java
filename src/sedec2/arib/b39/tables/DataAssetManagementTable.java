package sedec2.arib.b39.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.b39.DescriptorFactory;
import sedec2.arib.b39.descriptors.Descriptor;
import sedec2.util.Logger;

public class DataAssetManagementTable extends Table {
    protected byte num_of_data_components;
    protected List<DataComponent> data_components = new ArrayList<>();
    
    class DataComponent {
        public int transaction_id;
        public int component_tag;
        public int download_id;
        public byte num_of_mpus;
        public List<MPU> mpus = new ArrayList<>();
        public int descriptors_loop_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    class MPU {
        public int mpu_sequence_number;
        public byte num_of_items;
        public List<Item> items = new ArrayList<>();
        public byte mpu_info_length;
        public byte[] mpu_info_byte;
    }
    
    class Item {
        public int item_id;
        public int node_tag;
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
        num_of_data_components = (byte) ReadOnBuffer(8);
        
        for ( int i=0; i<num_of_data_components; i++ ) {
            DataComponent data_component = new DataComponent();
            data_component.transaction_id = ReadOnBuffer(32);
            data_component.component_tag = ReadOnBuffer(16);
            data_component.download_id = ReadOnBuffer(32);
            data_component.num_of_mpus = (byte) ReadOnBuffer(8);
            
            for ( int j=0; j<data_component.num_of_mpus; j++ ) {
                MPU mpu = new MPU();
                mpu.mpu_sequence_number = ReadOnBuffer(32);
                mpu.num_of_items = (byte) ReadOnBuffer(8);
                
                for ( int k=0; k<mpu.num_of_items; k++ ) {
                    Item item = new Item();
                    item.item_id = ReadOnBuffer(32);
                    item.node_tag = ReadOnBuffer(16);
                    item.item_size = ReadOnBuffer(32);
                    item.item_version = (byte) ReadOnBuffer(8);
                    item.checksum_flag = (byte) ReadOnBuffer(1);
                    SkipOnBuffer(7);
                    
                    if ( item.checksum_flag == 1 ) {
                        item.item_checksum = ReadOnBuffer(32);
                    }
                    
                    item.item_info_length = (byte) ReadOnBuffer(8);
                    item.item_info_byte = new byte[item.item_info_length];
                    for ( int m=0; m<item.item_info_length; m++ ) {
                        item.item_info_byte[m] = (byte) ReadOnBuffer(8);
                    }
                    mpu.items.add(item);
                }
                mpu.mpu_info_length = (byte) ReadOnBuffer(8);
                mpu.mpu_info_byte = new byte[mpu.mpu_info_length];
                for ( int k=0; k<mpu.mpu_info_length; k++ ) {
                    mpu.mpu_info_byte[k] = (byte) ReadOnBuffer(8);
                }
                data_component.mpus.add(mpu);
            }
            data_component.descriptors_loop_length = ReadOnBuffer(16);
            for ( int j=data_component.descriptors_loop_length; j>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
                j-=desc.GetDescriptorLength();
                data_component.descriptors.add(desc);
            }
        }
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("num_of_data_components : 0x%x \n", num_of_data_components));

        for ( int i=0; i<data_components.size(); i++ ) {
            DataComponent data_component = data_components.get(i);
            
            Logger.d(String.format("[%d] transaction_id : 0x%x \n", 
                    i, data_component.transaction_id));
            Logger.d(String.format("[%d] component_tag : 0x%x \n", 
                    i, data_component.component_tag));
            Logger.d(String.format("[%d] download_id : 0x%x \n", 
                    i, data_component.download_id));
            Logger.d(String.format("[%d] num_of_mpus : 0x%x \n", 
                    i, data_component.num_of_mpus));
            
            for ( int j=0; j<data_component.mpus.size(); j++ ) {
                MPU mpu = data_component.mpus.get(j);
                
                Logger.d(String.format("\t [%d] mpu_sequence_number : 0x%x \n", 
                        j, mpu.mpu_sequence_number));
                Logger.d(String.format("\t [%d] num_of_items : 0x%x \n", 
                        j, mpu.num_of_items));
                
                for ( int k=0; k<mpu.items.size(); k++ ) {
                    Item item = mpu.items.get(k);
                    
                    Logger.d(String.format("\t\t [%d] item_id : 0x%x \n", k, item.item_id));
                    Logger.d(String.format("\t\t [%d] node_tag : 0x%x \n", k, item.node_tag));
                    Logger.d(String.format("\t\t [%d] item_size : 0x%x \n", k, item.item_size));
                    Logger.d(String.format("\t\t [%d] item_version : 0x%x \n", 
                            k, item.item_version));
                    Logger.d(String.format("\t\t [%d] checksum_flag : 0x%x \n", 
                            k, item.checksum_flag));
                    
                    if ( item.checksum_flag == 1) {
                        Logger.d(String.format("\t\t [%d] item_checksum : 0x%x \n", 
                                k, item.item_checksum));
                    }
                    
                    Logger.d(String.format("\t\t [%d] item_info_length : 0x%x \n", 
                            k, item.item_info_length));
                    Logger.d(String.format("\t\t [%d] item_info_byte : %s \n", 
                            k, new String(item.item_info_byte)));
                }
                
                Logger.d(String.format("\t [%d] mpu_info_length : 0x%x \n", 
                        j, mpu.mpu_info_length));
                Logger.d(String.format("\t [%d] mpu_info_byte : %s \n", 
                        j, new String(mpu.mpu_info_byte)));
            }
            
            Logger.d(String.format("[%d] descriptors_loop_length : 0x%x \n", 
                    i, data_component.descriptors_loop_length));
            for ( int j=data_component.descriptors.size(); j>0; ) {
                data_component.descriptors.get(j).PrintDescriptor();
            }
        }
    }
}
