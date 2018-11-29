package sedec2.arib.tlv.container.mmtp.mfu;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * Class to decode NAL unit of HEVC, especially for some of non-VCL like VPS, SPS, PPS, AUD.
 *
 * Decoding routine refers to Table 7-1 NAL unit type codes and NAL unit type classes
 * <ul>
 * <li> VPS : Video Parameter Set
 * <li> SPS : Sequence Parameter Set
 * <li> PPS : Picture PArameter Set
 * <li> AUD : Access Unit Delimiter
 * </ul>
 */
public class MFU_H265NalUnit extends BitReadWriter {
    protected static final String TAG = MFU_H265NalUnit.class.getSimpleName();

    public static final int VPS = 32;
    public static final int SPS = 33;
    public static final int PPS = 34;
    public static final int AUD = 35;

    protected int nal_size;

    protected byte forbidden_zero_bit;
    protected byte nal_unit_type;
    protected byte nuh_layer_id;
    protected byte nuh_temporal_id_plus1;
    protected SPS seq_parameter_set_rbsp;
    protected AUD access_unit_delimiter_rbsp;

    protected byte trailing_zero_8bits;

    public static final float[] ASPECT_RATIO_IDC_VALUES = new float[] {
            1f /* Unspecified. Assume square */,
            1f,
            12f / 11f,
            10f / 11f,
            16f / 11f,
            40f / 33f,
            24f / 11f,
            20f / 11f,
            32f / 11f,
            80f / 33f,
            18f / 11f,
            15f / 11f,
            64f / 33f,
            160f / 99f,
            4f / 3f,
            3f / 2f,
            2f
    };

    public class SPS {
        public byte sps_max_sub_layers_minus1;
        public int chroma_format_idc;
        public int pic_width_in_luma_samples;
        public int pic_height_in_luma_samples;
        public int conf_win_left_offset;
        public int conf_win_right_offset;
        public int conf_win_top_offset;
        public int conf_win_bottom_offset;
        public int log2_max_pic_order_cnt_lsb_minus4;
        public byte scaling_list_enabled_flag;
        public byte aspect_ratio_idc;
        public int sar_width;
        public int sar_height;
        public float pixel_width_height_ratio = 1;

