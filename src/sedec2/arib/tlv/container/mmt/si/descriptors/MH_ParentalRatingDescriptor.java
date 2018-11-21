package sedec2.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ParentalRatingDescriptor extends Descriptor {
    List<Rating> ratings = new ArrayList<>();

    public class Rating {
        public int country_code;
        public byte rating;
    }

    public MH_ParentalRatingDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            Rating rating = new Rating();
            rating.country_code = brw.readOnBuffer(24);
            rating.rating = (byte) brw.readOnBuffer(8);
            ratings.add(rating);
            i-=4;
        }
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<ratings.size(); i++ ) {
            Logger.d(String.format("\t [%d] country_code : 0x%x \n",
                    i, ratings.get(i).country_code));
            Logger.d(String.format("\t [%d] rating : 0x%x \n",
                    i, ratings.get(i).rating));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = ratings.size() * 4;
    }
}
