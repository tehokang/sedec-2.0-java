package sedec2.arib.tlv.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class SystemManagementDescriptor extends Descriptor {
    protected SystemManagementId system_management_id;
    protected byte[] additional_identification_info;
    
    class SystemManagementId {
        public byte broadcasting_flag;
        public byte broadcasting_identifier;
        public byte additional_broadcasting_indentification;
    }
    
    public SystemManagementDescriptor(BitReadWriter brw) {
        super(brw);
        
        system_management_id = new SystemManagementId();
        system_management_id.broadcasting_flag = (byte) brw.readOnBuffer(2);
        system_management_id.broadcasting_identifier = (byte) brw.readOnBuffer(6);
        system_management_id.additional_broadcasting_indentification = 
                (byte) brw.readOnBuffer(8);

        additional_identification_info = new byte[descriptor_length-2];
        
        for ( int i=0; i<additional_identification_info.length; i++ ) {
            additional_identification_info[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public SystemManagementId getSystemManagementId() {
        return system_management_id;
    }
    
    public byte[] getAdditionalIdentificationInfo() {
        return additional_identification_info;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t broadcasting_flag : 0x%x \n", 
                system_management_id.broadcasting_flag));
        Logger.d(String.format("\t broadcasting_identifier : 0x%x \n", 
                system_management_id.broadcasting_identifier));
        Logger.d(String.format("\t additional_broadcasting_indentification : 0x%x \n", 
                system_management_id.additional_broadcasting_indentification));
        Logger.d(String.format("\t addtional_identification_info : %s \n", 
                new String(additional_identification_info)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + additional_identification_info.length;
    }

}
