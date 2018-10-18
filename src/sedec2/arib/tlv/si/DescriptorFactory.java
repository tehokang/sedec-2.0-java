package sedec2.arib.tlv.si;

import sedec2.arib.tlv.si.descriptors.Descriptor;
import sedec2.arib.tlv.si.descriptors.ServiceListDescriptor;
import sedec2.arib.tlv.si.descriptors.UnknownDescriptor;
import sedec2.arib.tlv.si.descriptors.NetworkNameDescriptor;
import sedec2.arib.tlv.si.descriptors.SateliteDeliverySystemDescriptor;
import sedec2.arib.tlv.si.descriptors.SystemManagementDescriptor;
import sedec2.base.BitReadWriter;

public class DescriptorFactory {
    public final static int NETWORK_NAME_DESCRIPTOR = 0x40;
    public final static int SERVICE_LIST_DESCRIPTOR = 0x41;
    public final static int SATELITE_DELIVERY_SYSTEM_DESCRIPTOR = 0x43;
    public final static int SYSTEM_MANAGEMENT_DESCRIPTOR = 0xfe;
    
    public final static int UNKNOWN_DESCRIPTOR = 0xff;
    
    public static Descriptor CreateDescriptor(BitReadWriter brw) {
        int descriptor_tag = 
                (((brw.GetCurrentBuffer()[0] & 0xff) << 8) |
                (brw.GetCurrentBuffer()[1] & 0xff));
        
        switch ( descriptor_tag ) {
            case SYSTEM_MANAGEMENT_DESCRIPTOR:
                return new SystemManagementDescriptor(brw);
            case SATELITE_DELIVERY_SYSTEM_DESCRIPTOR:
                return new SateliteDeliverySystemDescriptor(brw);
            case NETWORK_NAME_DESCRIPTOR:
                return new NetworkNameDescriptor(brw);
            case SERVICE_LIST_DESCRIPTOR:
                return new ServiceListDescriptor(brw);
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }
    
    private DescriptorFactory() {
        
    }
}
