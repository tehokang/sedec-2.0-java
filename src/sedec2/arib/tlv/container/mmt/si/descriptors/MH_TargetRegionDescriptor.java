package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_TargetRegionDescriptor extends Descriptor {
    protected byte region_spec_type;
    protected long prefecture_bitmap;
    
    public MH_TargetRegionDescriptor(BitReadWriter brw) {
        super(brw);
        
        region_spec_type = (byte) brw.readOnBuffer(8);
        
        if ( region_spec_type == 0x01 ) {
            prefecture_bitmap = brw.readOnBuffer(56);
        }
    }

    public byte getRegionSpecType() {
        return region_spec_type;
    }
    
    public long getPrefectureBitmap() {
        return prefecture_bitmap;
    }

    @Override
    public void print() {
        super._print_();
        
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
