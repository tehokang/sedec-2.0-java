package sedec2.arib.b10.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class CAServiceDescriptor extends Descriptor {
    protected int CA_system_id;
    protected byte ca_broadcaster_group_id;
    protected byte message_control;
    protected int[] service_id;
    
    public CAServiceDescriptor(BitReadWriter brw) {
        super(brw);
        
        CA_system_id = brw.readOnBuffer(16);
        ca_broadcaster_group_id = (byte) brw.readOnBuffer(8);
        message_control = (byte) brw.readOnBuffer(8);
        
        service_id = new int[(descriptor_length-4)/2];
        for ( int i=0; i<service_id.length; i++ ) {
            service_id[i] = brw.readOnBuffer(16);
        }
    }

    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t CA_system_id : 0x%x \n", CA_system_id));
        Logger.d(String.format("\t ca_broadcaster_group_id : 0x%x \n",  ca_broadcaster_group_id));
        Logger.d(String.format("\t message_control : 0x%x \n",  message_control));
        
        for ( int i=0; i<service_id.length; i++ ) {
            Logger.d(String.format("\t [%d] service_id : 0x%x \n", i, service_id[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + service_id.length*2;
    }
}
