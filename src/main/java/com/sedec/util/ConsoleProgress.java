package com.sedec.util;

import java.util.concurrent.TimeUnit;

/**
 * Class to decorate progress of processing with progress bar, bitrate and more things.
 */
public class ConsoleProgress {
    protected static final String TAG = ConsoleProgress.class.getSimpleName();

    protected final int PROGRESS_BAR_WIDTH=30;
    protected long counter = 0;
    protected long startTime = 0;
    protected long bitrate_process_time = 0;
    protected long memory_process_time = 0;
    protected double read_size = 0;
    protected double read_vector = 0;
    protected double total_size = 0;
    protected String output = "";
    protected String counter_name = "";
    protected double bitrate_average = 0;
    protected StringBuilder anim_progress_bar;
    protected char[] anim_circle = new char[]{'|', '/', '-', '\\'};

    protected boolean m_enable_progress_bar = false;
    protected boolean m_enable_percentage = false;
    protected boolean m_enable_loading_circle = false;
    protected boolean m_enable_bitrate = false;
    protected boolean m_enable_duration = false;
    protected boolean m_enable_proceed_amount = false;

    /**
     * Constructor with title of progress
     * @param counter_name shown as first column
     */
    public ConsoleProgress(String counter_name) {
        this.counter_name = counter_name;
    }

    /**
     * Sets whole length of progress
     * @param total_size of progress
     */
    public void start(double total_size) {
        this.counter = 0;
        this.startTime = 0;
        this.bitrate_process_time = 0;
        this.read_size = 0;
        this.read_vector = 0;
        this.bitrate_average = 0;
        this.total_size = total_size;
        this.startTime = System.currentTimeMillis();
        this.bitrate_process_time = System.currentTimeMillis();
        this.memory_process_time = System.currentTimeMillis();
    }

    /**
     * Stop progress
     */
    public void stop() {
        System.out.print("\n");
    }

    /**
     * Shows only progress bar during processing
     * @param progress_bar flag to enable progress_bar
     * @return instance of ConsoleProgress
     */
    public ConsoleProgress show(boolean progress_bar) {
        m_enable_progress_bar = progress_bar;
        return this;
    }

    /**
     * Shows progress bar and percentage during processing
     * @param progress_bar flag to enable progress_bar
     * @param percentage flag to enable percentage
     * @return instance of ConsoleProgress
     */
    public ConsoleProgress show(boolean progress_bar, boolean percentage) {
        this.show(progress_bar);
        m_enable_percentage = percentage;
        return this;
    }

    /**
     * Shows progress bar, percentage and bitrate during processing
     * @param progress_bar flag to enable progress_bar
     * @param percentage flag to enable percentage
     * @param bitrate flag to enable bitrate
     * @return instance of ConsoleProgress
     */
    public ConsoleProgress show(boolean progress_bar, boolean percentage, boolean bitrate) {
        this.show(progress_bar, percentage);
        m_enable_bitrate = bitrate;
        return this;
    }

    /**
     * Shows progress bar, percentage, bitrate and duration during processing
     * @param progress_bar flag to enable progress bar
     * @param percentage flag to enable percentage
     * @param bitrate flag to enable bitrate
     * @param duration flag to enable duration
     * @return instance of ConsoleProgress
     */
    public ConsoleProgress show(boolean progress_bar, boolean percentage, boolean bitrate,
            boolean duration) {
        this.show(progress_bar, percentage, bitrate);
        m_enable_duration = duration;
        return this;
    }

    /**
     * Show progress bar, percentage, bitrate, duration, and amount during processing
     * @param progress_bar flag to enable progress bar
     * @param percentage flag to enable percentage
     * @param bitrate flag to enable bitrate
     * @param duration flag to enable duration
     * @param amount flag to enable amount to be proceed
     * @return instance of ConsoleProgress
     */
    public ConsoleProgress show(boolean progress_bar, boolean percentage, boolean bitrate,
            boolean duration, boolean amount) {
        this.show(progress_bar, percentage, bitrate, duration);
        m_enable_proceed_amount = amount;
        return this;
    }

    /**
     * Show progress bar, percentage, bitrate, duration, amount, heap usage, cpu usage
     * and loading circle during processing
     * @param progress_bar flag to enable progress bar
     * @param percentage flag to enable percentage
     * @param loading_circle flag to enable loading circle
     * @param bitrate flag to enable bitrate
     * @param duration flag to enable duration
     * @param amount flag to enable amount to be proceed
     * @param heap_usage flag to enable heap usage
     * @param cpu_usage flag to enable cpu usage
     * @return instance of ConsoleProgress
     */
    public ConsoleProgress show(boolean progress_bar, boolean percentage,
            boolean loading_circle, boolean bitrate, boolean duration, boolean amount ) {
        this.show(progress_bar, percentage, bitrate, duration, amount);
        m_enable_loading_circle = loading_circle;
        return this;
    }

    /**
     * Updates progress as read
     * @param read size of read
     */
    public void update(long read) {
        counter+=1;
        read_size += read;

        /**
         * counter
         */
        output = String.format(" %s : %d ", counter_name, counter);

        /**
         * Progress bar as percentage
         */
        if ( m_enable_progress_bar )
            output += String.format("%s ",
                    getProgressBar(read_size / total_size * 100));

        /**
         * Percentage of processing while demuxing
         */
        if ( m_enable_percentage )
            output += "\033[1;31m" + String.format("%.2f %% ",
                    read_size / total_size * 100) + "\u001B[0m";

        /**
         * Circle animation of progressing
         */
        if ( m_enable_loading_circle )
            output += anim_circle[(int) (counter%4)] + " ";

        /**
         * Bitrate of processing as Mbps
         */
        if ( m_enable_bitrate ) {
            if ( System.currentTimeMillis() - bitrate_process_time >= 1000 ) {
                bitrate_average = read_vector*8/1024/1024;
                bitrate_process_time = System.currentTimeMillis();
                read_vector = 0;
            } else {
                read_vector += read;
            }
            output += String.format("%4.2f Mbps ", bitrate_average);
        }

        /**
         * Total amount of processed
         */
        if ( m_enable_proceed_amount )
            output += String.format("(%.2f / %.2f MBytes) ",
                    read_size/1024/1024, total_size/1024/1024 );

        /**
         * Duration time during demuxing
         */
        if ( m_enable_duration )
            output += String.format("%s ",
                    formatInterval(System.currentTimeMillis()-startTime));

        System.out.print(output + "\r");
    }

    protected String formatInterval(final long l) {
        final long hr = TimeUnit.MILLISECONDS.toHours(l);
        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
        final long sec = TimeUnit.MILLISECONDS.toSeconds(l -
                TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
        final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) -
                TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
        return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
    }

    protected String getProgressBar(double percent) {
        int bars = (int)(percent * PROGRESS_BAR_WIDTH / 100);
        anim_progress_bar = new StringBuilder(PROGRESS_BAR_WIDTH);
        anim_progress_bar.append("[");
        for ( int i=0; i<bars; i++ ) {
            anim_progress_bar.append("=");
        }
        for ( int i=PROGRESS_BAR_WIDTH-bars; i>0; i-- ) {
            anim_progress_bar.append(" ");
        }
        anim_progress_bar.append("]");
        return anim_progress_bar.toString();
    }
}
