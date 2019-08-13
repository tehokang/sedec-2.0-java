package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_HEVCDescriptor extends Descriptor {
    protected byte profile_space;
    protected byte tier_flag;
    protected byte profile_idc;
    protected int profile_compatibility_indication;
    protected byte progressive_source_flag;
    protected byte interlaced_source_flag;
    protected byte non_packed_constraint_flag;
    protected byte frame_only_constraint_flag;
    protected long reserved_zero_44bits;
    protected byte level_idc;
    protected byte temporal_layer_subset_flag;
    protected byte HEVC_still_present_flag;
    protected byte HEVC_24hr_picture_present_flag;
    protected byte sub_pic_hrd_params_not_present_flag;
    protected byte HDR_WCG_idc;
    protected byte temporal_id_min;
    protected byte temporal_id_max;

    public MH_HEVCDescriptor(BitReadWriter brw) {
        super(brw);

        profile_space = (byte) brw.readOnBuffer(2);
        tier_flag = (byte) brw.readOnBuffer(1);
        profile_idc = (byte) brw.readOnBuffer(5);
        profile_compatibility_indication = brw.readOnBuffer(32);
        progressive_source_flag = (byte) brw.readOnBuffer(1);
        interlaced_source_flag = (byte) brw.readOnBuffer(1);
        non_packed_constraint_flag = (byte) brw.readOnBuffer(1);
        frame_only_constraint_flag = (byte) brw.readOnBuffer(1);
        reserved_zero_44bits = brw.readOnBuffer(44);
        level_idc = (byte) brw.readOnBuffer(8);
        temporal_layer_subset_flag = (byte) brw.readOnBuffer(1);
        HEVC_still_present_flag = (byte) brw.readOnBuffer(1);
        HEVC_24hr_picture_present_flag = (byte) brw.readOnBuffer(1);
        sub_pic_hrd_params_not_present_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(2);
        HDR_WCG_idc = (byte) brw.readOnBuffer(2);

        if ( temporal_layer_subset_flag == 1 ) {
            temporal_id_min = (byte) brw.readOnBuffer(3);
            brw.skipOnBuffer(5);
            temporal_id_max = (byte) brw.readOnBuffer(3);
            brw.skipOnBuffer(5);
        }
    }

    public byte getProfileSpace() {
        return profile_space;
    }

    public byte getTierFlag() {
        return tier_flag;
    }

    public byte getProfileIdc() {
        return profile_idc;
    }

    public int getProfileCompatibilityIndication() {
        return profile_compatibility_indication;
    }

    public byte getProgressiveSourceFlag() {
        return progressive_source_flag;
    }

    public byte getInterlacedSourceFlag() {
        return interlaced_source_flag;
    }

    public byte getNonPackedConstraintFlag() {
        return non_packed_constraint_flag;
    }

    public byte getFrameOnlyConstraintFlag() {
        return frame_only_constraint_flag;
    }

    public long getReservedZero44bits() {
        return reserved_zero_44bits;
    }

    public byte getLevelIdc() {
        return level_idc;
    }

    public byte getTemporalLayerSubsetFlag() {
        return temporal_layer_subset_flag;
    }

    public byte getHEVCStillPresentFlag() {
        return HEVC_still_present_flag;
    }

    public byte getHEVC24hrPicturePresentFlag() {
        return HEVC_24hr_picture_present_flag;
    }

    public byte getSubPicHrdParamsNotPresentFlag() {
        return sub_pic_hrd_params_not_present_flag;
    }

    public byte getHDRWCcgIdc() {
        return HDR_WCG_idc;
    }

    public byte getTemporalIdMin() {
        return temporal_id_min;
    }

    public byte getTemporalIdMax() {
        return temporal_id_max;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t profile_space : 0x%x \n", profile_space));
        Logger.d(String.format("\t tier_flag : 0x%x \n", tier_flag));
        Logger.d(String.format("\t profile_idc : 0x%x \n", profile_idc));
        Logger.d(String.format("\t profile_compatibility_indication : 0x%x \n",
                profile_compatibility_indication));
        Logger.d(String.format("\t progressive_source_flag : 0x%x \n",
                progressive_source_flag));
        Logger.d(String.format("\t interlaced_source_flag : 0x%x \n",
                interlaced_source_flag));
        Logger.d(String.format("\t non_packed_constraint_flag : 0x%x \n",
                non_packed_constraint_flag));
        Logger.d(String.format("\t frame_only_constraint_flag : 0x%x \n",
                frame_only_constraint_flag));
        Logger.d(String.format("\t reserved_zero_44bits : 0x%x \n",
                reserved_zero_44bits));
        Logger.d(String.format("\t level_idc : 0x%x \n", level_idc));
        Logger.d(String.format("\t temporal_layer_subset_flag : 0x%x \n",
                temporal_layer_subset_flag));
        Logger.d(String.format("\t HEVC_still_present_flag : 0x%x \n",
                HEVC_still_present_flag));
        Logger.d(String.format("\t HEVC_24hr_picture_present_flag : 0x%x \n",
                HEVC_24hr_picture_present_flag));
        Logger.d(String.format("\t temporal_id_min : 0x%x \n", temporal_id_min));
        Logger.d(String.format("\t temporal_id_max : 0x%x \n", temporal_id_max));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 13;

        if ( temporal_layer_subset_flag == 1 ) {
            descriptor_length += 2;
        }
    }
}
