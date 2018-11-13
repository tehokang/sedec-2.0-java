package sedec2.arib.tlv.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class IpDataFlowDescriptor extends Descriptor {
    protected byte ip_version;
    protected byte number_of_flow;
    protected List<IpDataFlow> ip_data_flows = new ArrayList<>();
    
    public class IpDataFlow {
        public byte ip_data_flow_id;
        public byte[] src_address_32 = new byte[4];
        public byte[] dst_address_32 = new byte[4];
        public byte[] src_address_128 = new byte[16];
        public byte[] dst_address_128 = new byte[16];
        public int src_port;
        public int dst_port;
    }
    
    public IpDataFlowDescriptor(BitReadWriter brw) {
        super(brw);
        
        ip_version = (byte) brw.readOnBuffer(1);
        number_of_flow = (byte) brw.readOnBuffer(7);
        
        for ( int i=descriptor_length-1; i>0; ) {
            IpDataFlow ip_data_flow = new IpDataFlow();
            ip_data_flow.ip_data_flow_id = (byte) brw.readOnBuffer(8);
            
            i-=1;
            if ( ip_version == '0' ) {
                for ( int j=0; j<4; j++ ) 
                    ip_data_flow.src_address_32[j] = (byte) brw.readOnBuffer(8);
                for ( int j=0; j<4; j++ ) 
                    ip_data_flow.dst_address_32[j] = (byte) brw.readOnBuffer(8);
                i-=8;
            } else if ( ip_version == '1' ) {
                for ( int j=0; j<16; j++ ) 
                    ip_data_flow.src_address_128[j] = (byte) brw.readOnBuffer(8);
                for ( int j=0; j<16; j++ ) 
                    ip_data_flow.dst_address_128[j] = (byte) brw.readOnBuffer(8);
                i-=32;
            }
            ip_data_flow.src_port = brw.readOnBuffer(16);
            ip_data_flow.dst_port = brw.readOnBuffer(16);
            i-=4;
            
            ip_data_flows.add(ip_data_flow);
        }
    }

    public byte getIpVersion() {
        return ip_version;
    }
    
    public byte getNumberOfFlow() {
        return number_of_flow;
    }
    
    public List<IpDataFlow> getIpDataFlows() {
        return ip_data_flows;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t ip_version : 0x%x \n", ip_version));
        Logger.d(String.format("\t number_of_flow : 0x%x \n", number_of_flow));
        
        for ( int i=0; i<ip_data_flows.size(); i++ ) {
            IpDataFlow ip_data_flow = ip_data_flows.get(i);
            
            Logger.d(String.format("\t [%d] ip_data_flow_id : 0x%x \n", 
                    i, ip_data_flow.ip_data_flow_id));
            
            if ( ip_version == '0' ) {
                Logger.d(String.format("\t [%d] src_address_32 : %d.%d.%d.%d \n", 
                        i, ip_data_flow.src_address_32[0], ip_data_flow.src_address_32[1],
                        ip_data_flow.src_address_32[2], ip_data_flow.src_address_32[3]));
                Logger.d(String.format("\t [%d] dst_address_32 : %d.%d.%d.%d \n", 
                        i, ip_data_flow.dst_address_32[0], ip_data_flow.dst_address_32[1],
                        ip_data_flow.dst_address_32[2], ip_data_flow.dst_address_32[3]));
            } else if ( ip_version == '1') {
                Logger.d(String.format("\t [%d] src_address_128 : "
                        + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                        i, ip_data_flow.src_address_128[0], ip_data_flow.src_address_128[1],
                        ip_data_flow.src_address_128[2], ip_data_flow.src_address_128[3], 
                        ip_data_flow.src_address_128[4], ip_data_flow.src_address_128[5],
                        ip_data_flow.src_address_128[6], ip_data_flow.src_address_128[7],
                        ip_data_flow.src_address_128[8], ip_data_flow.src_address_128[9],
                        ip_data_flow.src_address_128[10], ip_data_flow.src_address_128[11],
                        ip_data_flow.src_address_128[12], ip_data_flow.src_address_128[12],
                        ip_data_flow.src_address_128[14], ip_data_flow.src_address_128[15]));
                Logger.d(String.format("\t [%d] dst_address_128 : "
                        + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                        i, ip_data_flow.dst_address_128[0], ip_data_flow.dst_address_128[1],
                        ip_data_flow.dst_address_128[2], ip_data_flow.dst_address_128[3], 
                        ip_data_flow.dst_address_128[4], ip_data_flow.dst_address_128[5],
                        ip_data_flow.dst_address_128[6], ip_data_flow.dst_address_128[7],
                        ip_data_flow.dst_address_128[8], ip_data_flow.dst_address_128[9],
                        ip_data_flow.dst_address_128[10], ip_data_flow.dst_address_128[11],
                        ip_data_flow.dst_address_128[12], ip_data_flow.dst_address_128[12],
                        ip_data_flow.dst_address_128[14], ip_data_flow.dst_address_128[15]));
            }
            
            Logger.d(String.format("\t [%d] src_port : 0x%x \n", ip_data_flow.src_port));
            Logger.d(String.format("\t [%d] dst_port : 0x%x \n", ip_data_flow.dst_port));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
        
        for ( int i=0; i<ip_data_flows.size(); i++ ) {
            descriptor_length+=1;
            if ( ip_version == '0' ) {
                descriptor_length += 8;
            } else if ( ip_version == '1' ) {
                descriptor_length += 32;
            }
            descriptor_length += 2;
        }
    }
}
