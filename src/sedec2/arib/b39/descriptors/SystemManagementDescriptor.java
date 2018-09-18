package sedec2.arib.b39.descriptors;

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
        super();
        
        descriptor_tag = (byte) brw.ReadOnBuffer(8);
        descriptor_length = (byte) brw.ReadOnBuffer(8);
        
        system_management_id = new SystemManagementId();
        system_management_id.broadcasting_flag = (byte) brw.ReadOnBuffer(2);
        system_management_id.broadcasting_identifier = (byte) brw.ReadOnBuffer(6);
        system_management_id.additional_broadcasting_indentification = 
                (byte) brw.ReadOnBuffer(8);

        additional_identification_info = new byte[descriptor_length-2];
        
        for ( int i=0; i<additional_identification_info.length; i++ ) {
            additional_identification_info[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    public SystemManagementId GetSystemManagementId() {
        return system_management_id;
    }
    
    public byte[] GetAdditionalIdentificationInfo() {
        return additional_identification_info;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
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
        descriptor_length = 1 + additional_identification_info.length;
    }

}