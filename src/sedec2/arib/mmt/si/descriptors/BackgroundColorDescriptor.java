package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class BackgroundColorDescriptor extends Descriptor {
    protected int background_color;
    
    public BackgroundColorDescriptor(BitReadWriter brw) {
        super(brw);
        
        background_color = brw.ReadOnBuffer(24);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t background_color : 0x%x", background_color));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3;
    }

}
