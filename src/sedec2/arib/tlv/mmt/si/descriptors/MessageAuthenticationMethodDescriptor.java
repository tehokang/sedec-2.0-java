package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MessageAuthenticationMethodDescriptor extends Descriptor {
    protected byte layer_type;
    protected byte message_authentication_system_id;
    protected byte[] private_data;
    
    public MessageAuthenticationMethodDescriptor(BitReadWriter brw) {
        super(brw);
        
        layer_type = (byte) brw.readOnBuffer(2);
        brw.skipOnBuffer(6);
        message_authentication_system_id = (byte) brw.readOnBuffer(8);
        
        private_data = new byte[descriptor_length - 2];
        for ( int i=0; i<private_data.length; i++ ) {
            private_data[i] = (byte) brw.readOnBuffer(8);
        }
    }

    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t layer_type : 0x%x \n", layer_type));
        Logger.d(String.format("\t message_authentication_system_id : 0x%x \n",
                message_authentication_system_id));
        
        Logger.d("\t private_data : \n");
        BinaryLogger.print(private_data);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + private_data.length;
    }

}
