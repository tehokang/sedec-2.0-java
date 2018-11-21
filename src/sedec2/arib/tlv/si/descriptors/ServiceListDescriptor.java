package sedec2.arib.tlv.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ServiceListDescriptor extends Descriptor {
    protected List<Service> services = new ArrayList<>();

    class Service {
        public int service_id;
        public byte service_type;
    }

    public ServiceListDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            Service service = new Service();
            service.service_id = brw.readOnBuffer(16);
            service.service_type = (byte) brw.readOnBuffer(8);
            services.add(service);
            i-=3;
        }
    }

    public List<Service> getServices() {
        return services;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<services.size(); i++ ) {
            Service service = services.get(i);
            Logger.d(String.format("\t [%d] service_id : 0x%x \n",
                    i, service.service_id));
            Logger.d(String.format("\t [%d] service_type : 0x%x \n",
                    i, service.service_type));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = services.size() * 3;
    }
}
