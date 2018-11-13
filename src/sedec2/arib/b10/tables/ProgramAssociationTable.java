package sedec2.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Table;
import sedec2.util.Logger;

public class ProgramAssociationTable extends Table {
    public ProgramAssociationTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    private int transport_stream_id;
    private byte version_number;
    private byte current_next_indicator;
    private byte section_number;
    private byte last_section_number;
    private List<Program> programs = new ArrayList<>();
    
    public class Program {
        private int program_number;
        private int pid;
        Program(int _program_number, int _pid) {
            program_number = _program_number;
            pid = _pid;
        }
        
        public int getProgramNumber() {
            return program_number;
        }
        
        public int getPid() {
            return pid;
        }
    }
    
    public int getTransportStreamId() {
        return transport_stream_id;
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
    
    public List<Program> getPrograms() {
        return programs;
    }
    
    @Override
    protected void __decode_table_body__() {
        transport_stream_id = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);

        for ( int i=(section_length-5-4); i>0; ) {
            int program_number = readOnBuffer(16);
            skipOnBuffer(3);
            int pid = readOnBuffer(13);
            Program program = new Program(program_number, pid);
            programs.add(program);
            i-=4;
        }
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("transport_stream_id : 0x%x \n", transport_stream_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));

        for ( int i=0; i<programs.size(); i++ ) {
            Logger.d(String.format("\t program_number : 0x%x \n", programs.get(i).getProgramNumber()));
            Logger.d(String.format("\t pid : 0x%x \n", programs.get(i).getPid()));
        }
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                        (checksum_CRC32 >> 24) & 0xff,
                        (checksum_CRC32 >> 16) & 0xff,
                        (checksum_CRC32 >> 8) & 0xff,
                        checksum_CRC32 & 0xff));
        Logger.d("====================================== \n\n");
    }
}
