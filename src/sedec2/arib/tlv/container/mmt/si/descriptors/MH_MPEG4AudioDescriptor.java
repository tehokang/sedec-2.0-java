package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_MPEG4AudioDescriptor extends Descriptor {
    protected byte MPEG4_audio_profile_and_level;
    
    public MH_MPEG4AudioDescriptor(BitReadWriter brw) {
        super(brw);
        
        MPEG4_audio_profile_and_level = (byte) brw.readOnBuffer(8);
    }

    public byte getMPEG4AudioProfileAndLevel() {
        return MPEG4_audio_profile_and_level;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t MPEG4_audio_profile_and_level : 0x%x \n", 
                MPEG4_audio_profile_and_level));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }
}
