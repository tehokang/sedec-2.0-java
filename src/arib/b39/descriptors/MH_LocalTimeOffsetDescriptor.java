package arib.b39.descriptors;

import java.util.List;

import base.BitReadWriter;
import util.Logger;

public class MH_LocalTimeOffsetDescriptor extends Descriptor {
    protected List<LocalTime> localtimes;
    
    class LocalTime {
        public int country_code;
        public byte country_region_id;
        public byte local_time_offset_polarity;
        public int local_time_offset;
        public int time_of_change;
        public int next_time_offset;
    }
    
    public MH_LocalTimeOffsetDescriptor(BitReadWriter brw) {
        super(brw);
        
        for ( int i=descriptor_length; i>0; ) {
            LocalTime localtime = new LocalTime();
            localtime.country_code = brw.ReadOnBuffer(24);
            localtime.country_region_id = (byte) brw.ReadOnBuffer(6);
            brw.SkipOnBuffer(1);
            localtime.local_time_offset_polarity = (byte) brw.ReadOnBuffer(1);
            localtime.local_time_offset = brw.ReadOnBuffer(16);
            localtime.time_of_change = brw.ReadOnBuffer(40);
            localtime.next_time_offset = brw.ReadOnBuffer(16);
            i-=13;
            
            localtimes.add(localtime);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        for ( int i=0; i<localtimes.size(); i++ ) {
            LocalTime localtime = localtimes.get(i);
            
            Logger.d(String.format("\t [%d] country_code : 0x%x \n", 
                    i, localtime.country_code));
            Logger.d(String.format("\t [%d] country_region_id : 0x%x \n",
                    i, localtime.country_region_id));
            Logger.d(String.format("\t [%d] local_time_offset_polarity : 0x%x \n",
                    i, localtime.local_time_offset_polarity));
            Logger.d(String.format("\t [%d] local_time_offset : 0x%x \n",
                    i, localtime.local_time_offset));
            Logger.d(String.format("\t [%d] time_of_change : 0x%x \n",
                    i, localtime.time_of_change));
            Logger.d(String.format("\t [%d] next_time_offset : 0x%x \n",
                    i, localtime.next_time_offset));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length += (localtimes.size() * 13);
    }
}
