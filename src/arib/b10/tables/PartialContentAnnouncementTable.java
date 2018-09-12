package arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import arib.b10.DescriptorFactory;
import arib.b10.descriptors.Descriptor;
import base.Table;
import util.Logger;

/**
 * @brief ARIB-B10 PCAT
 */
public class PartialContentAnnouncementTable extends Table {
    protected int service_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int transport_stream_id;
    protected int original_network_id;
    protected int content_id;
    protected byte num_of_content_version;
    protected List<ContentVersion> content_versions = new ArrayList<>();
    
    class ContentVersion {
        public int content_version;
        public int content_minor_version;
        public byte version_indicator;
        public int content_descriptor_length;
        public int schedule_description_length;
        public List<Schedule> schedule_descriptions = new ArrayList<>();
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    class Schedule {
        public long start_time;
        public long duration;
    }
    
    public PartialContentAnnouncementTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        service_id = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        transport_stream_id = ReadOnBuffer(16);
        original_network_id = ReadOnBuffer(16);
        content_id = ReadOnBuffer(32);
        num_of_content_version = (byte) ReadOnBuffer(8);
        
        for ( int i=0; i<num_of_content_version; i++ ) {
            ContentVersion content_version = new ContentVersion();
            content_version.content_version = ReadOnBuffer(16);
            content_version.content_minor_version = ReadOnBuffer(16);
            content_version.version_indicator = (byte) ReadOnBuffer(2);
            SkipOnBuffer(2);
            content_version.content_descriptor_length = ReadOnBuffer(12);
            SkipOnBuffer(4);
            content_version.schedule_description_length = ReadOnBuffer(12);
            
            for ( int j=content_version.schedule_description_length; j>0; ) {
                Schedule schedule_description = new Schedule();
                schedule_description.start_time = ReadOnBuffer(40);
                schedule_description.duration = ReadOnBuffer(24);
                content_version.schedule_descriptions.add(schedule_description);
                j-=8;
            }
            
            for ( int k=(content_version.content_descriptor_length-
                    content_version.schedule_description_length); k>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
                k-=desc.GetDescriptorLength();
                content_version.descriptors.add(desc);
            }
            content_versions.add(content_version);
        }
        
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("service_id : 0x%x \n", service_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("transport_stream_id : 0x%x \n", transport_stream_id));
        Logger.d(String.format("original_network_id : 0x%x \n", original_network_id));
        Logger.d(String.format("content_id : 0x%x \n", content_id));
        Logger.d(String.format("num_of_content_version : 0x%x \n", num_of_content_version));
        
        for ( int i=0; i<content_versions.size(); i++ ) {
            ContentVersion content_version = content_versions.get(i);
            
            Logger.d(String.format("\t [%d] content_version : 0x%x \n", 
                    content_version.content_version));
            Logger.d(String.format("\t [%d] content_minor_version : 0x%x \n", 
                    content_version.content_minor_version));
            Logger.d(String.format("\t [%d] version_indicator : 0x%x \n", 
                    content_version.version_indicator));
            Logger.d(String.format("\t [%d] content_descriptor_length : 0x%x \n", 
                    content_version.content_descriptor_length));
            Logger.d(String.format("\t [%d] schedule_description_length : 0x%x \n", 
                    content_version.schedule_description_length));
            
            for ( int j=0; j<content_version.schedule_descriptions.size(); j++ ) {
                Schedule schedule_description = 
                        content_version.schedule_descriptions.get(j);
                Logger.d(String.format("\t [%d] start_time : 0x%x \n", 
                        schedule_description.start_time));
                Logger.d(String.format("\t [%d] duration : 0x%x \n", 
                        schedule_description.duration));
            }
            
            for ( int j=0; j<content_version.descriptors.size(); j++ ) {
                content_version.descriptors.get(j).PrintDescriptor();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
