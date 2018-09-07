package arib.b39.tables;

import java.util.ArrayList;
import java.util.List;

import arib.b39.DescriptorFactory;
import arib.b39.descriptors.Descriptor;
import base.Table;
import util.Logger;

public class MPEGH_ServiceDescriptionTable extends Table {
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
    
    public MPEGH_ServiceDescriptionTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public int GetTlvStreamId() {
        return tlv_stream_id;
    }
    
    public byte GetVersionNumber() {
        return version_number;
    }
    
    public byte GetCurrentNextIndicator() {
        return current_next_indicator;
    }
    
    public byte GetSectionNumber() {
        return section_number;
    }
    
    public byte GetLastSectionNumber() {
        return last_section_number;
    }
    
    public int GetOriginalNetworkId() {
        return original_network_id;
    }
    
    public int GetServiceId() {
        return service_id;
    }
    
    public byte GetEITUserDefinedFlags() {
        return EIT_user_defined_flags;
    }
    
    public byte GetEITScheduleFlag() {
        return EIT_schedule_flag;
    }
    
    public byte GetEITPresentFollowingFlag() {
        return EIT_present_following_flag;
    }
    
    public byte GetRunningStatus() {
        return running_status;
    }
    
    public byte GetFreeCAMode() {
        return free_CA_mode;
    }
    
    public List<Descriptor> GetDescriptors() {
        return descriptors;
    }
    
    @Override
    protected void __decode_table_body__() {
        tlv_stream_id = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        original_network_id = ReadOnBuffer(16);
        SkipOnBuffer(8);
        
        service_id = ReadOnBuffer(16);
        SkipOnBuffer(3);
        EIT_user_defined_flags = (byte) ReadOnBuffer(3);
        EIT_schedule_flag = (byte) ReadOnBuffer(1);
        EIT_present_following_flag = (byte) ReadOnBuffer(1);
        running_status = (byte) ReadOnBuffer(3);
        free_CA_mode = (byte) ReadOnBuffer(1);
        
        descriptors_loop_length = ReadOnBuffer(12);
        for ( int i=descriptors_loop_length;i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
        
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
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
            desc.PrintDescriptor();
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }

}
