package arib.b39.tables;

import java.util.ArrayList;
import java.util.List;

import arib.b10.DescriptorFactory;
import arib.b10.descriptors.Descriptor;
import base.Table;
import util.Logger;

/**
 * @brief ARIB-B60
 */
public class MPEGH_SoftwareDownloadTriggerTable extends Table {
    protected int table_id_ext;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int tlv_stream_id;
    protected int original_network_id;
    protected int service_id;
    protected byte num_of_contents;
    protected List<Content> contents = new ArrayList<>();
    
    class Content {
        public byte group;
        public int target_version;
        public int new_version;
        public byte download_level;
        public byte version_indicator;
        public int content_description_length;
        public int schedule_description_length;
        public byte schedule_time_shift_information;
        List<Schedule> schedules = new ArrayList<>();
        List<Descriptor> descriptors = new ArrayList<>();
    }
    
    class Schedule {
        public long start_time;
        public int duration;
    }
    
    public MPEGH_SoftwareDownloadTriggerTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        table_id_ext = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        tlv_stream_id = ReadOnBuffer(16);
        original_network_id = ReadOnBuffer(16);
        service_id = ReadOnBuffer(16);
        num_of_contents = (byte) ReadOnBuffer(8);
        
        for ( int i=0; i<num_of_contents; i++ ) {
            Content content = new Content();
            content.group = (byte) ReadOnBuffer(4);
            content.target_version = ReadOnBuffer(12);
            content.new_version = ReadOnBuffer(12);
            content.download_level = (byte) ReadOnBuffer(2);
            content.version_indicator = (byte) ReadOnBuffer(2);
            content.content_description_length = ReadOnBuffer(12);
            SkipOnBuffer(4);
            content.schedule_description_length = ReadOnBuffer(12);
            content.schedule_time_shift_information = (byte) ReadOnBuffer(4);
            
            for ( int j=content.schedule_description_length; j>0; ) {
                Schedule schedule = new Schedule();
                schedule.start_time = ReadOnBuffer(40);
                schedule.duration = ReadOnBuffer(24);
                content.schedules.add(schedule);
                j-=8;
            }
            
            for ( int k=content.content_description_length; k>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
                k-=desc.GetDescriptorLength();
                content.descriptors.add(desc);
            }
            contents.add(content);
        }
        
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("table_id_ext : 0x%x \n", table_id_ext));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", 
                current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", 
                last_section_number));
        Logger.d(String.format("transport_stream_id : 0x%x \n", 
                tlv_stream_id));
        Logger.d(String.format("original_network_id : 0x%x \n", 
                original_network_id));
        Logger.d(String.format("service_id : 0x%x \n", 
                service_id));
        Logger.d(String.format("num_of_contents : 0x%x \n", 
                num_of_contents));
        
        for ( int i=0; i<contents.size(); i++ ) {
            Content content = contents.get(i);
            Logger.d(String.format("\t [%d] group : 0x%x \n", i, content.group));
            Logger.d(String.format("\t [%d] target_version : 0x%x \n", 
                    i, content.target_version));
            Logger.d(String.format("\t [%d] new_version : 0x%x \n", 
                    i, content.new_version));
            Logger.d(String.format("\t [%d] download_level : 0x%x \n", 
                    i, content.download_level));
            Logger.d(String.format("\t [%d] version_indicator : 0x%x \n", 
                    i, content.version_indicator));
            Logger.d(String.format("\t [%d] content_description_length : 0x%x \n", 
                    i, content.content_description_length));
            Logger.d(String.format("\t [%d] schedule_description_length : 0x%x \n", 
                    i, content.schedule_description_length));
            Logger.d(String.format("\t [%d] schedule_time_shift_information : 0x%x \n", 
                    i, content.schedule_time_shift_information));
            
            for ( int j=0; j<content.schedules.size(); j++ ) {
                Schedule schedule = content.schedules.get(j);
                Logger.d(String.format("\t [%d] start_time : 0x%x \n", 
                        j, schedule.start_time));
                Logger.d(String.format("\t [%d] duration : 0x%x \n", 
                        j, schedule.duration));
            }
            
            for ( int j=0; j<content.descriptors.size(); j++ ) {
                content.descriptors.get(j).PrintDescriptor();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
