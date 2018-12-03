package sedec2.dvb.ts.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Table;
import sedec2.dvb.ts.si.DescriptorFactory;
import sedec2.dvb.ts.si.descriptors.Descriptor;
import sedec2.util.Logger;
import sedec2.util.UTCTime;

public class TimeOffsetTable extends Table {
    protected long UTC_time_bits;
    protected UTCTime UTC_time;

    protected int descriptors_loop_length;
    protected List<Descriptor> descriptors = new ArrayList<>();

    public TimeOffsetTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public long getJSTTime() {
        return UTC_time.getJSTTime();
    }

    public double getMJD() {
        return UTC_time.getMJD();
    }

    public int getYear() {
        return UTC_time.getYear();
    }

    public int getMonth() {
        return UTC_time.getMonth();
    }

    public int getDay() {
        return UTC_time.getDay();
    }

    public int getHour() {
        return UTC_time.getHour();
    }

    public int getMinute() {
        return UTC_time.getMinute();
    }

    public int getSecond() {
        return UTC_time.getSecond();
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    protected void __decode_table_body__() {
        UTC_time_bits = readOnBuffer(40);
        UTC_time = new UTCTime(UTC_time_bits);

        skipOnBuffer(4);
        descriptors_loop_length = readOnBuffer(12);

        for ( int i=descriptors_loop_length; i>0; ) {
            Descriptor desc = DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }

        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("UTC_time : %d/%d/%d %d:%d:%d \n",
                UTC_time.getYear(), UTC_time.getMonth(), UTC_time.getDay(),
                UTC_time.getHour(), UTC_time.getMinute(), UTC_time.getSecond()));

        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }

        Logger.d(String.format("checksum_CRC32 : 0x%x%x%x%x \n",
                ((checksum_CRC32 >> 24) & 0xff),
                ((checksum_CRC32 >> 16) & 0xff),
                ((checksum_CRC32 >> 8) & 0xff),
                (checksum_CRC32 & 0xff)));
    }

}
