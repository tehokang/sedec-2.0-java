package sedec2.dvb.ts.container.packets;

import sedec2.base.BitReadWriter;

public class PacketizedElementaryStream extends BitReadWriter {
    protected static final byte PROGRAM_STREAM_MAP = (byte) 0b10111100;
    protected static final byte PRIVATE_STREAM_1 = (byte) 0b10111101;
    protected static final byte PADDING_STREAM = (byte) 0b10111110;
    protected static final byte PRIVATE_STREAM_2 = (byte) 0b10111111;
    protected static final byte ECM_STREAM = (byte) 0b11110000;
    protected static final byte EMM_STREAM = (byte) 0b11110001;
    protected static final byte DSMCC_STREAM = (byte) 0b11110010;
    protected static final byte H222_TYPE_A = (byte) 0b11110100;
    protected static final byte H222_TYPE_B = (byte) 0b11110101;
    protected static final byte H222_TYPE_C = (byte) 0b11110110;
    protected static final byte H222_TYPE_D = (byte) 0b11110111;
    protected static final byte H222_TYPE_E = (byte) 0b11111000;
    protected static final byte ANCILLARY_STREAM = (byte) 0b11111001;
    protected static final byte PROGRAM_STREAM_DIRECTORY = (byte) 0b11111111;

    protected int packet_start_code_prefix;
    protected byte stream_id;
    protected int PES_packet_length;

    protected byte PES_scrambling_control;
    protected byte PES_priority;
    protected byte data_alignment_indicator;
    protected byte copyright;
    protected byte original_or_copy;
    protected byte PTS_DTS_flags;
    protected byte ESCR_flag;
    protected byte ES_rate_flag;
    protected byte DSM_trick_mode_flag;
    protected byte additional_copy_info_flag;
    protected byte PES_CRC_flag;
    protected byte PES_extension_flag;
    protected byte PES_header_data_length;

    protected byte PTS_32_30;
    protected byte marker_bit1;
    protected int PTS_29_15;
    protected byte marker_bit2;
    protected int PTS_14_0;
    protected byte marker_bit3;

    protected byte DTS_32_30;
    protected int DTS_29_15;
    protected int DTS_14_0;

    protected byte ESCR_base_32_30;
    protected int ESCR_base_29_15;
    protected int ESCR_base_14_0;
    protected int ESCR_extension;
    protected byte marker_bit4;
    protected byte marker_bit5;
    protected byte marker_bit6;

    protected int ES_rate;

    protected byte trick_mode_control;
    protected byte field_id;
    protected byte intra_slice_refresh;
    protected byte frequency_truncation;
    protected byte rep_cntrl;
    protected byte additional_copy_info;
    protected int previous_PES_packet_CRC;

    protected byte PES_private_data_flag;
    protected byte pack_header_field_flag;
    protected byte program_packet_sequence_counter_flag;
    protected byte PSTD_buffer_flag;
    protected byte PES_extension_flag_2;

    protected byte[] PES_private_data = new byte[16];
    protected byte pack_field_length;
    protected byte program_packet_sequence_counter;
    protected byte MPEG1_MPEG2_indentifier;
    protected byte original_stuff_length;

    protected byte PSTD_buffer_scale;
    protected int PSTD_buffer_size;

    protected byte PES_extension_field_length;
    protected byte[] stuffing_byte;
    protected byte[] PES_packet_data_byte;
    protected byte[] padding_byte;

