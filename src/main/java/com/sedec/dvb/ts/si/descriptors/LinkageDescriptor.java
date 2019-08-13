package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class LinkageDescriptor extends Descriptor {
    protected int transport_stream_id;
    protected int original_network_id;
    protected int service_id;
    protected byte linkage_type;
    protected LinkageType0x08 linkage_type_0x08;
    protected LinkageType0x0d linkage_type_0x0d;
    protected byte[] private_data_byte;

    public class LinkageType0x08 {
        public byte hand_over_type;
        public byte origin_type;
        public int network_id;
        public int initial_service_id;
    }

    public class LinkageType0x0d {
        public int target_event_id;
        public byte target_listed;
        public byte event_simulcast;
    }

    public LinkageDescriptor(BitReadWriter brw) {
        super(brw);

        transport_stream_id = brw.readOnBuffer(16);
        original_network_id = brw.readOnBuffer(16);
        service_id = brw.readOnBuffer(16);
        linkage_type = (byte) brw.readOnBuffer(8);

        int linkage_type_length = 0;
        if ( linkage_type == 0x08 ) {
            linkage_type_0x08 = new LinkageType0x08();
            linkage_type_0x08.hand_over_type = (byte) brw.readOnBuffer(4);
            brw.skipOnBuffer(3);
            linkage_type_0x08.origin_type = (byte) brw.readOnBuffer(1);
            linkage_type_length += 1;
            if ( linkage_type_0x08.hand_over_type == 0x01 ||
                    linkage_type_0x08.hand_over_type == 0x02 ||
                    linkage_type_0x08.hand_over_type == 0x03 ) {
                linkage_type_0x08.network_id = brw.readOnBuffer(16);
                linkage_type_length += 2;
            }

            if ( linkage_type_0x08.origin_type == 0x00 ) {
                linkage_type_0x08.initial_service_id = brw.readOnBuffer(16);
                linkage_type_length += 2;
            }
        }

        if ( linkage_type == 0x0d ) {
            linkage_type_0x0d = new LinkageType0x0d();
            linkage_type_0x0d.target_event_id = brw.readOnBuffer(16);
            linkage_type_0x0d.target_listed = (byte) brw.readOnBuffer(1);
            linkage_type_0x0d.event_simulcast = (byte) brw.readOnBuffer(1);
            brw.skipOnBuffer(6);
            linkage_type_length += 3;
        }

        private_data_byte = new byte[descriptor_length - 7 - linkage_type_length];
        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getTransportStreamId() {
        return transport_stream_id;
    }

    public int getOriginalNetworkId() {
        return original_network_id;
    }

    public int getServiceId() {
        return service_id;
    }

    public byte getLinkageType() {
        return linkage_type;
    }

    public LinkageType0x08 getLinkageType0x08() {
        return linkage_type_0x08;
    }

    public LinkageType0x0d getLinkageType0x0d() {
        return linkage_type_0x0d;
    }

    public byte[] getPrivateDataByte() {
        return private_data_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t transport_stream_id : 0x%x \n", transport_stream_id));
        Logger.d(String.format("\t original_network_id : 0x%x \n", original_network_id));
        Logger.d(String.format("\t service_id : 0x%x \n", service_id));
        Logger.d(String.format("\t linkage_type : 0x%x \n", linkage_type));

        if ( linkage_type == 0x08 ) {
            Logger.d(String.format("\t linkage_type_0x08.hand_over_type : 0x%x \n",
                    linkage_type_0x08.hand_over_type));
            Logger.d(String.format("\t linkage_type_0x08.origin_type : 0x%x \n",
                    linkage_type_0x08.origin_type));
            Logger.d(String.format("\t linkage_type_0x08.network_id : 0x%x \n",
                    linkage_type_0x08.network_id));
            Logger.d(String.format("\t linkage_type_0x08.initial_service_id : 0x%x \n",
                    linkage_type_0x08.initial_service_id));
        }

        if ( linkage_type == 0x0d ) {
            Logger.d(String.format("\t linkage_type_0x0d.target_event_id : 0x%x \n",
                    linkage_type_0x0d.target_event_id));
            Logger.d(String.format("\t linkage_type_0x0d.target_listed : 0x%x \n",
                    linkage_type_0x0d.target_listed));
            Logger.d(String.format("\t linkage_type_0x0d.event_simulcast : 0x%x \n",
                    linkage_type_0x0d.event_simulcast));
        }

        Logger.d(String.format("\t private_data_byte : \n"));
        BinaryLogger.print(private_data_byte);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 7;

        if ( linkage_type == 0x08 ) {
            descriptor_length += 1;
            if ( linkage_type_0x08.hand_over_type == 0x01 ||
                    linkage_type_0x08.hand_over_type == 0x02 ||
                    linkage_type_0x08.hand_over_type == 0x03 ) {
                descriptor_length += 2;
            }

            if ( linkage_type_0x08.origin_type == 0x00 ) {
                descriptor_length += 2;
            }
        }

        if ( linkage_type == 0x0d ) {
            descriptor_length += 3;
        }

        if ( private_data_byte != null ) {
            descriptor_length += private_data_byte.length;
        }
    }

}
