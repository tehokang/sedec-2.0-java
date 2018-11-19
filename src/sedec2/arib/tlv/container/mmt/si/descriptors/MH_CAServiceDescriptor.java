package sedec2.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_CAServiceDescriptor extends Descriptor {
    protected int CA_system_ID;
    protected byte ca_broadcaster_group_id;
    protected byte message_control;
    protected List<ServiceId> service_ids = new ArrayList<>();

    public class ServiceId {
        public int service_id;
    }
    
    public MH_CAServiceDescriptor(BitReadWriter brw) {
        super(brw);
    
        CA_system_ID = brw.readOnBuffer(16);
        ca_broadcaster_group_id = (byte) brw.readOnBuffer(8);
        message_control = (byte) brw.readOnBuffer(8);
        
        for ( int i=descriptor_length-4; i>0; ) {
            ServiceId svc_id = new ServiceId();
            svc_id.service_id = brw.readOnBuffer(16);
            i-=2;
            service_ids.add(svc_id);
        }
    }
    
    public int getCASystemId() {
        return CA_system_ID;
    }
    
    public byte getCABroadcasterGroupId() {
        return ca_broadcaster_group_id;
    }
    
    public byte getMessageControl() {
        return message_control;
    }
    
    public List<ServiceId> getServiceIds() {
        return service_ids;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t CA_system_ID : 0x%x \n", CA_system_ID));
        Logger.d(String.format("\t ca_broadcaster_group_id : 0x%x \n", ca_broadcaster_group_id));
        Logger.d(String.format("\t message_control : 0x%x \n", message_control));
        
        for ( int i=0; i<service_ids.size(); i++ ) {
            Logger.d(String.format("\t [%d] service_id : 0x%x \n", 
                    i, service_ids.get(i).service_id));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + (service_ids.size()*2);
    }
}
