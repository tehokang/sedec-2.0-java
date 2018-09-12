package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

/**
 * @brief ConnectionRequirementDescriptor
 * @note Not Verified Yet
 */
public class ConnectionRequirementDescriptor extends Descriptor {
    private boolean IP_connection_requirement_flag;
    
    public ConnectionRequirementDescriptor(BitReadWriter brw) {
        super(brw);
        
        brw.SkipOnBuffer(7);
        IP_connection_requirement_flag = brw.ReadOnBuffer(1) == 1 ? true : false;
        
        for ( int i=0; i<descriptor_length-1; i++ ) {
            brw.SkipOnBuffer(8);
        }
    }

    public boolean GetIPConnectionRequirementFlag() {
        return IP_connection_requirement_flag;
    }
    
    public void SetIPConnectionRequirementFlag(boolean value) {
        IP_connection_requirement_flag = value;
    }
    
    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(0x7f, 7);
        brw.WriteOnBuffer(IP_connection_requirement_flag == true ? 1 : 0, 1);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t IP_connection_requirement_flag : %d \n", IP_connection_requirement_flag));
        Logger.d("\n");
    }
    
}
