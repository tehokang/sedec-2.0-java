package sedec2.arib.b39.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_TargetRegionDescriptor extends Descriptor {
    protected byte region_spec_type;
    protected long prefecture_bitmap;
    
    public MH_TargetRegionDescriptor(BitReadWriter brw) {
        super(brw);
        
        region_spec_type = (byte) brw.ReadOnBuffer(8);
        
        if ( region_spec_type == 0x01 ) {
            prefecture_bitmap = brw.ReadOnBuffer(56);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t region_spec_type : 0x%x \n", region_spec_type));
        
        if ( region_spec_type == 0x01 ) {
            Logger.d(String.format("\t prefecture_bitmap : 0x%x \n", prefecture_bitmap));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + ( region_spec_type == 0x01 ? 7 : 0 );
    }

}