package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class BackgroundColorDescriptor extends Descriptor {
    protected int background_color;
    
    public BackgroundColorDescriptor(BitReadWriter brw) {
        super(brw);
        
        background_color = brw.readOnBuffer(24);
    }

    public int getBackgroundColor() {
        return background_color;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t background_color : 0x%x", background_color));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3;
    }

}
