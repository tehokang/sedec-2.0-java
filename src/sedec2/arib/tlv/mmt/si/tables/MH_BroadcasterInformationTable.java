package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

/**
 * @brief ARIB-B10 BIT
 */
public class MH_BroadcasterInformationTable extends Table {
    protected int original_network_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected byte broadcast_view_priority;
    protected int first_descriptors_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    protected List<Broadcaster> broadcasters = new ArrayList<>();
    
    class Broadcaster {
        public byte broadcaster_id;
        public int broadcaster_descriptors_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    public MH_BroadcasterInformationTable(byte[] buffer) {
        super(buffer);
       
        __decode_table_body__();
    }

    public int GetOriginalNetworkId() {
        return original_network_id;
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
    
    public byte GetBroadcastViewPriority() {
        return broadcast_view_priority;
    }
    
    public int GetFirstDescriptorsLength() {
        return first_descriptors_length;
    }
    
    public List<Descriptor> GetDescriptors() {
        return descriptors;
    }
    
    public List<Broadcaster> GetBroadcasters() {
        return broadcasters;
    }
    
    @Override
    protected void __decode_table_body__() {
        original_network_id = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        SkipOnBuffer(3);
        broadcast_view_priority = (byte) ReadOnBuffer(1);
        first_descriptors_length = ReadOnBuffer(12);
        
        for ( int i=first_descriptors_length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
        
        for ( int i=(section_length - 7 - first_descriptors_length - 4); i>0; ) {
            Broadcaster broadcaster = new Broadcaster();
            broadcaster.broadcaster_id = (byte) ReadOnBuffer(8);
            SkipOnBuffer(4);
            broadcaster.broadcaster_descriptors_length = ReadOnBuffer(12);
            i-=3;
            
            for ( int j=broadcaster.broadcaster_descriptors_length; j>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
                j-=desc.GetDescriptorLength();
                i-=desc.GetDescriptorLength();
                broadcaster.descriptors.add(desc);
            }
            broadcasters.add(broadcaster);
        }
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("original_network_id : 0x%x \n", original_network_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n",  last_section_number));
        Logger.d(String.format("broadcast_view_priority : 0x%x \n", broadcast_view_priority));
        Logger.d(String.format("first_descriptors_length : 0x%x \n",  first_descriptors_length));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }
        
        for ( int i=0; i<broadcasters.size(); i++ ) {
            Broadcaster broadcaster = broadcasters.get(i);
            
            Logger.d(String.format("\t [%d] broadcaster_id : 0x%x \n", 
                    i, broadcaster.broadcaster_id));
            Logger.d(String.format("\t [%d] broadcaster_descriptors_length : 0x%x \n", 
                    i, broadcaster.broadcaster_descriptors_length));
            
            for ( int j=0; j<broadcaster.descriptors.size(); j++ ) {
                broadcaster.descriptors.get(j).PrintDescriptor();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%x%x%x%x \n", 
                ((checksum_CRC32 >> 24) & 0xff), 
                ((checksum_CRC32 >> 16) & 0xff), 
                ((checksum_CRC32 >> 8) & 0xff), 
                (checksum_CRC32 & 0xff)));
    }
}
