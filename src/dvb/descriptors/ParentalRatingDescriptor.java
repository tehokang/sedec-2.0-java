package dvb.descriptors;

import base.BitReadWriter;
import util.Logger;

public class ParentalRatingDescriptor extends Descriptor {
    private int[] country_code = new int[64];
    private byte[] rating = new byte[64];
    private int parent_rating_count;
    
    public ParentalRatingDescriptor(BitReadWriter brw) {
        super(brw);
        
        int j=0;
        for ( int i=descriptor_length; i>0; j++ ) {
            country_code[j] = brw.ReadOnBuffer(24);
            rating[j] = (byte) brw.ReadOnBuffer(8);
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
            brw.WriteOnBuffer(rating[i], 8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptor_("ParentalRatingDescriptor");
        
        for ( int j=0; j<parent_rating_count; j++ ) {
            Logger.d("\t country_code[" + j + " : " + country_code[j] + "\n");
            Logger.d("\t rating[" + j + " : " + rating[j] + "\n");
        }
    }

    
}
