package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class MH_MPEG4AudioExtensionDescriptor extends Descriptor {
    protected byte ASC_flag;
    protected byte num_of_loops;
    protected byte[] audioProfileLevelIndication;
    protected byte ASC_size;
    protected AudioSpecificConfig audioSpecificConfig;
    
    public MH_MPEG4AudioExtensionDescriptor(BitReadWriter brw) {
        super(brw);
        
        ASC_flag = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(3);
        num_of_loops = (byte) brw.ReadOnBuffer(4);
        
        audioProfileLevelIndication = new byte[num_of_loops];
        for ( int i=0; i<num_of_loops; i++ ) {
            audioProfileLevelIndication[i] = (byte) brw.ReadOnBuffer(8);
        }
        
        if ( ASC_flag == 1 ) {
            ASC_size = (byte) brw.ReadOnBuffer(8);
            audioSpecificConfig = new AudioSpecificConfig(brw);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t ASC_flag : 0x%x \n", ASC_flag));
        Logger.d(String.format("\t num_of_loops : 0x%x \n", num_of_loops));
        for ( int i=0; i<num_of_loops; i++ ) {
            Logger.d(String.format("\t [%d] audioProfileLevelIndication : 0x%x \n", 
                    i, audioProfileLevelIndication[i]));
        }
        
        if ( ASC_flag == 1 ) {
            Logger.d(String.format("\t ASC_size : 0x%x \n", ASC_size));
            audioSpecificConfig.PrintInfo();
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + audioProfileLevelIndication.length;
        if ( ASC_flag == 1 ) {
            descriptor_length += (1 + audioSpecificConfig.GetLength());
        }
    }
}
