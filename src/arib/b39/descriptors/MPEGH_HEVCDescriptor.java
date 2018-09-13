package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class MPEGH_HEVCDescriptor extends Descriptor {
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
    protected byte temporal_id_min;
    protected byte temporal_id_max;
    
    public MPEGH_HEVCDescriptor(BitReadWriter brw) {
        super(brw);
        
        profile_space = (byte) brw.ReadOnBuffer(2);
        tier_flag = (byte) brw.ReadOnBuffer(1);
        profile_idc = (byte) brw.ReadOnBuffer(5);
        profile_compatibility_indication = brw.ReadOnBuffer(32);
        progressive_source_flag = (byte) brw.ReadOnBuffer(1);
        interlaced_source_flag = (byte) brw.ReadOnBuffer(1);
        non_packed_constraint_flag = (byte) brw.ReadOnBuffer(1);
        frame_only_constraint_flag = (byte) brw.ReadOnBuffer(1);
        reserved_zero_44bits = brw.ReadOnBuffer(44);
        level_idc = (byte) brw.ReadOnBuffer(8);
        temporal_layer_subset_flag = (byte) brw.ReadOnBuffer(1);
        HEVC_still_present_flag = (byte) brw.ReadOnBuffer(1);
        HEVC_24hr_picture_present_flag = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(5);
        
        if ( temporal_layer_subset_flag == 1 ) {
            brw.SkipOnBuffer(5);
            temporal_id_min = (byte) brw.ReadOnBuffer(3);
            brw.SkipOnBuffer(5);
            temporal_id_max = (byte) brw.ReadOnBuffer(3);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
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