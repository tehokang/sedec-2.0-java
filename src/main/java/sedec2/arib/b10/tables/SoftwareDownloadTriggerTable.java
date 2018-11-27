package sedec2.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.b10.DescriptorFactory;
import sedec2.arib.b10.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

/**
 * ARIB-B21 from B10 SDTT
 */
public class SoftwareDownloadTriggerTable extends Table {
    protected int table_id_ext;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int transport_stream_id;
    protected int original_network_id;
    protected int service_id;
    protected byte num_of_contents;
    protected List<Content> contents = new ArrayList<>();

    public class Content {
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

    public class Schedule {
        public long start_time;
        public int duration;
    }

    public SoftwareDownloadTriggerTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public int getTableIdExt() {
        return table_id_ext;
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

    public int getTransportStreamId() {
        return transport_stream_id;
    }

    public int getOriginalNetworkId() {
        return original_network_id;
    }

    public int getServiceId() {
        return service_id;
    }

    public byte getNumOfContents() {
        return num_of_contents;
    }

    public List<Content> getContents() {
        return contents;
    }

    @Override
    protected void __decode_table_body__() {
        table_id_ext = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        transport_stream_id = readOnBuffer(16);
        original_network_id = readOnBuffer(16);
        service_id = readOnBuffer(16);
        num_of_contents = (byte) readOnBuffer(8);

        for ( int i=0; i<num_of_contents; i++ ) {
            Content content = new Content();
            content.group = (byte) readOnBuffer(4);
            content.target_version = readOnBuffer(12);
            content.new_version = readOnBuffer(12);
            content.download_level = (byte) readOnBuffer(2);
            content.version_indicator = (byte) readOnBuffer(2);
            content.content_description_length = readOnBuffer(12);
            skipOnBuffer(4);
            content.schedule_description_length = readOnBuffer(12);
            content.schedule_time_shift_information = (byte) readOnBuffer(4);

            for ( int j=content.schedule_description_length; j>0; ) {
                Schedule schedule = new Schedule();
                schedule.start_time = readOnBuffer(40);
                schedule.duration = readOnBuffer(24);
                content.schedules.add(schedule);
                j-=8;
            }

            for ( int k=content.content_description_length; k>0; ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                k-=desc.getDescriptorLength();
                content.descriptors.add(desc);
            }
            contents.add(content);
        }

        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("table_id_ext : 0x%x \n", table_id_ext));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n",
                current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n",
                last_section_number));
        Logger.d(String.format("transport_stream_id : 0x%x \n",
                transport_stream_id));
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
                content.descriptors.get(j).print();
            }
        }

        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
