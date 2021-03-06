package com.sedec.arib.tlv.container.mmtp.mfu;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

/**
 * Class which refers to Table 9-1 configuration of MFU for closed-caption in ARIB B60.
 */
public class MFU_ClosedCaption extends BitReadWriter {
    protected byte subtitle_tag;
    protected byte subtitle_sequence_number;
    protected byte subsample_number;
    protected byte last_subsample_number;
    protected byte data_type;
    protected byte length_extension_flag;
    protected byte subsample_info_list_flag;
    protected int data_size;
    protected List<SubSample> subsamples = new ArrayList<>();
    protected byte[] data_byte;

    public class SubSample {
        public byte subsample_i_data_type;
        public int subsample_i_data_size;
    }

    /**
     * Constructor to decode Table 9-1 configuration of MFU for closed-caption in ARIB B60.
     * @param buffer MFU_data_byte of MMT packet payload.
     */
    public MFU_ClosedCaption(byte[] buffer) {
        super(buffer);

        subtitle_tag = (byte) readOnBuffer(8);
        subtitle_sequence_number = (byte) readOnBuffer(8);
        subsample_number = (byte) readOnBuffer(8);
        last_subsample_number = (byte) readOnBuffer(8);
        data_type = (byte) readOnBuffer(4);
        length_extension_flag = (byte) readOnBuffer(1);
        subsample_info_list_flag = (byte) readOnBuffer(1);
        skipOnBuffer(2);

        if ( length_extension_flag == 0x01 ) {
            data_size = readOnBuffer(32);
        } else {
            data_size = readOnBuffer(16);
        }

        if ( subsample_number == 0 &&
                last_subsample_number > 0 &&
                subsample_info_list_flag == 1 ) {
            /**
             * TODO Should verify Table 9-1 of ARIB B60
             */
            for ( int i=0; i<last_subsample_number; i++ ) {
                SubSample ss = new SubSample();
                ss.subsample_i_data_type = (byte) readOnBuffer(4);
                skipOnBuffer(4);

                if ( length_extension_flag == 1 ) {
                    ss.subsample_i_data_size = readOnBuffer(32);
                } else {
                    ss.subsample_i_data_size = readOnBuffer(16);
                }
                subsamples.add(ss);
            }
        }

        int remaining_length = buffer.length- 5 -
                (length_extension_flag==1?4:2) -
                (subsamples.size()*(length_extension_flag==1?5:3));

        data_byte = new byte[remaining_length];
        for ( int i=0; i<data_byte.length; i++ ) {
            data_byte[i] = (byte) readOnBuffer(8);
        }
    }

    /**
     * Gets subtitle_tag of Table 9-1 of ARIB B60
     * @return subtitle_tag
     */
    public byte getSubtitleTag() {
        return subtitle_tag;
    }

    /**
     * Gets subtitle_sequence_number of Table 9-1 of ARIB B60
     * @return subtitle_sequence_number
     */
    public byte getSubtitleSequenceNumber() {
        return subtitle_sequence_number;
    }

    /**
     * Gets subsample_number of Table 9-1 of ARIB B60
     * @return subsample_number
     */
    public byte getSubsampleNumber() {
        return subsample_number;
    }

    /**
     * Gets last_subsample_number of Table 9-1 of ARIB B60
     * @return last_subsample_number
     */
    public byte getLastSubsampleNumber() {
        return last_subsample_number;
    }

    /**
     * Gets data_type of Table 9-1 of ARIB B60
     * @return data_type
     */
    public byte getDataType() {
        return data_type;
    }

    /**
     * Gets length_extension_flag of Table 9-1 of ARIB B60
     * @return length_extension_flag
     */
    public byte getLengthExtensionFlag() {
        return length_extension_flag;
    }

    /**
     * Gets subsample_info_list_flag of Table 9-1 of ARIB B60
     * @return subsample_info_list_flag
     */
    public byte getSubsampleInfoListFlag() {
        return subsample_info_list_flag;
    }

    /**
     * Gets subsamples of Table 9-1 of ARIB B60
     * @return subsamples which comes from subsample_i_data_type to subsample_i_data_size
     */
    public List<SubSample> getSubsamples() {
        return subsamples;
    }

    /**
     * Gets data_byte of Table 9-1 of ARIB B60
     * @return data_byte as byte buffer
     */
    public byte[] getDataByte() {
        return data_byte;
    }

    /**
     * Prints all of properties it has.
     */
    public void print() {
        Logger.d(String.format("- MFU_data_byte (ClosedCaption) (%s)\n", getClass().getName()));
        Logger.d(String.format("subtitle_tag : 0x%x \n", subtitle_tag));
        Logger.d(String.format("subtitle_sequence_number : 0x%x \n", subtitle_sequence_number));
        Logger.d(String.format("subsample_number : 0x%x \n", subsample_number));
        Logger.d(String.format("last_subsample_number : 0x%x \n",  last_subsample_number));
        Logger.d(String.format("data_type : 0x%x \n", data_type));
        Logger.d(String.format("length_extension_flag : 0x%x \n", length_extension_flag));
        Logger.d(String.format("subsample_info_list_flag : 0x%x \n", subsample_info_list_flag));
        Logger.d(String.format("data_size : 0x%x (%d) \n", data_size, data_size));

        for ( int i=0; i<subsamples.size(); i++ ) {
            SubSample ss = subsamples.get(i);
            Logger.d(String.format("\t [%d] subsample_i_data_type : 0x%x \n",
                    ss.subsample_i_data_type));
            Logger.d(String.format("\t [%d] subsample_i_data_size : 0x%x \n",
                    ss.subsample_i_data_size));
        }

        Logger.d(String.format("data_byte is " ));
        if ( data_type == 0x0000 ) {
            Logger.d(String.format("TTHML-DOC \n"));
            Logger.d(String.format("%s \n", new String(data_byte)));
        } else if ( data_type == 0x0001 ) {
            Logger.d(String.format("TTML-PNG \n"));
            BinaryLogger.print(data_byte, 10);
        } else if ( data_type == 0x0002 ) {
            Logger.d(String.format("TTML-SVG \n"));
            BinaryLogger.print(data_byte, 10);
        } else if ( data_type == 0x0003 ) {
            Logger.d(String.format("TTML-PCM \n"));
            BinaryLogger.print(data_byte, 10);
        } else if ( data_type == 0x0004 ) {
            Logger.d(String.format("TTML-MP3 \n"));
            BinaryLogger.print(data_byte, 10);
        } else if ( data_type == 0x0005 ) {
            Logger.d(String.format("TTML-AAC \n"));
            BinaryLogger.print(data_byte, 10);
        } else if ( data_type == 0x0006 ) {
            Logger.d(String.format("TTML-FONT-SVG \n"));
            BinaryLogger.print(data_byte, 10);
        } else if ( data_type == 0x0007 ) {
            Logger.d(String.format("TTML-FONT_WOFF \n"));
            BinaryLogger.print(data_byte, 10);
        } else {
            Logger.d("\n");
            BinaryLogger.print(data_byte);
        }
    }
}
