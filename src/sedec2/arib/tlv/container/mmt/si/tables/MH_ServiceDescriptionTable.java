package sedec2.arib.tlv.container.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.container.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.container.mmt.si.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

public class MH_ServiceDescriptionTable extends Table {
    protected int tlv_stream_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int original_network_id;
    protected int service_id;
    protected byte EIT_user_defined_flags;
    protected byte EIT_schedule_flag;
    protected byte EIT_present_following_flag;
    protected byte running_status;
    protected byte free_CA_mode;
    protected int descriptors_loop_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    
    public MH_ServiceDescriptionTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public int getTlvStreamId() {
        return tlv_stream_id;
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
    
    public int getServiceId() {
        return service_id;
    }
    
    public byte getEITUserDefinedFlags() {
        return EIT_user_defined_flags;
    }
    
    public byte getEITScheduleFlag() {
        return EIT_schedule_flag;
    }
    
    public byte getEITPresentFollowingFlag() {
        return EIT_present_following_flag;
    }
    
    public byte getRunningStatus() {
        return running_status;
    }
    
    public byte getFreeCAMode() {
        return free_CA_mode;
    }
    
    public List<Descriptor> getDescriptors() {
        return descriptors;
    }
    
    @Override
    protected void __decode_table_body__() {
        tlv_stream_id = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        original_network_id = readOnBuffer(16);
        skipOnBuffer(8);
        
        service_id = readOnBuffer(16);
        skipOnBuffer(3);
        EIT_user_defined_flags = (byte) readOnBuffer(3);
        EIT_schedule_flag = (byte) readOnBuffer(1);
        EIT_present_following_flag = (byte) readOnBuffer(1);
        running_status = (byte) readOnBuffer(3);
        free_CA_mode = (byte) readOnBuffer(1);
        
        descriptors_loop_length = readOnBuffer(12);
        for ( int i=descriptors_loop_length;i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }
        
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("tlv_stream_id : 0x%x \n", tlv_stream_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", 
                current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", 
                last_section_number));
        Logger.d(String.format("original_network_id : 0x%x \n", 
                original_network_id));
        
        Logger.d(String.format("service_id : 0x%x \n",  service_id));
        Logger.d(String.format("EIT_user_defined_flags : 0x%x \n", 
                EIT_user_defined_flags));
        Logger.d(String.format("EIT_schedule_flag : 0x%x \n", 
                EIT_user_defined_flags));
        Logger.d(String.format("EIT_presen_following_flag : 0x%x \n",  
                EIT_present_following_flag));
        Logger.d(String.format("running_status : 0x%x \n", running_status));
        Logger.d(String.format("free_CA_mode : 0x%x \n", free_CA_mode));
        Logger.d(String.format("descriptors_loop_length : 0x%x \n", 
                descriptors_loop_length));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            Descriptor desc = descriptors.get(i);
            desc.print();
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }

}
