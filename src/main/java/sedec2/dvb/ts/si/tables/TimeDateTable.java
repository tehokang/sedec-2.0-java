package sedec2.dvb.ts.si.tables;

import sedec2.base.Table;
import sedec2.util.Logger;
import sedec2.util.UTCTime;

public class TimeDateTable extends Table {
    protected long UTC_time_bits;
    protected UTCTime UTC_time;

    public TimeDateTable(byte[] buffer) {
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

    @Override
    protected void __decode_table_body__() {
        UTC_time_bits = readOnBuffer(40);
        UTC_time = new UTCTime(UTC_time_bits);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("UTC_time : %d/%d/%d %d:%d:%d \n",
                UTC_time.getYear(), UTC_time.getMonth(), UTC_time.getDay(),
                UTC_time.getHour(), UTC_time.getMinute(), UTC_time.getSecond()));

    }

}
