package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class MH_LinkageDescriptor extends Descriptor {
    protected int tlv_stream_id;
    protected int original_network_id;
    protected int service_id;
    protected int linkage_type;
    protected byte[] private_data_byte;

    public MH_LinkageDescriptor(BitReadWriter brw) {
        super();

        descriptor_tag = brw.readOnBuffer(16);
        descriptor_length = brw.readOnBuffer(16);

        tlv_stream_id = brw.readOnBuffer(16);
        original_network_id = brw.readOnBuffer(16);
        service_id = brw.readOnBuffer(16);
        linkage_type = brw.readOnBuffer(8);

        private_data_byte = new byte[descriptor_length - 7];
        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getTlvStreamId() {
        return tlv_stream_id;
    }

    public int getOriginalNetworkId() {
        return original_network_id;
    }

    public int getServiceId() {
        return service_id;
    }

    public int getLinkageType() {
        return linkage_type;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t tlv_stream_id : 0x%x \n", tlv_stream_id));
        Logger.d(String.format("\t original_network_id : 0x%x \n", original_network_id));
        Logger.d(String.format("\t service_id : 0x%x \n", service_id));
        Logger.d(String.format("\t linkage_type : 0x%x \n", linkage_type));

        BinaryLogger.print(private_data_byte);
    }


    @Override
    public int getDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 4;
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 7 + private_data_byte.length;
    }
}
