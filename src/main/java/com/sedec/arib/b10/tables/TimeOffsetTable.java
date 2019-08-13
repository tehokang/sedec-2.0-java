package com.sedec.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.b10.DescriptorFactory;
import com.sedec.arib.b10.descriptors.Descriptor;
import com.sedec.base.Table;
import com.sedec.util.JSTTime;
import com.sedec.util.Logger;

public class TimeOffsetTable extends Table {
    protected long JST_time_bits;
    protected JSTTime JST_time;

    protected int descriptors_loop_length;
    protected List<Descriptor> descriptors = new ArrayList<>();

    public TimeOffsetTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public long getJSTTime() {
        return JST_time_bits;
    }

    public double getMJD() {
        return JST_time.getMJD();
    }

    public int getYear() {
        return JST_time.getYear();
    }

    public int getMonth() {
        return JST_time.getMonth();
    }

    public int getDay() {
        return JST_time.getDay();
    }

    public int getHour() {
        return JST_time.getHour();
    }

    public int getMinute() {
        return JST_time.getMinute();
    }

    public int getSecond() {
        return JST_time.getSecond();
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    protected void __decode_table_body__() {
        JST_time_bits = readLongOnBuffer(40);
        JST_time = new JSTTime(JST_time_bits);

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

        Logger.d(String.format("JST_time : %d/%d/%d %d:%d:%d \n",
                JST_time.getYear(), JST_time.getMonth(), JST_time.getDay(),
                JST_time.getHour(), JST_time.getMinute(), JST_time.getSecond()));

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
