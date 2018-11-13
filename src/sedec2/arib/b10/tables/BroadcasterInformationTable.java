package sedec2.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.b10.DescriptorFactory;
import sedec2.arib.b10.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

/**
 * @brief ARIB-B10 BIT
 */
public class BroadcasterInformationTable extends Table {
    protected int original_network_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected byte broadcast_view_priority;
    protected int first_descriptors_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    protected List<Broadcaster> broadcasters = new ArrayList<>();
    
    public class Broadcaster {
        public byte broadcaster_id;
        public int broadcaster_descriptors_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    public BroadcasterInformationTable(byte[] buffer) {
        super(buffer);
       
        __decode_table_body__();
    }

    public int getOriginalNetworkId() {
        return original_network_id;
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
    
    public byte getBroadcastViewPriority() {
        return broadcast_view_priority;
    }
    
    public int getFirstDescriptorsLength() {
        return first_descriptors_length;
    }
    
    public List<Descriptor> getDescriptors() {
        return descriptors;
    }
    
    public List<Broadcaster> getBroadcasters() {
        return broadcasters;
    }
    
    @Override
    protected void __decode_table_body__() {
        original_network_id = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        skipOnBuffer(3);
        broadcast_view_priority = (byte) readOnBuffer(1);
        first_descriptors_length = readOnBuffer(12);
        
        for ( int i=first_descriptors_length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }
        
        for ( int i=(section_length - 7 - first_descriptors_length - 4); i>0 ; ) {
            Broadcaster broadcaster = new Broadcaster();
            broadcaster.broadcaster_id = (byte) readOnBuffer(8);
            skipOnBuffer(4);
            broadcaster.broadcaster_descriptors_length = readOnBuffer(12);
            
            for ( int j=broadcaster.broadcaster_descriptors_length; j>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
                j-=desc.getDescriptorLength();
                broadcaster.descriptors.add(desc);
            }
            broadcasters.add(broadcaster);
        }
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("original_network_id : 0x%x \n", original_network_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n",  last_section_number));
        Logger.d(String.format("broadcast_view_priority : 0x%x \n", broadcast_view_priority));
        Logger.d(String.format("first_descriptors_length : 0x%x \n",  first_descriptors_length));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }
        
        for ( int i=0; i<broadcasters.size(); i++ ) {
            Broadcaster broadcaster = broadcasters.get(i);
            
            Logger.d(String.format("\t [%d] broadcaster_id : 0x%x \n", 
                    i, broadcaster.broadcaster_id));
            Logger.d(String.format("\t [%d] broadcaster_descriptors_length : 0x%x \n", 
                    i, broadcaster.broadcaster_descriptors_length));
            
            for ( int j=0; j<broadcaster.descriptors.size(); j++ ) {
                broadcaster.descriptors.get(j).print();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%x%x%x%x \n", 
                ((checksum_CRC32 >> 24) & 0xff), 
                ((checksum_CRC32 >> 16) & 0xff), 
                ((checksum_CRC32 >> 8) & 0xff), 
                (checksum_CRC32 & 0xff)));
    }
}
