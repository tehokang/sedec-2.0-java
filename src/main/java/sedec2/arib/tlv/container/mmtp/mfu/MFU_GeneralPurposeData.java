package sedec2.arib.tlv.container.mmtp.mfu;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

/**
 * Class which refers to Table 12-1 configuration of MFU for general-purpose data in ARIB B60.
 */
public class MFU_GeneralPurposeData extends BitReadWriter {
    protected int component_tag;
    protected byte data_type_length;
    protected byte[] data_type_char;
    protected int additional_info_length;
    protected byte[] additional_info_byte;
    protected int data_length;
    protected byte[] data_byte;

    /**
     * Constructor to decode Table 12-1 configuration of MFU for general-purpose data in ARIB B60.
     * @param buffer MFU_data_byte of MMT packet payload.
     */
    public MFU_GeneralPurposeData(byte[] buffer) {
        super(buffer);

        component_tag = readOnBuffer(16);
        data_type_length = (byte) readOnBuffer(8);
        data_type_char = new byte[data_type_length];

        for ( int i=0; i<data_type_char.length; i++ ) {
            data_type_char[i] = (byte) readOnBuffer(8);
        }

        additional_info_length = readOnBuffer(16);
        additional_info_byte = new byte[additional_info_length];

        for ( int i=0; i<additional_info_byte.length; i++ ) {
            additional_info_byte[i] = (byte) readOnBuffer(8);
        }

        data_length = readOnBuffer(32);
        data_byte = new byte[data_length];

        for ( int i=0; i<data_byte.length; i++ ) {
            data_byte[i] = (byte) readOnBuffer(8);
        }
    }

    /**
     * Gets component_tag of Table 12-1 of ARIB B60.
     * @return component_tag
     */
    public int getComponentTag() {
        return component_tag;
    }

    /**
     * Gets data_type_length of Table 12-1 of ARIB B60.
     * @return data_type_length
     */
    public byte getDataTypeLength() {
        return data_type_length;
    }

    /**
     * Gets data_type_char of Table 12-1 of ARIB B60.
     * @return data_type_char as byte buffer
     */
    public byte[] getDataTypeChar() {
        return data_type_char;
    }

    /**
     * Gets additional_info_length of Table 12-1 of ARIB B60.
     * @return additional_info_length
     */
    public int getAdditionalInfoLength() {
        return additional_info_length;
    }

    /**
     * Gets additional_info_byte of Table 12-1 of ARIB B60.
     * @return additional_info_byte as byte buffer
     */
    public byte[] getAdditionalInfoByte() {
        return additional_info_byte;
    }

    /**
     * Gets data_length of Table 12-1 of ARIB B60.
     * @return data_length
     */
    public int getDataLength() {
        return data_length;
    }

    /**
     * Gets data_byte of Table 12-1 of ARIB B60.
     * @return data_byte as byte buffer
     */
    public byte[] getDataByte() {
        return data_byte;
    }

    /**
     * Prints all of properties it has.
     */
    public void print() {
        Logger.d(String.format("- MFU_data_byte (GeneralPurpose) (%s)\n", getClass().getName()));
        Logger.d(String.format("component_tag : 0x%x \n", component_tag));
        Logger.d(String.format("data_type_length : 0x%x (%d) \n",
                data_type_length, data_type_length));
        BinaryLogger.print(data_type_char);

        Logger.d(String.format("additional_info_length : 0x%x (%d) \n",
                additional_info_length, additional_info_length));

        BinaryLogger.print(additional_info_byte);

        Logger.d(String.format("data_length : 0x%x (%d) \n", data_length, data_length));
        BinaryLogger.print(data_byte);
    }
}
