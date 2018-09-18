package sedec2.arib.b39.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MessageAuthenticationMethodDescriptor extends Descriptor {
    protected byte layer_type;
    protected byte message_authentication_system_id;
    protected byte[] private_data;
    
    public MessageAuthenticationMethodDescriptor(BitReadWriter brw) {
        super(brw);
        
        layer_type = (byte) brw.ReadOnBuffer(2);
        brw.SkipOnBuffer(6);
        message_authentication_system_id = (byte) brw.ReadOnBuffer(8);
        
        private_data = new byte[descriptor_length - 2];
        for ( int i=0; i<private_data.length; i++ ) {
            private_data[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t layer_type : 0x%x \n", layer_type));
        Logger.d(String.format("\t message_authentication_system_id : 0x%x \n",
                message_authentication_system_id));
        
        int j=1;
        Logger.d("private_data : \n");
        Logger.p(String.format("%03d : ", j));
        for(int i=0; i<private_data.length; i++)
        {
            Logger.p(String.format("%02x ", private_data[i]));
            if(i%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + private_data.length;
    }

}
