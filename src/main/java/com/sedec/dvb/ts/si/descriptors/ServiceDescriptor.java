package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class ServiceDescriptor extends Descriptor {
    protected byte service_type;
    protected byte service_provider_name_length;
    protected byte[] service_provider_name;
    protected byte service_name_length;
    protected byte[] service_name;

    public ServiceDescriptor(BitReadWriter brw) {
        super(brw);

        service_type = (byte) brw.readOnBuffer(8);
        service_provider_name_length = (byte) brw.readOnBuffer(8);
        service_provider_name = new byte[service_provider_name_length];

        for ( int i=0; i<service_provider_name.length; i++ ) {
            service_provider_name[i] = (byte) brw.readOnBuffer(8);
        }

        service_name_length = (byte) brw.readOnBuffer(8);
        service_name = new byte[service_name_length];

        for ( int i=0; i<service_name.length; i++ ) {
            service_name[i] = (byte) brw.readOnBuffer(8);
        }

    }

    public byte getServiceType() {
        return service_type;
    }

    public byte[] getServiceProviderName() {
        return service_provider_name;
    }

    public byte[] getServiceName() {
        return service_name;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t service_type : 0x%x \n", service_type));
        Logger.d(String.format("\t service_provider_name : %s \n",
                new String(service_provider_name)));
        Logger.d(String.format("\t service_name : %s \n",
                new String(service_name)));

    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = (3 + service_provider_name.length + service_name.length);
    }
}
