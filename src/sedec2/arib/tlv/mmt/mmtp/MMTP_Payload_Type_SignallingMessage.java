package sedec2.arib.tlv.mmt.mmtp;

import sedec2.arib.tlv.mmt.si.MessageFactory;
import sedec2.arib.tlv.mmt.si.messages.Message;
import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MMTP_Payload_Type_SignallingMessage {
    protected byte fragmentation_indicator;
    protected byte length_extension_flag;
    protected byte aggregation_flag;
    protected byte fragment_counter;
    
    protected Message message = null;
    protected int message_length;
    protected byte[] message_byte;
    
    public MMTP_Payload_Type_SignallingMessage(BitReadWriter brw) {
        fragmentation_indicator = (byte) brw.ReadOnBuffer(2);
        brw.SkipOnBuffer(4);
        length_extension_flag = (byte) brw.ReadOnBuffer(1);
        aggregation_flag = (byte) brw.ReadOnBuffer(1);
        fragment_counter = (byte) brw.ReadOnBuffer(8);
        
        if ( aggregation_flag == 0x00 ) {
            /**
             * @todo How to calculate the length of message_byte
             */
            message = MessageFactory.CreateMessage(brw.GetCurrentBuffer());
        } else {
            if ( length_extension_flag == 0x01 ) {
                message_length = brw.ReadOnBuffer(32);
            } else {
                message_length = brw.ReadOnBuffer(16);
            }
            
            message_byte = new byte[message_length];
            for ( int i=0; i<message_byte.length; i++ ) {
                message_byte[i] = (byte) brw.ReadOnBuffer(8);
            }
            
            message = MessageFactory.CreateMessage(message_byte);
        }
    }
    
    public void Print() {
        Logger.d(String.format("------- MMTP ------- (%s)\n", getClass().getName()));

        Logger.d(String.format("fragmentation_indicator : 0x%x \n", fragmentation_indicator));
        Logger.d(String.format("length_extension_flag : 0x%x \n", length_extension_flag));
        Logger.d(String.format("aggregation_flag : 0x%x \n", aggregation_flag));
        Logger.d(String.format("fragment_counter : 0x%x \n", fragment_counter));
        
        if ( null != message ) {
            message.PrintMessage();
        }
    }
}
