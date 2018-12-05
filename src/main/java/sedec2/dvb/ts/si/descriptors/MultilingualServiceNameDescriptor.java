package sedec2.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MultilingualServiceNameDescriptor extends Descriptor {
    protected List<ServiceName> service_names = new ArrayList<>();

    public class ServiceName {
        public byte[] ISO_639_language_code = new byte[3];
        public byte service_provider_name_length;
        public byte[] service_provider_name;
        public byte service_name_length;
        public byte[] service_name;
    }

    public MultilingualServiceNameDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            ServiceName name = new ServiceName();
            name.ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            name.ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            name.ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);
            name.service_provider_name_length = (byte) brw.readOnBuffer(8);
            name.service_provider_name = new byte[name.service_provider_name_length];
            for ( int k=0; k<name.service_provider_name.length; k++ ) {
                name.service_provider_name[k] = (byte) brw.readOnBuffer(8);
            }

            name.service_name_length = (byte) brw.readOnBuffer(8);
            name.service_name = new byte[name.service_name_length];

            for ( int k=0; k<name.service_name.length; k++ ) {
                name.service_name[k] = (byte) brw.readOnBuffer(8);
            }

            i-= ( 5 + name.service_provider_name.length + name.service_name.length );
            service_names.add(name);
        }
    }

    public List<ServiceName> getServiceNames() {
        return service_names;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<service_names.size(); i++ ) {
            ServiceName name = service_names.get(i);

            Logger.d(String.format("\t [%d] ISO_639_language_code : %s \n",
                    i, new String(name.ISO_639_language_code)));
            Logger.d(String.format("\t [%d] service_provider_name : %s \n",
                    i, new String(name.service_provider_name)));
            Logger.d(String.format("\t [%d] service_name : %s \n",
                    i, new String(name.service_name)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 0;

        for ( int i=0; i<service_names.size(); i++ ) {
            ServiceName name = service_names.get(i);
            descriptor_length +=
                    (5 + name.service_provider_name.length + name.service_name.length );
        }
    }

}
