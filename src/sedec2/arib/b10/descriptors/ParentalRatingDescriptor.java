package sedec2.arib.b10.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ParentalRatingDescriptor extends Descriptor {
    private byte[] country_code = new byte[3];
    private byte rating;
    private int parent_rating_count;
    
    public ParentalRatingDescriptor(BitReadWriter brw) {
        super(brw);
        
        int j=0;
        for ( int i=descriptor_length; i>0; j++ ) {
            country_code[0] = (byte) brw.ReadOnBuffer(8);
            country_code[1] = (byte) brw.ReadOnBuffer(8);
            country_code[2] = (byte) brw.ReadOnBuffer(8);
            rating = (byte) brw.ReadOnBuffer(8);
            i-=4;
            parent_rating_count = j+1;
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = parent_rating_count * 4;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        for ( int i=0; i<parent_rating_count; i++ ) {
            brw.WriteOnBuffer(country_code[i], 24);
            brw.WriteOnBuffer(rating, 8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        for ( int j=0; j<parent_rating_count; j++ ) {
            Logger.d(String.format("\t country_code[%d] : %s \n", j, new String(country_code)));
            Logger.d(String.format("\t rating[%d] : 0x%x \n", j, rating));
        }
    }

    
}
