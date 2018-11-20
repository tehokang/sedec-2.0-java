package sedec2.util;

import java.util.concurrent.TimeUnit;

public class ConsoleProgress {
    protected final int PROGRESS_BAR_WIDTH=30;
    protected long counter = 0;
    protected long startTime = 0;
    protected long processTime = 0;
    protected long read_size = 0;
    protected long read_vector = 0;
    protected double total_size = 0;
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
    
    public ConsoleProgress(String counter_name) {
        this.counter_name = counter_name;
    }
    
    public void start(double file_size) {
        total_size = file_size;
        bitrate_average = 0;
        startTime = System.currentTimeMillis();
        processTime = System.currentTimeMillis();
    }
    
    public void stop() {
        total_size = 0;
        bitrate_average = 0;
        System.out.print("\n");
    }
    
    public ConsoleProgress show(boolean progress_bar, boolean percentage, boolean loading_circle,
            boolean bitrate, boolean duration, boolean amount ) {
        m_enable_progress_bar = progress_bar;
        m_enable_percentage = percentage;
        m_enable_loading_circle = loading_circle;
        m_enable_bitrate = bitrate;
        m_enable_duration = duration;
        m_enable_proceed_amount = amount;
        
        return this;
    }
    
    public void showProgressbar(boolean show) {
        m_enable_progress_bar = show;
    }
    
    public void showPercentage(boolean show) {
        m_enable_percentage = show;
    }
    
    public void showLoadingCircle(boolean show) {
        m_enable_loading_circle = show;
    }
    
    public void showBitrate(boolean show) {
        m_enable_bitrate = show;
    }
    
    public void showDuration(boolean show) {
        m_enable_duration = show;
    }
    
    public void showProceedAmount(boolean show) {
        m_enable_proceed_amount = show;
    }
    
    public void update(long read) {
        counter+=1;
        read_size += read;

        /**
         * @note TLV counter
         */
        System.out.print(String.format(" %s : %d ", counter_name, counter));
        
        /**
         * @note Progress bar as percentage
         */
        if ( m_enable_progress_bar )
            System.out.print(String.format("%s ", 
                    getProgressBar((double)(read_size / total_size) * 100)));

        /**
         * @note Percentage of processing while demuxing
         */
        if ( m_enable_percentage ) 
            System.out.print("\033[1;31m" + String.format("%.2f %% ", 
                    (double)(read_size / total_size) * 100) + "\u001B[0m");
        
        /**
         * @note Circle animation of progressing 
         */
        if ( m_enable_loading_circle )
            System.out.print(anim_circle[(int) (counter%4)] + " "); 
        
        /**
         * @note Bitrate of processing as Mbps
         */
        if ( m_enable_bitrate ) {
            if ( System.currentTimeMillis()-processTime >= 1000 ) {
                bitrate_average = bitrate_average * 8 / 1024 / 1024;
                processTime = System.currentTimeMillis();
                read_vector = 0;
            } else {
                read_vector += read;
            }
            System.out.print(String.format("%4.2f Mbps ", bitrate_average));
        }

        /**
         * @note Total amount of processed
         */
        if ( m_enable_proceed_amount ) 
            System.out.print(String.format("(%.2f / %.2f MBytes) ", 
                    (double)(read_size/1024/1024),
                    (double)(total_size/1024/1024) ));
        
        /**
         * @note Duration time during demuxing
         */
        if ( m_enable_duration )
            System.out.print(String.format("%s ",
                    formatInterval(System.currentTimeMillis()-startTime)));
        
        System.out.print("\r");
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
