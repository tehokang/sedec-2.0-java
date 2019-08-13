package com.sedec.dvb.ts.container.packets;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class AdaptationField {
    protected int adaptation_field_length;
    protected byte discontinuity_indicator;
    protected byte random_access_indicator;
    protected byte elementary_stream_priority_indicator;
    protected byte PCR_flag;
    protected byte OPCR_flag;
    protected byte splicing_point_flag;
    protected byte transport_private_data_flag;
    protected byte adaptation_field_extension_flag;
    protected int program_clock_reference_base;
    protected int program_clock_reference_extension;
    protected int original_program_clock_reference_base;
    protected int original_program_clock_reference_extension;
    protected byte splice_countdown;
    protected byte transport_private_data_length;
    protected byte[] private_data_byte;
    protected byte adaptation_field_extension_length;
    protected byte ltw_flag;
    protected byte piecewise_rate_flag;
    protected byte seamless_splice_flag;
    protected byte ltw_valid_flag;
    protected int ltw_offset;
    protected int piecewise_rate;
    protected byte splice_type;
    protected byte DTS_next_AU_32_30;
    protected int marker_bit1;
    protected int DTS_next_AU_29_15;
    protected byte marker_bit2;
    protected int DTS_next_AU_14_10;
    protected byte marker_bit3;
    protected byte[] stuffing_byte;

    public int getAdaptationFieldLength() {
        return adaptation_field_length;
    }

    public AdaptationField(BitReadWriter brw) {
        adaptation_field_length = brw.readOnBuffer(8) & 0xff;
        int stuffing_length = adaptation_field_length;

        if ( adaptation_field_length > 0 ) {
            discontinuity_indicator = (byte) brw.readOnBuffer(1);
            random_access_indicator = (byte) brw.readOnBuffer(1);
            elementary_stream_priority_indicator = (byte) brw.readOnBuffer(1);
            PCR_flag = (byte) brw.readOnBuffer(1);
            OPCR_flag = (byte) brw.readOnBuffer(1);
            splicing_point_flag = (byte) brw.readOnBuffer(1);
            transport_private_data_flag = (byte) brw.readOnBuffer(1);
            adaptation_field_extension_flag = (byte) brw.readOnBuffer(1);
            stuffing_length -= 1;

            if ( PCR_flag == 1 ) {
                program_clock_reference_base = brw.readOnBuffer(33);
                brw.skipOnBuffer(6);
                program_clock_reference_extension = brw.readOnBuffer(9);
                stuffing_length -= 6;
            }

            if ( OPCR_flag == 1) {
                original_program_clock_reference_base = brw.readOnBuffer(33);
                brw.skipOnBuffer(6);
                original_program_clock_reference_extension = brw.readOnBuffer(9);
                stuffing_length -= 6;
            }

            if ( splicing_point_flag == 1 ) {
                splice_countdown = (byte) brw.readOnBuffer(8);
                stuffing_length -= 1;
            }

            if ( transport_private_data_flag == 1 ) {
                transport_private_data_length = (byte) brw.readOnBuffer(8);
                private_data_byte = new byte[transport_private_data_length];
                for ( int i=0; i<private_data_byte.length; i++ ) {
                    private_data_byte[i] = (byte) brw.readOnBuffer(8);
                }

                stuffing_length -= ( 1 + private_data_byte.length );
            }

            if ( adaptation_field_extension_flag == 1 ) {
                adaptation_field_extension_length = (byte) brw.readOnBuffer(8);
                int reserved_length = adaptation_field_extension_length;
                ltw_flag = (byte) brw.readOnBuffer(1);
                piecewise_rate_flag = (byte) brw.readOnBuffer(1);
                seamless_splice_flag = (byte) brw.readOnBuffer(1);
                brw.skipOnBuffer(5);
                stuffing_length -= 2;
                reserved_length -= 2;

                if ( ltw_flag == 1 ) {
                    ltw_valid_flag = (byte) brw.readOnBuffer(1);
                    ltw_offset = brw.readOnBuffer(15);
                    stuffing_length -= 2;
                    reserved_length -= 2;
                }

                if ( piecewise_rate_flag == 1 ) {
                    brw.skipOnBuffer(2);;
                    piecewise_rate = brw.readOnBuffer(22);
                    stuffing_length -= 3;
                    reserved_length -= 3;
                }

                if ( seamless_splice_flag == 1 ) {
                    splice_type = (byte) brw.readOnBuffer(4);
                    DTS_next_AU_32_30 = (byte) brw.readOnBuffer(3);
                    marker_bit1 = (byte) brw.readOnBuffer(1);
                    DTS_next_AU_29_15 = (byte) brw.readOnBuffer(15);
                    marker_bit2 = (byte) brw.readOnBuffer(1);
                    DTS_next_AU_14_10 = (byte) brw.readOnBuffer(15);
                    marker_bit3 = (byte) brw.readOnBuffer(1);
                    stuffing_length -= 5;
                    reserved_length -= 5;
                }

                for ( int i=0; i<reserved_length; i++ ) {
                    brw.skipOnBuffer(8);
                    stuffing_length -= 1;
                }
            }

            stuffing_byte = new byte[stuffing_length];
            for ( int i=0; i<stuffing_length; i++ ) {
                stuffing_byte[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public void print() {
        Logger.d(String.format("- Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("adaptation_field_length : 0x%x \n", adaptation_field_length));

        if ( adaptation_field_length > 0 ) {
            Logger.d(String.format("discontinuity_indicator : 0x%x \n",
                    discontinuity_indicator));
            Logger.d(String.format("random_access_indicator : 0x%x \n",
                    random_access_indicator));
            Logger.d(String.format("elementary_stream_priority_indicator : 0x%x \n",
                    elementary_stream_priority_indicator));
            Logger.d(String.format("PCR_flag : 0x%x \n", PCR_flag));
            Logger.d(String.format("OPCR_flag : 0x%x \n", OPCR_flag));
            Logger.d(String.format("splicing_point_flag : 0x%x \n", splicing_point_flag));
            Logger.d(String.format("transport_private_data_flag : 0x%x \n",
                    transport_private_data_flag));
            Logger.d(String.format("adaptation_field_extension_flag : 0x%x \n",
                    adaptation_field_extension_flag));

            if ( PCR_flag == 1 ) {
                Logger.d(String.format("program_clock_reference_base : 0x%x \n",
                        program_clock_reference_base));
                Logger.d(String.format("program_clock_reference_extension : 0x%x \n",
                        program_clock_reference_extension));
            }

            if ( OPCR_flag == 1) {
                Logger.d(String.format("original_program_clock_reference_base : 0x%x \n",
                        original_program_clock_reference_base));
                Logger.d(String.format("original_program_clock_reference_extension : 0x%x \n",
                        original_program_clock_reference_extension));
            }

            if ( splicing_point_flag == 1 ) {
                Logger.d(String.format("splice_countdown : 0x%x \n", splice_countdown));
            }

            if ( transport_private_data_flag == 1 ) {
                Logger.d(String.format("transport_private_data_length : 0x%x \n",
                        transport_private_data_length));
                Logger.d(String.format("private_data_byte : \n"));
                BinaryLogger.print(private_data_byte);
            }

            if ( adaptation_field_extension_flag == 1 ) {
                Logger.d(String.format("adaptation_field_extension_length : 0x%x \n",
                        adaptation_field_extension_length));
                Logger.d(String.format("ltw_flag : 0x%x \n", ltw_flag));
                Logger.d(String.format("piecewise_rate_flag : 0x%x \n", piecewise_rate_flag));
                Logger.d(String.format("seamless_splice_flag : 0x%x \n", seamless_splice_flag));

                if ( ltw_flag == 1 ) {
                    Logger.d(String.format("ltw_valid_flag : 0x%x \n", seamless_splice_flag));
                    Logger.d(String.format("ltw_offset : 0x%x \n", ltw_offset));
                }

                if ( piecewise_rate_flag == 1 ) {
                    Logger.d(String.format("piecewise_rate : 0x%x \n", piecewise_rate));
                }

                if ( seamless_splice_flag == 1 ) {
                    Logger.d(String.format("splice_type : 0x%x \n", splice_type));
                    Logger.d(String.format("DTS_next_AU_32_30 : 0x%x \n", DTS_next_AU_32_30));
                    Logger.d(String.format("marker_bit1 : 0x%x \n", marker_bit1));
                    Logger.d(String.format("DTS_next_AU_29_15 : 0x%x \n", DTS_next_AU_29_15));
                    Logger.d(String.format("marker_bit2 : 0x%x \n", marker_bit2));
                    Logger.d(String.format("DTS_next_AU_14_10 : 0x%x \n", DTS_next_AU_14_10));
                    Logger.d(String.format("marker_bit3 : 0x%x \n", marker_bit3));
                }
            }
            Logger.d(String.format("stuffing_byte : \n"));
            BinaryLogger.print(stuffing_byte);
        }
        Logger.d(String.format("- End of %s - \n", getClass().getName()));
    }
}
