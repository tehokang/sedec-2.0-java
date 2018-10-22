package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_CAContractInfoDescriptor extends Descriptor {
    protected int CA_system_ID;
    protected byte CA_unit_id;
    protected byte num_of_component;
    protected int[] component_tag;
    protected byte contract_verification_info_length;
    protected byte[] contract_verification_info;
    protected byte fee_name_length;
    protected byte[] fee_name;
    
    public MH_CAContractInfoDescriptor(BitReadWriter brw) {
        super(brw);
        
        CA_system_ID = brw.ReadOnBuffer(16);
        CA_unit_id = (byte) brw.ReadOnBuffer(4);
        num_of_component = (byte) brw.ReadOnBuffer(4);
        component_tag = new int[num_of_component];
        
        for ( int i=0; i<num_of_component; i++ ) {
            component_tag[i] = brw.ReadOnBuffer(16);
        }
        
        contract_verification_info_length = (byte) brw.ReadOnBuffer(8);
        contract_verification_info = new byte[contract_verification_info_length];
        for ( int i=0; i<contract_verification_info_length; i++ ) {
            contract_verification_info[i] = (byte) brw.ReadOnBuffer(8);
        }
        
        fee_name_length = (byte) brw.ReadOnBuffer(8);
        fee_name = new byte[fee_name_length];
        
        for ( int i=0; i<fee_name_length; i++ ) {
            fee_name[i] = (byte) brw.ReadOnBuffer(8);
        }
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t CA_system_ID : 0x%x \n", CA_system_ID));
        Logger.d(String.format("\t CA_unit_id : 0x%x \n", CA_unit_id));
        Logger.d(String.format("\t num_of_component : 0x%x \n", num_of_component));
        
        for ( int i=0; i<component_tag.length; i++ ) {
            Logger.d(String.format("\t [%d] compoent_tag : 0x%x \n", i, component_tag[i]));
        }
        
        Logger.d(String.format("\t contract_vertification_info_length : 0x%x \n", 
                contract_verification_info_length));
        
        for ( int i=0; i<contract_verification_info.length; i++ ) {
            Logger.d(String.format("\t [%d] contract_verification_info : 0x%x \n", 
                    i, contract_verification_info[i]));
        }
        
        Logger.d(String.format("\t fee_name_length : 0x%x \n",  fee_name_length));
        Logger.d(String.format("\t fee_name : %s \n", new String(fee_name)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5;
        
        descriptor_length += ((component_tag.length*2) + 
                contract_verification_info.length + fee_name.length);
    }
}
