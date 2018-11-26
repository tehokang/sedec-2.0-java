package sedec2.arib.tlv.container.mmt.si.descriptors;

import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_LocalTimeOffsetDescriptor extends Descriptor {
    protected List<LocalTime> localtimes;

    public class LocalTime {
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
            localtime.country_code = brw.readOnBuffer(24);
            localtime.country_region_id = (byte) brw.readOnBuffer(6);
            brw.skipOnBuffer(1);
            localtime.local_time_offset_polarity = (byte) brw.readOnBuffer(1);
            localtime.local_time_offset = brw.readOnBuffer(16);
            localtime.time_of_change = brw.readOnBuffer(40);
            localtime.next_time_offset = brw.readOnBuffer(16);
            i-=13;

            localtimes.add(localtime);
        }
    }

    public List<LocalTime> getLocalTimes() {
        return localtimes;
    }

    @Override
    public void print() {
        super._print_();

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
        descriptor_length = 0;
        descriptor_length += (localtimes.size() * 13);
    }
}
