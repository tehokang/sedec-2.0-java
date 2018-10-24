package sedec2.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Table;
import sedec2.arib.b10.DescriptorFactory;
import sedec2.arib.b10.descriptors.Descriptor;
import sedec2.util.Logger;

public class ProgramMapTable extends Table {
    private int program_number;
    private byte version_number;
    private byte current_next_indicator;
    private byte section_number;
    private byte last_section_number;
    private int PCR_PID;
    private int program_info_length;
    List<Descriptor> descriptors = new ArrayList<>();
    List<Program> programs = new ArrayList<>();
    
    class Program {
        public byte stream_type;
        public int elementary_PID;
        public int ES_info_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    public ProgramMapTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        program_number = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        skipOnBuffer(3);
        PCR_PID = readOnBuffer(13);
        skipOnBuffer(4);
        program_info_length = readOnBuffer(12);

        for ( int i=program_info_length; i>0; ) {
            Descriptor desc = DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }

        for ( int i=(section_length-9-program_info_length-4); i>0; )
        {
            Program program = new Program();
            program.stream_type = (byte) readOnBuffer(8);
            skipOnBuffer(3);
            program.elementary_PID = readOnBuffer(13);
            skipOnBuffer(4);
            program.ES_info_length = readOnBuffer(12);

            for ( int j=program.ES_info_length; j>0; )
            {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                j-=desc.getDescriptorLength();
                program.descriptors.add(desc);
            }
            i-= (5 + program.ES_info_length);
            programs.add(program);
        }
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("program_number : 0x%x \n", program_number));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("PCR_PID : 0x%x \n", PCR_PID));
        Logger.d(String.format("program_info_length : 0x%x \n", program_info_length));

        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }

        for ( int i=0; i<programs.size(); i++ ) {
            Program program = programs.get(i);
            Logger.d(String.format("  [%d] Program \n", i));
            Logger.d(String.format("  stream_type : 0x%x \n", program.stream_type));
            Logger.d(String.format("  elementary_PID : 0x%x \n", program.elementary_PID));
            Logger.d(String.format("  ES_info_length : 0x%x \n", program.ES_info_length));

            for ( int j=0; j<program.descriptors.size(); j++ ) {
                program.descriptors.get(j).print();
            }
        }

        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                        (checksum_CRC32 >> 24) & 0xff,
                        (checksum_CRC32 >> 16) & 0xff,
                        (checksum_CRC32 >> 8) & 0xff,
                        checksum_CRC32 & 0xff));
        Logger.d("====================================== \n\n");
    }
    
}
