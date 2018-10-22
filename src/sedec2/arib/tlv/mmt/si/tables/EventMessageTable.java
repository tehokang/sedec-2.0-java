package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

public class EventMessageTable extends Table {
    protected byte data_event_id;
    protected int event_msg_group_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected List<Descriptor> descriptors = new ArrayList<>();
    
    public EventMessageTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }
    
    public int GetDataEventId() {
        return data_event_id;
    }
    
    public int GetEventMsgGroupId() {
        return event_msg_group_id;
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
    
    public List<Descriptor> GetDescriptors() {
        return descriptors;
    }
    
    @Override
    protected void __decode_table_body__() {
        data_event_id = (byte) ReadOnBuffer(4);
        event_msg_group_id = (byte) ReadOnBuffer(12);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        
        for ( int i=(section_length-5-4); i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
        
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("data_event_id : 0x%x \n", data_event_id));
        Logger.d(String.format("event_msg_group_id : 0x%x \n", event_msg_group_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }
    }
}
