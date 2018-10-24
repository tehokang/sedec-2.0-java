package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

public class MH_CommonDataTable extends Table {

    protected int download_data_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int original_network_id;
    protected byte data_type;
    protected int descriptors_loop_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    protected DataModuleByte data_module_byte;
    
    class DataModuleByte {
        public byte logo_type;
        public int logo_id;
        public int logo_version;
        public int data_size;
        public int[] data_byte;
    }
    public MH_CommonDataTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public int getDownloadDataId() {
        return download_data_id;
    }
    
    public byte getVersionNumber() {
        return version_number;
    }
    
    public byte getCurrentNextIndicator() {
        return current_next_indicator;
    }
    
    public byte getSectionNumber() {
        return section_number;
    }
    
    public byte getLastSectionNumber() {
        return last_section_number;
    }
    
    public int getOriginalNetworkId() {
        return original_network_id;
    }
    
    public byte getDataType() {
        return data_type;
    }
    
    public int getDescriptorsLoopLength() {
        return descriptors_loop_length;
    }
    
    public List<Descriptor> getDescriptors() {
        return descriptors;
    }
    
    public DataModuleByte getDataModuleByte() {
        return data_module_byte;
    }
    
    @Override
    protected void __decode_table_body__() {
        download_data_id = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        original_network_id = readOnBuffer(16);
        data_type = (byte) readOnBuffer(8);
        skipOnBuffer(4);
        descriptors_loop_length = readOnBuffer(12);
        
        for ( int i=descriptors_loop_length;i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }
        
        for (int i = (section_length - 10 - descriptors_loop_length - 4); i>0; ) {
            if ( data_type == 0x01 ) {
                data_module_byte.logo_type = (byte) readOnBuffer(8);
                skipOnBuffer(7);
                data_module_byte.logo_id = readOnBuffer(9);
                skipOnBuffer(4);
                data_module_byte.logo_version = readOnBuffer(12);
                data_module_byte.data_size = readOnBuffer(16);
                data_module_byte.data_byte = new int[data_module_byte.data_size];
                
                for ( int j=0; j<data_module_byte.data_size; j++ ) {
                    data_module_byte.data_byte[j] = readOnBuffer(8);
                }
                i-= (7 + data_module_byte.data_size);
            } else {
                skipOnBuffer(8);
                i-=1;
            }
        }
        
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("download_data_id : 0x%x \n", download_data_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("original_network_id : 0x%x \n", original_network_id));
        Logger.d(String.format("data_type : 0x%x \n", data_type));
        Logger.d(String.format("descriptors_loop_length : 0x%x \n",  descriptors_loop_length));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            Descriptor desc = descriptors.get(i);
            desc.print();
        }
        
        if ( data_type == 0x01 ) {
            Logger.d(String.format("data_module_byte.logo_type : 0x%x \n", 
                    data_module_byte.logo_type));
            Logger.d(String.format("data_module_byte.logo_id : 0x%x \n", 
                    data_module_byte.logo_id));
            Logger.d(String.format("data_module_byte.logo_version : 0x%x \n", 
                    data_module_byte.logo_version));
            Logger.d(String.format("data_module_byte.data_size : 0x%x \n", 
                    data_module_byte.data_size));
            
            Logger.d(String.format("data_module_byte.data_byte : "));
            for ( int i=0; i<data_module_byte.data_size; i++ ) {
                Logger.d(String.format("0x%x", data_module_byte.data_byte[i]));
            }
            Logger.d(String.format("\n"));
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
    
}
