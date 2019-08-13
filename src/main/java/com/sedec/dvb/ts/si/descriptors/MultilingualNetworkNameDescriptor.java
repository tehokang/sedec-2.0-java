package com.sedec.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MultilingualNetworkNameDescriptor extends Descriptor {
    protected List<NetworkName> network_names = new ArrayList<>();

    public class NetworkName {
        public byte[] ISO_639_language_code = new byte[3];
        public byte network_name_length;
        public byte[] network_name;
    }
    public MultilingualNetworkNameDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            NetworkName name = new NetworkName();
            name.ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            name.ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            name.ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

            name.network_name_length = (byte) brw.readOnBuffer(8);
            name.network_name = new byte[name.network_name_length];

            for ( int j=0; j<name.network_name.length; j++ ) {
                name.network_name[j] = (byte) brw.readOnBuffer(8);
            }

            i-= (4 + name.network_name.length);
            network_names.add(name);
        }
    }

    public List<NetworkName> getNetworkNames() {
        return network_names;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<network_names.size(); i++ ) {
            NetworkName name = network_names.get(i);
            Logger.d(String.format("\t [%d] ISO_639_language_code : %s \n",
                    i, new String(name.ISO_639_language_code)));
            Logger.d(String.format("\t [%d] network_name : %s \n",
                    i, new String(name.network_name)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 0;

        for ( int i=0; i<network_names.size(); i++ ) {
            NetworkName name = network_names.get(i);
            descriptor_length += 4;
            descriptor_length += name.network_name.length;
        }
    }
}
