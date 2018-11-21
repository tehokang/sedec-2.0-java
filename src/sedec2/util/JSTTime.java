package sedec2.util;

public class JSTTime {
    protected long JST;
    protected double MJD;
    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;

    public JSTTime(long time) {
        JST = time;

        MJD = 0xffff & (time >> 24);

        year = (int) (( MJD - 15078.2) / 365.25 );
        month = (int) (( MJD - 14956.1 - (int)( year * 365.25 )) / 30.6001);
        day = (int) ( MJD - 14956 - (int)( year * 365.25 ) - (int)( month * 30.6001) );

        int k=0;
        if ( month == 14 || month == 15 ) k = 1;

        year = year + k + 1900;
        month = month - 1 - k * 12;

        String hour_s = Integer.toHexString(0xff & (byte)(time >> 16));
        String minute_s = Integer.toHexString(0xff & (byte)(time >> 8));
        String second_s = Integer.toHexString(0xff & (byte)time);

        hour = Integer.parseInt(hour_s);
        minute = Integer.parseInt(minute_s);
        second = Integer.parseInt(second_s);
    }

    public long getJSTTime() {
        return JST;
    }

    public double getMJD() {
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
