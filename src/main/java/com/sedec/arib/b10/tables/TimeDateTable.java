package com.sedec.arib.b10.tables;

import com.sedec.base.Table;
import com.sedec.util.JSTTime;
import com.sedec.util.Logger;

public class TimeDateTable extends Table {
    protected long JST_time_bits;
    protected JSTTime JST_time;

    public TimeDateTable(byte[] buffer) {
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

    @Override
    protected void __decode_table_body__() {
        JST_time_bits = readLongOnBuffer(40);
        JST_time = new JSTTime(JST_time_bits);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("JST_time : %d/%d/%d %d:%d:%d \n",
                JST_time.getYear(), JST_time.getMonth(), JST_time.getDay(),
                JST_time.getHour(), JST_time.getMinute(), JST_time.getSecond()));
    }
}
