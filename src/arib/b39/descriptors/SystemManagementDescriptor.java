package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class SystemManagementDescriptor extends Descriptor {
    protected int system_management_id;
    protected byte[] additional_identification_info;
    
    public SystemManagementDescriptor(BitReadWriter brw) {
        super();
        
        descriptor_tag = (byte) brw.ReadOnBuffer(8);
        descriptor_length = (byte) brw.ReadOnBuffer(8);
        
        system_management_id = brw.ReadOnBuffer(16);
        additional_identification_info = new byte[descriptor_length-2];
        
        for ( int i=0; i<additional_identification_info.length; i++ ) {
            additional_identification_info[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t system_management_id : 0x%x \n", system_management_id));
        Logger.d(String.format("\t addtional_identification_info : %s \n", 
                new String(additional_identification_info)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + additional_identification_info.length;
    }

}
