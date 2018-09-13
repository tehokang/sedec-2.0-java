package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class EventMessageDescriptor extends Descriptor {
    protected int event_msg_group_id;
    protected byte time_mode;
    protected long event_msg_UTC;
    protected long event_msg_NPT;
    protected long event_msg_relativeTime;
    protected byte event_msg_type;
    protected int event_msg_id;
    protected byte[] private_data_byte;
    
    public EventMessageDescriptor(BitReadWriter brw) {
        super();
        
        descriptor_tag = brw.ReadOnBuffer(16);
        descriptor_length = brw.ReadOnBuffer(16);
        
        event_msg_group_id = brw.ReadOnBuffer(12);
        brw.SkipOnBuffer(4);
        time_mode = (byte) brw.ReadOnBuffer(8);
        
        if ( time_mode == 0 ) {
            brw.SkipOnBuffer(64);
        } else if ( time_mode == 0x01 || time_mode == 0x05 ) {
            event_msg_UTC = brw.ReadOnBuffer(64);
        } else if ( time_mode == 0x02 ) {
            event_msg_NPT = brw.ReadOnBuffer(64);
        } else if ( time_mode == 0x03 ) {
            event_msg_relativeTime = brw.ReadOnBuffer(64);
        }
        
        event_msg_type = (byte) brw.ReadOnBuffer(8);
        event_msg_id = brw.ReadOnBuffer(16);
        
        private_data_byte = new byte[descriptor_length - 3 - 8 - 1 - 2];
        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t event_msg_group_id : 0x%x \n", event_msg_group_id));
        Logger.d(String.format("\t time_mode : 0x0%x \n", time_mode));
        Logger.d(String.format("\t event_msg_UTC : 0x%x \n", event_msg_UTC));
        Logger.d(String.format("\t event_msg_NPT : 0x%x \n", event_msg_NPT));
        Logger.d(String.format("\t event_msg_relativeTime : 0x%x \n", event_msg_relativeTime));
        Logger.d(String.format("\t event_msg_type : 0x%x \n", event_msg_type));
        Logger.d(String.format("\t event_msg_id : 0x%x \n", event_msg_id));
        
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
        descriptor_length = 14 + private_data_byte.length;
    }
}
