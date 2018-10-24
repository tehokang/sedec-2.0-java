package sedec2.arib.b10.tables;

import sedec2.base.Table;
import sedec2.util.JSTTime;
import sedec2.util.Logger;

public class TimeDateTable extends Table {
    protected long JST_time_bits;
    protected JSTTime JST_time;
    
    protected long MJD;
    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;
    
    public TimeDateTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public long getJSTTime() {
        return JST_time_bits;
    }
    
    public long getMJD() {
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
        JST_time_bits = readOnBuffer(40);
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
