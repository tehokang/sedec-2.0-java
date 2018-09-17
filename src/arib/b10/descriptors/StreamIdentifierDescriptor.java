package arib.b10.descriptors;

import base.BitReadWriter;
import util.Logger;

public class StreamIdentifierDescriptor extends Descriptor {
    protected byte component_tag;
    
    public StreamIdentifierDescriptor(BitReadWriter brw) {
        super(brw);
        
        component_tag = (byte) brw.ReadOnBuffer(8);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }

}
