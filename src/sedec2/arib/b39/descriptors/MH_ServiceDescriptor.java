package sedec2.arib.b39.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ServiceDescriptor extends Descriptor {
    protected byte service_type;
    protected byte service_provider_name_length;
    protected byte[] service_provider_name;
    protected byte service_name_length;
    protected byte[] service_name;
    
    public MH_ServiceDescriptor(BitReadWriter brw) {
        super(brw);
        
        service_type = (byte) brw.ReadOnBuffer(8);
        service_provider_name_length = (byte) brw.ReadOnBuffer(8);
        
        service_provider_name = new byte[service_provider_name_length];
        for ( int i=0; i<service_provider_name_length; i++ ) {
            service_provider_name[i] = (byte) brw.ReadOnBuffer(8);
        }
        
        service_name_length = (byte) brw.ReadOnBuffer(8);
        
        service_name = new byte[service_name_length];
        for ( int i=0; i<service_name_length; i++ ) {
            service_name[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t service_type : 0x%x \n", service_type));
        Logger.d(String.format("\t service_provider_name_length : 0x%x \n", 
                service_provider_name_length));
        Logger.d(String.format("\t service_provider_name : %s \n", 
                new String(service_provider_name)));
        Logger.d(String.format("\t service_name_length : 0x%x \n", service_name_length));
        Logger.d(String.format("\t service_name : 0x%x \n", new String(service_name)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3 + service_provider_name.length + service_name.length;
    }
}
