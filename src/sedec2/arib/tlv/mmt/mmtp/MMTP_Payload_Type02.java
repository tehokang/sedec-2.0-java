package sedec2.arib.tlv.mmt.mmtp;

import sedec2.base.BitReadWriter;

public class MMTP_Payload_Type02 {
    protected byte fragmentation_indicator;
    protected byte length_extension_flag;
    protected byte aggregation_flag;
    protected byte fragment_counter;
    
    protected int message_length;
    protected byte[] message_byte;
    
    public MMTP_Payload_Type02(BitReadWriter brw) {
        fragmentation_indicator = (byte) brw.ReadOnBuffer(2);
        brw.SkipOnBuffer(4);
        length_extension_flag = (byte) brw.ReadOnBuffer(1);
        aggregation_flag = (byte) brw.ReadOnBuffer(1);
        fragment_counter = (byte) brw.ReadOnBuffer(8);
        
        if ( aggregation_flag == 0x00 ) {
            message_byte = new byte[payload_length-2];
            
            for ( int i=0; i<message_byte.length; i++ ) {
                message_byte[i] = (byte) brw.ReadOnBuffer(8);
            }
        } else {
            
        }
    }
}
