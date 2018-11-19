package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_StreamIdentifierDescriptor extends Descriptor {
    protected int component_tag;
    
    public MH_StreamIdentifierDescriptor(BitReadWriter brw) {
        super(brw);
        
        component_tag = brw.readOnBuffer(16);
    }

    public int getComponentTag() {
        return component_tag;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }
}