    public PacketizedElementaryStream(byte[] buffer) {
        super(buffer);

        packet_start_code_prefix = readOnBuffer(24);
        stream_id = (byte) readOnBuffer(8);
        PES_packet_length = readOnBuffer(16);

        int remaining_length = PES_packet_length;
        if ( stream_id != PROGRAM_STREAM_MAP &&
                stream_id != PADDING_STREAM &&
                stream_id != PRIVATE_STREAM_2 &&
                stream_id != ECM_STREAM &&
                stream_id != EMM_STREAM &&
                stream_id != PROGRAM_STREAM_DIRECTORY &&
                stream_id != DSMCC_STREAM &&
                stream_id != H222_TYPE_E ) {
            skipOnBuffer(2);
            PES_scrambling_control = (byte) readOnBuffer(2);
            PES_priority = (byte) readOnBuffer(1);
            data_alignment_indicator = (byte) readOnBuffer(1);
            copyright = (byte) readOnBuffer(1);
            original_or_copy = (byte) readOnBuffer(1);
            PTS_DTS_flags = (byte) readOnBuffer(2);
            ESCR_flag = (byte) readOnBuffer(1);
            ES_rate_flag = (byte) readOnBuffer(1);
            DSM_trick_mode_flag = (byte) readOnBuffer(1);
            additional_copy_info_flag = (byte) readOnBuffer(1);
            PES_CRC_flag = (byte) readOnBuffer(1);
            PES_extension_flag = (byte) readOnBuffer(1);
            PES_header_data_length = (byte) readOnBuffer(8);
            remaining_length -= 3;

            if ( PTS_DTS_flags == 0b10 ) {
                skipOnBuffer(4);
                PTS_32_30 = (byte) readOnBuffer(3);
                marker_bit1 = (byte) readOnBuffer(1);
                PTS_29_15 = readOnBuffer(15);
                marker_bit2 = (byte) readOnBuffer(1);
                PTS_14_0 = readOnBuffer(15);
                marker_bit2 = (byte) readOnBuffer(1);
                remaining_length -= 5;
            }

            if ( PTS_DTS_flags == 0b11 ) {
                skipOnBuffer(4);
                PTS_32_30 = (byte) readOnBuffer(3);
                marker_bit1 = (byte) readOnBuffer(1);
                PTS_29_15 = readOnBuffer(15);
                marker_bit2 = (byte) readOnBuffer(1);
                PTS_14_0 = readOnBuffer(15);
                marker_bit3 = (byte) readOnBuffer(1);
                skipOnBuffer(4);
                DTS_32_30 = (byte) readOnBuffer(3);
                marker_bit4 = (byte) readOnBuffer(1);
                DTS_29_15 = readOnBuffer(15);
                marker_bit5 = (byte) readOnBuffer(1);
                DTS_14_0 = readOnBuffer(15);
                marker_bit6 = (byte) readOnBuffer(1);
                remaining_length -= 10;
            }

            if ( ESCR_flag == 0b1 ) {
                skipOnBuffer(2);
                ESCR_base_32_30 = (byte) readOnBuffer(3);
                marker_bit1 = (byte) readOnBuffer(1);
                ESCR_base_29_15 = readOnBuffer(15);
                marker_bit2 = (byte) readOnBuffer(1);
                ESCR_base_14_0 = readOnBuffer(15);
                marker_bit3 = (byte) readOnBuffer(1);
                ESCR_extension = readOnBuffer(9);
                marker_bit4 = (byte) readOnBuffer(1);
                remaining_length -= 6;
            }

            if ( ES_rate_flag == 0b1 ) {
                marker_bit1 = (byte) readOnBuffer(1);
                ES_rate = readOnBuffer(22);
                marker_bit2 = (byte) readOnBuffer(1);
                remaining_length -= 3;
            }

            if ( DSM_trick_mode_flag == 0b1 ) {
                trick_mode_control = (byte) readOnBuffer(3);
                if ( trick_mode_control == 0b000 /* fast forward */ ) {
                    field_id = (byte) readOnBuffer(2);
                    intra_slice_refresh = (byte) readOnBuffer(1);
                    frequency_truncation = (byte) readOnBuffer(2);
                } else if ( trick_mode_control == 0b001 /* slow motion */ ) {
                    rep_cntrl = (byte) readOnBuffer(5);
                } else if ( trick_mode_control == 0b010 /* freeze frame */ ) {
                    field_id = (byte) readOnBuffer(2);
                } else if ( trick_mode_control == 0b011 /* fast reverse */ ) {
                    field_id = (byte) readOnBuffer(2);
                    intra_slice_refresh = (byte) readOnBuffer(1);
                    frequency_truncation = (byte) readOnBuffer(2);
                } else if ( trick_mode_control == 0b100 /* slow reverse */ ) {
                    rep_cntrl = (byte) readOnBuffer(5);
                } else {
                    skipOnBuffer(5);
                }
                remaining_length -= 1;
            }

            if ( additional_copy_info_flag == 0b1 ) {
                marker_bit1 = (byte) readOnBuffer(1);
                additional_copy_info = (byte) readOnBuffer(7);
                remaining_length -= 1;
            }

            if ( PES_CRC_flag == 0b1 ) {
                previous_PES_packet_CRC = readOnBuffer(16);
                remaining_length -= 2;
            }

            if ( PES_extension_flag == 0b1 ) {
                PES_private_data_flag = (byte) readOnBuffer(1);
                pack_header_field_flag = (byte) readOnBuffer(1);
                program_packet_sequence_counter_flag = (byte) readOnBuffer(1);
                PSTD_buffer_flag = (byte) readOnBuffer(1);
                skipOnBuffer(3);
                PES_extension_flag_2 = (byte) readOnBuffer(1);
                remaining_length -= 1;

                if ( PES_private_data_flag == 0b1 ) {
                    for ( int i=0; i<PES_private_data.length; i++ ) {
                        PES_private_data[i] = (byte) readOnBuffer(8);
                    }
                    remaining_length -= (PES_private_data.length);
                }

                if ( pack_header_field_flag == 0b1 ) {
                    pack_field_length = (byte) readOnBuffer(8);
                    skipOnBuffer(pack_field_length*8);
                    remaining_length -= ( 1 + pack_field_length);
                }

                if ( program_packet_sequence_counter_flag == 0b1 ) {
                    marker_bit1 = (byte) readOnBuffer(1);
                    program_packet_sequence_counter = (byte) readOnBuffer(7);
                    marker_bit2 = (byte) readOnBuffer(1);
                    MPEG1_MPEG2_indentifier = (byte) readOnBuffer(1);
                    original_stuff_length = (byte) readOnBuffer(6);
                    remaining_length -= 2;
                }

                if ( PSTD_buffer_flag == 0b1 ) {
                    skipOnBuffer(2);
                    PSTD_buffer_scale = (byte) readOnBuffer(1);
                    PSTD_buffer_size = readOnBuffer(13);
                    remaining_length -= 2;
                }

                if ( PES_extension_flag_2 == 0b1 ) {
                    marker_bit1 = (byte) readOnBuffer(1);
                    PES_extension_field_length = (byte) readOnBuffer(7);
                    skipOnBuffer(PES_extension_field_length*8);
                    remaining_length -= ( 1 + PES_extension_field_length );
                }
            }
            // stuffing_byte
            skipOnBuffer(8);
            remaining_length -= 1;

            // PES_packet_data_byte
            PES_packet_data_byte = new byte[remaining_length];
            for ( int i=0; i<PES_packet_data_byte.length; i++ ) {
                PES_packet_data_byte[i] = (byte) readOnBuffer(8);
            }
        } else if ( stream_id == PROGRAM_STREAM_MAP ||
                stream_id == PRIVATE_STREAM_2 ||
                stream_id == ECM_STREAM ||
                stream_id == EMM_STREAM ||
                stream_id == PROGRAM_STREAM_DIRECTORY ||
                stream_id == DSMCC_STREAM ||
                stream_id == H222_TYPE_E ) {
            // PES_packet_data_byte
            PES_packet_data_byte = new byte[PES_packet_length];
            for ( int i=0; i<PES_packet_data_byte.length; i++ ) {
                PES_packet_data_byte[i] = (byte) readOnBuffer(8);
            }
        } else if ( stream_id == PADDING_STREAM ) {
            // padding_byte
            padding_byte = new byte[PES_packet_length];
            for ( int i=0; i<padding_byte.length; i++ ) {
                padding_byte[i] = (byte) readOnBuffer(8);
            }
        }
    }
}
