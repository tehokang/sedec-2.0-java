package arib.b10.tables;

import base.Table;
import util.Logger;

public class TimeDateTable extends Table {
    protected long JST_time;
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

    public long GetJSTTime() {
        return JST_time;
    }
    
    public long GetMJD() {
        return MJD;
    }
    
    public int GetYear() {
        return year;
    }
    
    public int GetMonth() {
        return month;
    }
    
    public int GetDay() {
        return day;
    }
    
    public int GetHour() {
        return hour;
    }
    
    public int GetMinute() {
        return minute;
    }
    
    public int GetSecond() {
        return second;
    }
    
    @Override
    protected void __decode_table_body__() {
        JST_time = ReadOnBuffer(40);
        MJD = 0xffff & (JST_time >> 24);
        year = (int) ((MJD - 15078.2) / 365.25);
        month = (int) (( MJD - 14956.1 ) - (( year * 365.25 ) / 30.6001));
        day = (int) (MJD - 14956 - (int) ( year * 365.25 ) - (int) ( month * 30.6001));
        hour = (int) (0xff & (JST_time >> 16));
        minute = (int) (0xff & (JST_time >> 8));
        second = (int) (0xff & JST_time);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("JST_time : %d/%d/%d %d:%d:%d \n",
                year, month, day, hour, minute, second));
                
    }

}
