package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class AssetGroupDescriptor extends Descriptor {
    protected byte group_identification;
    protected byte selection_level;
    
    public AssetGroupDescriptor(BitReadWriter brw) {
        super(brw);
        
        group_identification = (byte) brw.ReadOnBuffer(8);
        selection_level = (byte) brw.ReadOnBuffer(8);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("group_identification : 0x%x \n", group_identification));
        Logger.d(String.format("selection_level : 0x%x \n", selection_level));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }

}
