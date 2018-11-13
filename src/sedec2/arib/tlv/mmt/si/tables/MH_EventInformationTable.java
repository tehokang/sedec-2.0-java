package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

public class MH_EventInformationTable extends Table {
    protected int service_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int tlv_stream_id;
    protected int original_network_id;
    protected byte segment_last_section_number;
    protected byte last_table_id;
    protected List<Event> events = new ArrayList<>();
    
    public class Event {
        public int event_id;
        public long start_time;
        public int duration;
        public byte running_status;
        public byte free_CA_mode;
        public int descriptors_loop_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    public MH_EventInformationTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public int getServiceId() {
        return service_id;
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
    
    public int getTlvStreamId() {
        return tlv_stream_id;
    }
    
    public int getOriginalNetworkId() {
        return original_network_id;
    }
    
    public byte getSegmentLastSectionNumber() {
        return segment_last_section_number;
    }
    
    public byte getLastTableId() {
        return last_table_id;
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    @Override
    protected void __decode_table_body__() {
        service_id = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        tlv_stream_id = readOnBuffer(16);
        original_network_id = readOnBuffer(16);
        segment_last_section_number = (byte) readOnBuffer(8);
        last_table_id = (byte) readOnBuffer(8);
        
        for ( int i=(section_length-11-4); i>0; ) {
            Event event = new Event();
            event.event_id = readOnBuffer(16);
            event.start_time = readOnBuffer(40);
            event.duration = readOnBuffer(24);
            event.running_status = (byte) readOnBuffer(3);
            event.free_CA_mode = (byte) readOnBuffer(1);
            event.descriptors_loop_length = readOnBuffer(12);
            /**
             * @todo event.descriptors_loop_length has wrong length of descriptors
             * in field stream, for instance CAS-A-0000-03b-bsc.tlv
             */
            for ( int j=event.descriptors_loop_length-4; j>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
                j-=desc.getDescriptorLength();
                event.descriptors.add(desc);
            }
            i-= (12 + event.descriptors_loop_length);
            events.add(event);
        }
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("service_id : 0x%x \n", service_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("tlv_stream_id : 0x%x \n", tlv_stream_id));
        Logger.d(String.format("original_network_id : 0x%x \n", original_network_id));
        Logger.d(String.format("segment_last_section_number : 0x%x \n", 
                segment_last_section_number));
        Logger.d(String.format("last_table_id : 0x%x \n", last_table_id));
        
        for ( int i=0; i<events.size(); i++ ) {
            Event event = events.get(i);
            Logger.d(String.format("\t [%d] event_id : 0x%x \n", i, event.event_id));
            Logger.d(String.format("\t [%d] start_time : 0x%x \n", i, event.start_time));
            Logger.d(String.format("\t [%d] duration : 0x%x \n", i, event.duration));
            Logger.d(String.format("\t [%d] running_status : 0x%x \n", i, event.running_status));
            Logger.d(String.format("\t [%d] free_CA_mode : 0x%x \n", i, event.free_CA_mode));
            Logger.d(String.format("\t [%d] descriptors_loop_length : 0x%x \n\n", 
                    i, event.descriptors_loop_length));
            
            for ( int j=0; j<event.descriptors.size(); j++ ) {
                Descriptor desc = event.descriptors.get(j);
                desc.print();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }

}
