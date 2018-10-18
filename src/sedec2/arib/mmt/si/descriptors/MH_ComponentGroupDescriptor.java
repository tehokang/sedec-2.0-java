package sedec2.arib.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ComponentGroupDescriptor extends Descriptor {
    protected byte component_group_type;
    protected byte total_bit_rate_flag;
    protected byte num_of_group;
    protected List<Group> groups = new ArrayList<>();
    
    class Group {
        public byte component_group_id;
        public byte num_of_CA_unit;
        public List<CAUnit> ca_units = new ArrayList<>();
        public byte total_bit_rate;
        public byte text_length;
        public byte[] text_char;
    }
    
    class CAUnit {
        public byte CA_unit_id;
        public byte num_of_component;
        public byte[] component_tag;
    }
    
    public MH_ComponentGroupDescriptor(BitReadWriter brw) {
        super(brw);
        
        component_group_type = (byte) brw.ReadOnBuffer(3);
        total_bit_rate_flag = (byte) brw.ReadOnBuffer(1);
        num_of_group = (byte) brw.ReadOnBuffer(4);
        
        for ( int i=0; i<num_of_group; i++ ) {
            Group group = new Group();
            group.component_group_id = (byte) brw.ReadOnBuffer(4);
            group.num_of_CA_unit = (byte) brw.ReadOnBuffer(4);
            
            for ( int j=0; j<group.num_of_CA_unit; j++ ) {
                CAUnit ca_unit = new CAUnit();
                ca_unit.CA_unit_id = (byte) brw.ReadOnBuffer(4);
                ca_unit.num_of_component = (byte) brw.ReadOnBuffer(4);
                ca_unit.component_tag = new byte[ca_unit.num_of_component*2];
                
                for ( int k=0; k<ca_unit.component_tag.length; k++ ) {
                    ca_unit.component_tag[k] = (byte) brw.ReadOnBuffer(16);
                }
                group.ca_units.add(ca_unit);
            }
            
            if ( total_bit_rate_flag == 1) {
                group.total_bit_rate = (byte) brw.ReadOnBuffer(8);
            }
            
            group.text_length = (byte) brw.ReadOnBuffer(8);
            group.text_char = new byte[group.text_length];
            
            for ( int j=0; j<group.text_char.length; j++ ) {
                group.text_char[j] = (byte) brw.ReadOnBuffer(8);
            }
            
            groups.add(group);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t component_group_type : 0x%x \n", component_group_type));
        Logger.d(String.format("\t total_bit_rate_flag : 0x%x \n", total_bit_rate_flag));
        Logger.d(String.format("\t num_of_group : 0x%x \n", num_of_group));
        
        for ( int i=0; i<groups.size(); i++ ) {
            Group group = groups.get(i);
            Logger.d(String.format("\t [%d] component_group_id : 0x%x \n",
                    i, group.component_group_id));
            Logger.d(String.format("\t [%d] num_of_CA_unit : 0x%x \n",
                    i, group.num_of_CA_unit));
            
            for ( int j=0; j<group.ca_units.size(); j++ ) {
                CAUnit ca_unit = group.ca_units.get(j);
                
                Logger.d(String.format("\t\t [%d] CA_unit_id : 0x%x \n", 
                        j, ca_unit.CA_unit_id));
                Logger.d(String.format("\t\t [%d] num_of_component : 0x%x \n", 
                        j, ca_unit.num_of_component));
                
                for ( int k=0; k<ca_unit.component_tag.length; k++ ) {
                    Logger.d(String.format("\t\t\t [%d] component_tag : 0x%x \n", 
                            k, ca_unit.component_tag[k]));
                }
            }
            
            if ( total_bit_rate_flag == 1) {
                Logger.d(String.format("\t [%d] total_bit_rate : 0x%x \n",
                        i, group.total_bit_rate));
            }
            
            Logger.d(String.format("\t [%d] total_bit_rate : 0x%x \n",
                    i, group.text_length));
            Logger.d(String.format("\t [%d] total_bit_rate : %s \n",
                    i, new String(group.text_char)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
        
        for ( int i=0; i<groups.size(); i++ ) {
            Group group = groups.get(i);
            descriptor_length += 1;
            
            for ( int j=0; j<group.ca_units.size(); j++ ) {
                CAUnit ca_unit = group.ca_units.get(j);
                descriptor_length += (1 + (ca_unit.num_of_component*2));
            }
            
            if ( total_bit_rate_flag == 1) {
                descriptor_length += 1;
            }
            
            descriptor_length += (1 + group.text_char.length);
        }
    }
}
