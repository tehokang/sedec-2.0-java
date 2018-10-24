package sedec2.util;

public class JSTTime {
    protected long JST;
    protected long MJD;
    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;
    
    public JSTTime(long time) {
        JST = time;
        
        MJD = 0xffff & (time >> 24);
        year = (int) ((MJD - 15078.2) / 365.25);
        month = (int) (( MJD - 14956.1 ) - (( year * 365.25 ) / 30.6001));
        day = (int) (MJD - 14956 - (int) ( year * 365.25 ) - (int) ( month * 30.6001));
        hour = (int) (0xff & (time >> 16));
        minute = (int) (0xff & (time >> 8));
        second = (int) (0xff & time);
    }
    
    public long getJSTTime() {
        return JST;
    }
    
    public long getMJD() {
        return MJD;
    }
    
    public int getYear() {
        return year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public int getDay() {
        return day;
    }
    
    public int getHour() {
        return hour;
    }
    
    public int getMinute() {
        return minute;
    }
    
    public int getSecond() {
        return second;
    }
}
