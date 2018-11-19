package sedec2.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MH_EventGroupDescriptor extends Descriptor {
    protected byte group_type;
    protected byte event_count;
    protected List<Event> events = new ArrayList<>();
    protected List<Group> groups = new ArrayList<>();
    protected byte[] private_data_byte;
    
    public class Event {
        public int service_id;
        public int event_id;
    }
    
    public class Group {
        public int original_network_id;
        public int tlv_stream_id;
        public int service_id;
        public int event_id;
    }
    
    public MH_EventGroupDescriptor(BitReadWriter brw) {
        super(brw);
        
        group_type = (byte) brw.readOnBuffer(4);
        event_count = (byte) brw.readOnBuffer(4);
        
        for ( int i=0; i<event_count; i++ ) {
            Event event = new Event();
            event.service_id = brw.readOnBuffer(16);
            event.event_id = brw.readOnBuffer(16);
            events.add(event);
        }
        
        if ( group_type == 4 || group_type == 5 ) {
            for ( int i=(descriptor_length-1-(event_count*4)); i>0; ) {
                Group group = new Group();
                group.original_network_id = brw.readOnBuffer(16);
                group.tlv_stream_id = brw.readOnBuffer(16);
                group.service_id = brw.readOnBuffer(16);
                group.event_id = brw.readOnBuffer(16);
                groups.add(group);
                i-=8;
            }
        } else {
            private_data_byte = new byte[descriptor_length-1-(event_count*4)];
            for ( int i=0; i<private_data_byte.length; i++ ) {
                private_data_byte[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public byte getGroupType() {
        return group_type;
    }
    
    public byte getEventCount() {
        return event_count;
    }
    
    public List<Event> getEvents() {
        return events;
    }
    
    public List<Group> getGroups() {
        return groups;
    }
    
    public byte[] getPrivateDataByte() {
        return private_data_byte;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t group_type : 0x%x \n", group_type));
        Logger.d(String.format("\t event_count : 0x%x \n", event_count));
        
        for ( int i=0; i<event_count; i++ ) {
            Logger.d(String.format("\t [%d] service_id : 0x%x \n", 
                    i, events.get(i).service_id));
            Logger.d(String.format("\t [%d] event_id : 0x%x \n", 
                    i, events.get(i).event_id));
        }
        
        if ( group_type == 4 || group_type == 5 ) {
            for ( int i=0; i<groups.size(); i++ ) {
                Group group = groups.get(i);
                Logger.d(String.format("\t [%d] original_network_id : 0x%x \n", 
                        i, group.original_network_id));
                Logger.d(String.format("\t [%d] tlv_stream_id : 0x%x \n", 
                        i, group.tlv_stream_id));
                Logger.d(String.format("\t [%d] service_id : 0x%x \n", 
                        i, group.service_id));
                Logger.d(String.format("\t [%d] event_id : 0x%x \n", 
                        i, group.event_id));
            }
        } else {
            Logger.d("private_data_byte : \n");
            BinaryLogger.print(private_data_byte);
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + (events.size()*2);
        
        if ( group_type == 4 || group_type == 5 ) {
            for ( int i=0; i<groups.size(); i++ ) {
                descriptor_length += 8;
            }
        } else {
            descriptor_length += private_data_byte.length;
        }
    }
}
