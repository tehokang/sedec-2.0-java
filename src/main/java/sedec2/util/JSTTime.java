package sedec2.util;

/**
 * Wrapper class to convert long bits to JST time
 */
public class JSTTime {
    protected long JST;
    protected double MJD;
    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;

    /**
     * Constructor with 40 bits-aligned JST as known as Japan Standard Time
     * @param time JST 40bits
     */
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

    /**
     * Gets original 40 bits-aligned JST
     * @return
     */
    public long getJSTTime() {
        return JST;
    }

    /**
     * Gets MJD as known as Modified Julian Day
     * @return MDJ
     */
    public double getMJD() {
        return MJD;
    }

    /**
     * Gets year from JST
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets month from JST
     * @return month
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets day from JST
     * @return day
     */
    public int getDay() {
        return day;
    }

    /**
     * Gets hour from JST
     * @return hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Gets minute from JST
     * @return minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Gets second from JST
     * @return second
     */
    public int getSecond() {
        return second;
    }
}
