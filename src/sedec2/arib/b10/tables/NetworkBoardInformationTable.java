package sedec2.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.b10.DescriptorFactory;
import sedec2.arib.b10.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

/**
 * @brief ARIB-B10 NBIT
 */
public class NetworkBoardInformationTable extends Table {
    protected int original_network_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected List<BoardInformation> board_informations = new ArrayList<>();
    
    public class BoardInformation {
        public int information_id;
        public byte information_type;
        public byte description_body_location;
        public byte user_defined;
        public byte number_of_keys;
        public int []key_id;
        public int descriptors_loop_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    public NetworkBoardInformationTable(byte[] buffer) {
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
    
    public List<BoardInformation> getBoardInformations() {
        return board_informations;
    }
    
    @Override
    protected void __decode_table_body__() {
        original_network_id = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        
        for ( int i=(section_length-5-4); i>0; ) {
            BoardInformation board_information = new BoardInformation();
            board_information.information_id = readOnBuffer(16);
            board_information.information_type = (byte) readOnBuffer(4);
            board_information.description_body_location = (byte) readOnBuffer(2);
            skipOnBuffer(2);
            board_information.user_defined = (byte) readOnBuffer(8);
            board_information.number_of_keys = (byte) readOnBuffer(8);
            
            board_information.key_id = new int[board_information.number_of_keys];
            for ( int j=0; j<board_information.key_id.length; j++ ) {
                board_information.key_id[j] = readOnBuffer(16);
            }
            skipOnBuffer(4);
            board_information.descriptors_loop_length = readOnBuffer(12);
            
            for ( int k=board_information.descriptors_loop_length; k>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
                k-=desc.getDescriptorLength();
                board_information.descriptors.add(desc);
            }
            i-= ( 5 + board_information.key_id.length*2 + 
                    2 + board_information.description_body_location);
            board_informations.add(board_information);
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
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
     
        for ( int i=0; i<board_informations.size(); i++) {
            BoardInformation board_information = board_informations.get(i);
            
            Logger.d(String.format("\t [%d] information_id : 0x%x \n", 
                    i, board_information.information_id));
            Logger.d(String.format("\t [%d] information_type : 0x%x \n", 
                    i, board_information.information_type));
            Logger.d(String.format("\t [%d] description_body_location : 0x%x \n", 
                    i, board_information.description_body_location));
            Logger.d(String.format("\t [%d] user_defined : 0x%x \n", 
                    i, board_information.user_defined));
            Logger.d(String.format("\t [%d] number_of_keys : 0x%x \n", 
                    i, board_information.number_of_keys));
            
            for ( int j=0; j<board_information.key_id.length; j++ ) {
                Logger.d(String.format("\t [%d] key_id[%d] : 0x%x \n", 
                        i, j, board_information.key_id[j]));
            }
            
            Logger.d(String.format("\t [%d] descriptors_loop_length : 0x%x \n", 
                    i, board_information.descriptors_loop_length));
            
            for ( int k=board_information.descriptors_loop_length; k>0; ) {
                board_information.descriptors.get(k).print();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }

}
