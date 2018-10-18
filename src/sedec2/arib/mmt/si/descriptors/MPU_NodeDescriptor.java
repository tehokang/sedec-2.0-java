package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MPU_NodeDescriptor extends Descriptor {
    protected int node_tag;
    
    public MPU_NodeDescriptor(BitReadWriter brw) {
        super(brw);
        
        node_tag = brw.ReadOnBuffer(16);
    }
    
    public int GetNodeTag() {
        return node_tag;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("node_tag : 0x%x \n", node_tag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }
}
