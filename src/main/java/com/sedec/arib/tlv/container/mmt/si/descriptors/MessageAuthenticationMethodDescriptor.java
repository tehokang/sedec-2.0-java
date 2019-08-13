package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

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

    public byte getLayerType() {
        return layer_type;
    }

    public byte getMessageAuthenticationSystemId() {
        return message_authentication_system_id;
    }

    public byte[] getPrivateData() {
        return private_data;
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
