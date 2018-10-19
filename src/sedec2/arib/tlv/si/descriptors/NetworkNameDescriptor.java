package sedec2.arib.tlv.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class NetworkNameDescriptor extends Descriptor {
    protected byte[] network_name;
    
    public NetworkNameDescriptor(BitReadWriter brw) {
        super(brw);
        
        network_name = new byte[descriptor_length];
        
        for ( int i=0; i<network_name.length; i++ ) {
            network_name[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    public String GetNetworkName() {
        return new String(network_name);
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t network_name : %s \n", new String(network_name)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = network_name.length;
    }
}