        public SPS(BitReadWriter brw) {
            brw.skipOnBuffer(4); // sps_video_parameter_set_id
            sps_max_sub_layers_minus1 = (byte) readOnBuffer(3);
            brw.skipOnBuffer(1); // sps_temporal_id_nesting_flag

            // profile_tier_level(1, sps_max_sub_layers_minus1)
            brw.skipOnBuffer(88); // if (profilePresentFlag) { ... }
            brw.skipOnBuffer(8); // general_level_idc

            int to_skip = 0;
            for ( int i=0; i<sps_max_sub_layers_minus1; i++ ) {
                if ( readOnBuffer(1) != 0 ) { // sub_layer_profile_present_flag[i]
                    to_skip += 89;
                }
                if ( readOnBuffer(1) != 0 ) { // sub_layer_level_present_flag[i]
                    to_skip +=8;
                }
            }
            brw.skipOnBuffer(to_skip);
            if ( sps_max_sub_layers_minus1 > 0 ) {
                brw.skipOnBuffer(2 * ( 8 - sps_max_sub_layers_minus1));
            }

            brw.readUnsignedExpGolombCodedInt(); // sps_seq_parameter_set_id
            chroma_format_idc = brw.readUnsignedExpGolombCodedInt();
            if ( chroma_format_idc == 3 ) {
                brw.skipOnBuffer(1); // seperate_colour_plane_flag
            }

            pic_width_in_luma_samples = brw.readUnsignedExpGolombCodedInt();
            pic_height_in_luma_samples = brw.readUnsignedExpGolombCodedInt();

            if ( brw.readOnBuffer(1) != 0 ) { // conformance_window_flag
                conf_win_left_offset = brw.readUnsignedExpGolombCodedInt();
                conf_win_right_offset = brw.readUnsignedExpGolombCodedInt();
                conf_win_top_offset = brw.readUnsignedExpGolombCodedInt();
                conf_win_bottom_offset = brw.readUnsignedExpGolombCodedInt();
                // H.265/HEVC (2014) Table 6-1
                int sub_width_c = chroma_format_idc == 1 || chroma_format_idc == 2 ? 2 : 1;
                int sub_height_c = chroma_format_idc == 1 ? 2 : 1;
                pic_width_in_luma_samples -=
                        sub_width_c * (conf_win_left_offset + conf_win_right_offset);
                pic_height_in_luma_samples -=
                        sub_height_c * (conf_win_top_offset + conf_win_bottom_offset);
            }

            brw.readUnsignedExpGolombCodedInt(); // bit_depth_luma_minus8
            brw.readUnsignedExpGolombCodedInt(); // bit_depth_chroma_minus8

            log2_max_pic_order_cnt_lsb_minus4 = brw.readUnsignedExpGolombCodedInt();
            // for (i = sps_sub_layer_ordering_info_present_flag ? 0 : sps_max_sub_layers_minus1; ...)
            for ( int i = brw.readOnBuffer(1)  != 0 ? 0 : sps_max_sub_layers_minus1;
                    i<=sps_max_sub_layers_minus1; i++ ) {
                brw.readUnsignedExpGolombCodedInt(); // sps_max_dec_pic_buffering_minus1[i]
                brw.readUnsignedExpGolombCodedInt(); // sps_max_num_reorder_pics[i]
                brw.readUnsignedExpGolombCodedInt(); // sps_max_latency_increase_plus1[i]
            }
            brw.readUnsignedExpGolombCodedInt(); // log2_min_luma_coding_block_size_minus3
            brw.readUnsignedExpGolombCodedInt(); // log2_diff_max_min_luma_coding_block_size
            brw.readUnsignedExpGolombCodedInt(); // log2_min_luma_transform_block_size_minus2
            brw.readUnsignedExpGolombCodedInt(); // log2_diff_max_min_luma_transform_block_size
            brw.readUnsignedExpGolombCodedInt(); // max_transform_hierarchy_depth_inter
            brw.readUnsignedExpGolombCodedInt(); // max_transform_hierarchy_depth_intra

            scaling_list_enabled_flag = (byte) brw.readOnBuffer(1);
            if ( scaling_list_enabled_flag != 0 && brw.readOnBuffer(1) != 0 ) {
                skipScalingList(brw);
            }

            brw.skipOnBuffer(2); // amp_enabled_flag (1), sample_adaptive_offset_enabled_flag (1)
            if ( brw.readOnBuffer(1) != 0 ) { // pcm_enabled_flag
                // pcm_sample_bit_depth_luma_minus1 (4), pcm_sample_bit_depth_chroma_minus1 (4)
                brw.skipOnBuffer(8);
                brw.readUnsignedExpGolombCodedInt(); // log2_min_pcm_luma_coding_block_size_minus3
                brw.readUnsignedExpGolombCodedInt(); // log2_diff_max_min_pcm_luma_coding_block_size
                brw.skipOnBuffer(1); // pcm_loop_filter_disabled_flag
            }

            // Skips all short term reference picture sets.
            skipShortTermRefPicSets(brw);

            if ( brw.readOnBuffer(1) != 0 ) { // long_term_ref_pics_present_flag
                // num_long_term_ref_pics_sps
                for (int i = 0; i < brw.readUnsignedExpGolombCodedInt(); i++) {
                    int ltRefPicPocLsbSpsLength = log2_max_pic_order_cnt_lsb_minus4 + 4;
                    // lt_ref_pic_poc_lsb_sps[i], used_by_curr_pic_lt_sps_flag[i]
                    brw.skipOnBuffer(ltRefPicPocLsbSpsLength + 1);
                }
            }

            brw.skipOnBuffer(2); // sps_temporal_mvp_enabled_flag, strong_intra_smoothing_enabled_flag

            if ( brw.readOnBuffer(1) != 0 ) {
                if ( brw.readOnBuffer(1) != 0 ) {
                    aspect_ratio_idc = (byte) brw.readOnBuffer(8);
                    if ( aspect_ratio_idc == 0xff ) {
                        sar_width = brw.readOnBuffer(16);
                        sar_height = brw.readOnBuffer(16);

                        if ( sar_width != 0 && sar_height != 0 ) {
                            pixel_width_height_ratio = (float) sar_width / sar_height;
                        }
                    } else if ( aspect_ratio_idc < ASPECT_RATIO_IDC_VALUES.length ) {
                        pixel_width_height_ratio = ASPECT_RATIO_IDC_VALUES[aspect_ratio_idc];
                    } else {
                        Logger.w(TAG, "Unexpected aspect_ratio_idc value: " + aspect_ratio_idc);
                    }
                }
            }
        }

