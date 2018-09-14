package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class MH_StreamIdentifierDescriptor extends Descriptor {
    protected int component_tag;
    
    public MH_StreamIdentifierDescriptor(BitReadWriter brw) {
        super(brw);
        
        component_tag = brw.ReadOnBuffer(16);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }

}
