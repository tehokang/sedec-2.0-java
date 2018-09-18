package sedec2.arib.b10.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ConditionalAccessDescriptor extends Descriptor {
    protected int CA_system_ID;
    protected int CA_PID;
    protected byte[] private_data_byte;
    
    public ConditionalAccessDescriptor(BitReadWriter brw) {
        super(brw);
        
        CA_system_ID = brw.ReadOnBuffer(16);
        brw.SkipOnBuffer(3);;
        CA_PID = brw.ReadOnBuffer(13);
        
        private_data_byte = new byte[descriptor_length - 4];
        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t CA_system_ID : 0x%x \n",  CA_system_ID));
        Logger.d(String.format("\t CA_PID : 0x%x \n", CA_PID));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + private_data_byte.length;
    }

}
