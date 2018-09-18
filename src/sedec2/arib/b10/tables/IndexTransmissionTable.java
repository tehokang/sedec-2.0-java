package sedec2.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.b10.DescriptorFactory;
import sedec2.arib.b10.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

public class IndexTransmissionTable extends Table {
    protected int event_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int descriptors_loop_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    
    public IndexTransmissionTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        event_id = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        SkipOnBuffer(4);
        descriptors_loop_length = ReadOnBuffer(12);
        
        for ( int i=descriptors_loop_length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("event_id : 0x%x \n", event_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", 
                current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", 
                last_section_number));
        Logger.d(String.format("descriptors_loop_length : 0x%x \n", 
                descriptors_loop_length));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }

}