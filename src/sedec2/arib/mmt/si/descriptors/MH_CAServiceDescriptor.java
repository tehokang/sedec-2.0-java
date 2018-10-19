package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_CAServiceDescriptor extends Descriptor {
    protected int CA_system_ID;
    protected byte ca_broadcaster_group_id;
    protected byte message_control;
    protected int[] service_id;
    
    public MH_CAServiceDescriptor(BitReadWriter brw) {
        super(brw);
    
        CA_system_ID = brw.ReadOnBuffer(16);
        ca_broadcaster_group_id = (byte) brw.ReadOnBuffer(8);
        message_control = (byte) brw.ReadOnBuffer(8);
        
        service_id = new int[(descriptor_length-4)/2];
        
        for ( int i=descriptor_length-4; i>0; ) {
            service_id[i] = brw.ReadOnBuffer(16);
            i-=2;
        }
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t CA_system_ID : 0x%x \n", CA_system_ID));
        Logger.d(String.format("\t ca_broadcaster_group_id : 0x%x \n", ca_broadcaster_group_id));
        Logger.d(String.format("\t message_control : 0x%x \n", message_control));
        
        for ( int i=0; i<service_id.length; i++ ) {
            Logger.d(String.format("\t [%d] service_id : 0x%x \n", i, service_id[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + (service_id.length*2);
    }
}
