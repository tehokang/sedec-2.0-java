package sedec2.arib.tlv.container.mmtp;

import java.util.Arrays;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * Class to describe signaling message of Table 6-1 in ARIB B60.
 */
public class MMTP_Payload_SignalingMessage {
    protected byte fragmentation_indicator;
    protected byte length_extension_flag;
    protected byte aggregation_flag;
    protected byte fragment_counter;

    protected int message_length;
    protected byte[] message_byte;

    /**
     * Constructor to decode signaling message with BitReadWriter of MMTP_Packet.
     * @param brw BitReadWriter which MMTP_Packet own.
     */
    public MMTP_Payload_SignalingMessage(BitReadWriter brw) {
        fragmentation_indicator = (byte) brw.readOnBuffer(2);
        brw.skipOnBuffer(4);
        length_extension_flag = (byte) brw.readOnBuffer(1);
        aggregation_flag = (byte) brw.readOnBuffer(1);
        fragment_counter = (byte) brw.readOnBuffer(8);

        if ( aggregation_flag == 0x00 ) {
            message_byte = new byte[brw.getCurrentBuffer().length];
            for ( int i=0; i<message_byte.length; i++ ) {
                message_byte[i] = (byte) brw.readOnBuffer(8);
            }
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
        }
    }

    /**
     * Gets message_byte of Table 6-1 of ARIB B60.
     * @return message_byte
     *
     * message_byte could have Message like PA, CA, ... etc.
     */
    public byte[] getMessageByte() {
        return Arrays.copyOfRange(message_byte, 0, message_byte.length);
    }

    /**
     * Gets fragmentation_indicator of Table 6-1 of ARIB B60.
     * @return fragmentation_indicator
     */
    public byte getFragmentationIndicator() {
        return fragmentation_indicator;
    }

    /**
     * Gets length_extension_flag of Table 6-1 of ARIB B60.
     * @return length_extension_flag
     */
    public byte getLengthExtensionFlag() {
        return length_extension_flag;
    }

    /**
     * Gets aggregation_flag of Table 6-1 of ARIB B60.
     * @return aggregation_flag
     */
    public byte getAggregationFlag() {
        return aggregation_flag;
    }

    /**
     * Gets fragment_counter of Table 6-1 of ARIB B60.
     * @return fragment_counter
     */
    public byte getFragmentCounter() {
        return fragment_counter;
    }

    /**
     * Prints all of properties it has.
     */
    public void print() {
        Logger.d(String.format("------- MMTP Payload ------- (%s)\n", getClass().getName()));
        Logger.d(String.format("fragmentation_indicator : 0x%x \n", fragmentation_indicator));
        Logger.d(String.format("length_extension_flag : 0x%x \n", length_extension_flag));
        Logger.d(String.format("aggregation_flag : 0x%x \n", aggregation_flag));
        Logger.d(String.format("fragment_counter : 0x%x \n", fragment_counter));
    }
}
