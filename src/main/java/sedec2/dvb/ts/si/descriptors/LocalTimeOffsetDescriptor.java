package sedec2.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class LocalTimeOffsetDescriptor extends Descriptor {
    protected List<LocalTimeOffset> local_time_offsets = new ArrayList<>();

    public class LocalTimeOffset {
        public int country_code;
        public byte country_region_id;
        public byte local_time_offset_polarity;
        public int local_time_offset;
        public int time_of_change;
        public int next_time_offset;
    }

    public LocalTimeOffsetDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            LocalTimeOffset offset = new LocalTimeOffset();
            offset.country_code = brw.readOnBuffer(24);
            offset.country_region_id = (byte) brw.readOnBuffer(6);
            brw.skipOnBuffer(1);
            offset.local_time_offset_polarity = (byte) brw.readOnBuffer(1);
            offset.local_time_offset = brw.readOnBuffer(16);
            offset.time_of_change = brw.readOnBuffer(40);
            offset.next_time_offset = brw.readOnBuffer(16);
            i-=13;

            local_time_offsets.add(offset);
        }
    }

    public List<LocalTimeOffset> getLocalTimeOffsets() {
        return local_time_offsets;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<local_time_offsets.size(); i++ ) {
            LocalTimeOffset offset = local_time_offsets.get(i);
            Logger.d(String.format("\t [%d] country_code : 0x%x \n",
                    i, offset.country_code));
            Logger.d(String.format("\t [%d] country_region_id : 0x%x \n",
                    i, offset.country_region_id));
            Logger.d(String.format("\t [%d] local_time_offset_polarity : 0x%x \n",
                    i, offset.local_time_offset_polarity));
            Logger.d(String.format("\t [%d] local_time_offset : 0x%x \n",
                    i, offset.local_time_offset));
            Logger.d(String.format("\t [%d] time_of_change : 0x%x \n",
                    i, offset.time_of_change));
            Logger.d(String.format("\t [%d] next_time_offset : 0x%x \n",
                    i, offset.next_time_offset));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = local_time_offsets.size()*13;
    }
}