        public void print() {
            Logger.d(String.format("sps_max_sub_layers_minus1 : 0x%x \n",
                    sps_max_sub_layers_minus1));
            Logger.d(String.format("chroma_format_idc : 0x%x \n",
                    chroma_format_idc));
            Logger.d(String.format("pic_width_in_luma_samples : 0x%x (%d) \n",
                    pic_width_in_luma_samples, pic_width_in_luma_samples));
            Logger.d(String.format("pic_height_in_luma_samples : 0x%x (%d) \n",
                    pic_height_in_luma_samples, pic_height_in_luma_samples));
            Logger.d(String.format("conf_win_left_offset : 0x%x (%d) \n",
                    conf_win_left_offset, conf_win_left_offset));
            Logger.d(String.format("conf_win_right_offset : 0x%x (%d) \n",
                    conf_win_right_offset, conf_win_right_offset));
            Logger.d(String.format("conf_win_top_offset : 0x%x (%d) \n",
                    conf_win_top_offset, conf_win_top_offset));
            Logger.d(String.format("conf_win_bottom_offset : 0x%x (%d) \n",
                    conf_win_bottom_offset, conf_win_bottom_offset));
            Logger.d(String.format("log2_max_pic_order_cnt_lsb_minus4 : 0x%x \n",
                    log2_max_pic_order_cnt_lsb_minus4));
            Logger.d(String.format("scaling_list_enabled_flag : 0x%x \n",
                    scaling_list_enabled_flag));
            Logger.d(String.format("aspect_ratio_idc : 0x%x \n",
                    aspect_ratio_idc));
            Logger.d(String.format("sar_width : 0x%x (%d) \n",
                    sar_width, sar_width));
            Logger.d(String.format("sar_height : 0x%x (%d) \n",
                    sar_height, sar_height));
            Logger.d(String.format("pixel_width_height_ratio : %f \n",
                    pixel_width_height_ratio));
        }
    }

    public class AUD {
        private final String TAG = AUD.class.getSimpleName();
        public byte pic_type;

        public AUD(BitReadWriter brw) {
            pic_type = (byte) brw.readOnBuffer(3);
        }

        public void print() {
            Logger.d(TAG, String.format("pic_type : %d \n",  pic_type));
        }
    }

    /**
     * Constructor which decode Annex B ByteStream format of ITU-T H.265
     * @param buffer NAL unit's raw buffer not decoded yet.
     */
    public MFU_H265NalUnit(byte[] buffer) {
        super(buffer);

        nal_size = readOnBuffer(32);

        /**
         * nal_unit_header()
         */
        forbidden_zero_bit = (byte) readOnBuffer(1);
        nal_unit_type = (byte) readOnBuffer(6);
        nuh_layer_id = (byte) readOnBuffer(6);
        nuh_temporal_id_plus1 = (byte) readOnBuffer(3);

        switch ( nal_unit_type ) {
            case SPS:
                seq_parameter_set_rbsp = new SPS(this);
                break;
            case AUD:
                access_unit_delimiter_rbsp = new AUD(this);
                break;
            default:
                skipOnBuffer(getCurrentBuffer().length*8);
                break;
        }
    }

