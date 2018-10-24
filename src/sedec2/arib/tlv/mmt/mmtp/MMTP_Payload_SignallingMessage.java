package sedec2.arib.tlv.mmt.mmtp;

import sedec2.arib.tlv.mmt.si.MessageFactory;
import sedec2.arib.tlv.mmt.si.messages.Message;
import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MMTP_Payload_SignallingMessage {
    protected byte fragmentation_indicator;
    protected byte length_extension_flag;
    protected byte aggregation_flag;
    protected byte fragment_counter;
    
    protected Message message = null;
    protected int message_length;
    protected byte[] message_byte;
    
    public MMTP_Payload_SignallingMessage(BitReadWriter brw) {
        fragmentation_indicator = (byte) brw.readOnBuffer(2);
        brw.skipOnBuffer(4);
        length_extension_flag = (byte) brw.readOnBuffer(1);
        aggregation_flag = (byte) brw.readOnBuffer(1);
        fragment_counter = (byte) brw.readOnBuffer(8);
        
        if ( aggregation_flag == 0x00 ) {
            message = MessageFactory.createMessage(brw.getCurrentBuffer());
        } else {
            if ( length_extension_flag == 0x01 ) {
                message_length = brw.readOnBuffer(32);
            } else {
                message_length = brw.readOnBuffer(16);
            }
            
            message_byte = new byte[message_length];
            for ( int i=0; i<message_byte.length; i++ ) {
                message_byte[i] = (byte) brw.readOnBuffer(8);
            }
            message = MessageFactory.createMessage(message_byte);
        }
    }
    
    public void print() {
        Logger.d(String.format("------- MMTP Payload ------- (%s)\n", getClass().getName()));
        Logger.d(String.format("fragmentation_indicator : 0x%x \n", fragmentation_indicator));
        Logger.d(String.format("length_extension_flag : 0x%x \n", length_extension_flag));
        Logger.d(String.format("aggregation_flag : 0x%x \n", aggregation_flag));
        Logger.d(String.format("fragment_counter : 0x%x \n", fragment_counter));
        
        if ( null != message ) {
            message.print();
        }
    }
}
