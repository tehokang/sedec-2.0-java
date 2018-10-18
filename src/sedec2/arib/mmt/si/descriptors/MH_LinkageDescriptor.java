package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_LinkageDescriptor extends Descriptor {
    protected int tlv_stream_id;
    protected int original_network_id;
    protected int service_id;
    protected int linkage_type;
    protected byte[] private_data_byte;
    
    public MH_LinkageDescriptor(BitReadWriter brw) {
        super();
        
        descriptor_tag = brw.ReadOnBuffer(16);
        descriptor_length = brw.ReadOnBuffer(16);
        
        tlv_stream_id = brw.ReadOnBuffer(16);
        original_network_id = brw.ReadOnBuffer(16);
        service_id = brw.ReadOnBuffer(16);
        linkage_type = brw.ReadOnBuffer(16);
        
        private_data_byte = new byte[descriptor_length - 7];
        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    public int GetTlvStreamId() {
        return tlv_stream_id;
    }
    
    public int GetOriginalNetworkId() {
        return original_network_id;
    }
    
    public int GetServiceId() {
        return service_id;
    }
    
    public int GetLinkageType() {
        return linkage_type;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t tlv_stream_id : 0x%x \n", tlv_stream_id));
        Logger.d(String.format("\t original_network_id : 0x%x \n", original_network_id));
        Logger.d(String.format("\t service_id : 0x%x \n", service_id));
        Logger.d(String.format("\t linkage_type : 0x%x \n", linkage_type));
        
        int j=1;
        Logger.p(String.format("%03d : ", j));
        for(int k=0; k<private_data_byte.length; k++)
        {
            Logger.p(String.format("%02x ", private_data_byte[k]));
            if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 7 + private_data_byte.length;
    }
}
