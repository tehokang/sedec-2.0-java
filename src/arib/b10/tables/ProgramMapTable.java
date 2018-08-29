package arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import base.Table;
import dvb.DescriptorFactory;
import dvb.descriptors.Descriptor;
import util.Logger;

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
        program_number = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        SkipOnBuffer(3);
        PCR_PID = ReadOnBuffer(13);
        SkipOnBuffer(4);
        program_info_length = ReadOnBuffer(12);

        for ( int i=program_info_length; i>0; ) {
            Descriptor desc = DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }

        for ( int i=(section_length-9-program_info_length-4); i>0; )
        {
            Program program = new Program();
            program.stream_type = (byte) ReadOnBuffer(8);
            SkipOnBuffer(3);
            program.elementary_PID = ReadOnBuffer(13);
            SkipOnBuffer(4);
            program.ES_info_length = ReadOnBuffer(12);

            for ( int j=program.ES_info_length; j>0; )
            {
                Descriptor desc = DescriptorFactory.CreateDescriptor(this);
                j-=desc.GetDescriptorLength();
                program.descriptors.add(desc);
            }
            i-= (5 + program.ES_info_length);
            programs.add(program);
        }
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("program_number : 0x%x \n", program_number));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("PCR_PID : 0x%x \n", PCR_PID));
        Logger.d(String.format("program_info_length : 0x%x \n", program_info_length));

        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }

        for ( int i=0; i<programs.size(); i++ ) {
            Program program = programs.get(i);
            Logger.d(String.format("\t[%d] Program \n", i));
            Logger.d(String.format("\tstream_type : 0x%x \n", program.stream_type));
            Logger.d(String.format("\telementary_PID : 0x%x \n", program.elementary_PID));
            Logger.d(String.format("\tES_info_length : 0x%x \n", program.ES_info_length));

            for ( int j=0; j<program.descriptors.size(); j++ ) {
                program.descriptors.get(j).PrintDescriptor();
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
