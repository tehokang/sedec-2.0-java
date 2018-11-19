package sedec2.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MPU_PresentationRegionDescriptor extends Descriptor {
    protected List<PresentationRegion> presentation_regions = new ArrayList<>();
    
    public class PresentationRegion {
        public int mpu_sequence_number;
        public byte layout_number;
        public byte region_number;
        public byte length_of_reserved;
    }
    
    public MPU_PresentationRegionDescriptor(BitReadWriter brw) {
        super(brw);
        
        for ( int i=descriptor_length; i>0; ) {
            PresentationRegion region = new PresentationRegion();
            region.mpu_sequence_number = brw.readOnBuffer(32);
            region.layout_number = (byte) brw.readOnBuffer(8);
            region.region_number = (byte) brw.readOnBuffer(8);
            region.length_of_reserved = (byte) brw.readOnBuffer(8);
            
            for ( int j=0; j<region.length_of_reserved; j++ ) {
                brw.skipOnBuffer(8);
            }
            
            presentation_regions.add(region);
        }
    }

    public List<PresentationRegion> getPresentationRegions() {
        return presentation_regions ;
    }
    
    @Override
    public void print() {
        super._print_();
        
        for ( int i=0; i<presentation_regions.size(); i++ ) {
            PresentationRegion region = presentation_regions.get(i);
            Logger.d(String.format("\t [%d] mpu_sequence_number : 0x%x \n", 
                    i, region.mpu_sequence_number));
            Logger.d(String.format("\t [%d] layout_number : 0x%x \n", 
                    i, region.layout_number));
            Logger.d(String.format("\t [%d] region_number : 0x%x \n", 
                    i, region.region_number));
            Logger.d(String.format("\t [%d] length_of_reserved : 0x%x \n", 
                    i, region.length_of_reserved));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 0;
        for ( int i=0; i<presentation_regions.size(); i++ ) {
            descriptor_length += (7 + presentation_regions.get(i).length_of_reserved); 
        }
    }
}
