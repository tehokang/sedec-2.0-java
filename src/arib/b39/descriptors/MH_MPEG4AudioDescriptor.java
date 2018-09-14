package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class MH_MPEG4AudioDescriptor extends Descriptor {
    protected byte MPEG4_audio_profile_and_level;
    
    public MH_MPEG4AudioDescriptor(BitReadWriter brw) {
        super(brw);
        
        MPEG4_audio_profile_and_level = (byte) brw.ReadOnBuffer(8);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t MPEG4_audio_profile_and_level : 0x%x \n", 
                MPEG4_audio_profile_and_level));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }
}