    private static void skipScalingList(BitReadWriter brw) {
        for (int sizeId = 0; sizeId < 4; sizeId++) {
            for (int matrixId = 0; matrixId < 6; matrixId += sizeId == 3 ? 3 : 1) {
                if ( 0 == brw.readOnBuffer(1)) { // scaling_list_pred_mode_flag[sizeId][matrixId]
                    // scaling_list_pred_matrix_id_delta[sizeId][matrixId]
                    brw.readUnsignedExpGolombCodedInt();
                } else {
                    int coefNum = Math.min(64, 1 << (4 + (sizeId << 1)));
                    if (sizeId > 1) {
                        // scaling_list_dc_coef_minus8[sizeId - 2][matrixId]
                        brw.readSignedExpGolombCodedInt();
                    }
                    for (int i = 0; i < coefNum; i++) {
                        brw.readSignedExpGolombCodedInt(); // scaling_list_delta_coef
                    }
                }
            }
        }
    }

    private static void skipShortTermRefPicSets(BitReadWriter brw) {
        int numShortTermRefPicSets = brw.readUnsignedExpGolombCodedInt();
        byte interRefPicSetPredictionFlag = 0;
        int numNegativePics;
        int numPositivePics;
        // As this method applies in a SPS, the only element of NumDeltaPocs accessed is the previous
        // one, so we just keep track of that rather than storing the whole array.
        // RefRpsIdx = stRpsIdx - (delta_idx_minus1 + 1) and delta_idx_minus1 is always zero in SPS.
        int previousNumDeltaPocs = 0;
        for (int stRpsIdx = 0; stRpsIdx < numShortTermRefPicSets; stRpsIdx++) {
            if (stRpsIdx != 0) {
                interRefPicSetPredictionFlag = (byte) brw.readOnBuffer(1);
            }
            if ( interRefPicSetPredictionFlag == 1 ) {
                brw.skipOnBuffer(1); // delta_rps_sign
                brw.readUnsignedExpGolombCodedInt(); // abs_delta_rps_minus1
                for (int j = 0; j <= previousNumDeltaPocs; j++) {
                    if (brw.readOnBuffer(1) != 0 ) { // used_by_curr_pic_flag[j]
                        brw.skipOnBuffer(1); // use_delta_flag[j]
                    }
                }
            } else {
                numNegativePics = brw.readUnsignedExpGolombCodedInt();
                numPositivePics = brw.readUnsignedExpGolombCodedInt();
                previousNumDeltaPocs = numNegativePics + numPositivePics;
                for (int i = 0; i < numNegativePics; i++) {
                    brw.readUnsignedExpGolombCodedInt(); // delta_poc_s0_minus1[i]
                    brw.skipOnBuffer(1); // used_by_curr_pic_s0_flag[i]
                }
                for (int i = 0; i < numPositivePics; i++) {
                    brw.readUnsignedExpGolombCodedInt(); // delta_poc_s1_minus1[i]
                    brw.skipOnBuffer(1); // used_by_curr_pic_s1_flag[i]
                }
            }
        }
    }

    public void print() {
        switch ( nal_unit_type ) {
            case SPS:
                Logger.d(String.format("nal_size : 0x%x (%d) \n",  nal_size, nal_size));
                Logger.d(String.format("nal_unit_type : SPS (%d) \n", nal_unit_type));
                seq_parameter_set_rbsp.print();
                break;
            case AUD:
                Logger.d(String.format("nal_unit_type : AUD (%d) \n", nal_unit_type));
            default:
                break;
        }
    }
}
