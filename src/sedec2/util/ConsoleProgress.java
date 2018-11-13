package sedec2.util;

import java.util.concurrent.TimeUnit;

public class ConsoleProgress {
    protected final int PROGRESS_BAR_WIDTH=30;
    protected long counter = 0;
    protected long startTime = 0;
    protected long processTime = 0;
    protected double read_size = 0;
    protected double total_size = 0;
    protected double bitrate_average = 0;
    protected StringBuilder anim_progress_bar;
    protected char[] anim_circle = new char[]{'|', '/', '-', '\\'};
    
    public void start(double file_size) {
        total_size = file_size;
        startTime = System.currentTimeMillis();
        processTime = System.currentTimeMillis();
    }
    
    public void stop() {
        total_size = 0;
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
    
    public void update(int read) {
        counter+=1;
        read_size += read;

        /**
         * @note TLV counter
         */
        System.out.print(String.format(" TLV : %d ", counter));
        
        /**
         * @note Progress bar as percentage
         */
        System.out.print(String.format("%s ", 
                getProgressBar((double)(read_size / total_size) * 100)));

        /**
         * @note Percentage of processing while demuxing
         */
        System.out.print("\033[1;31m" + 
                String.format("%.2f %% ", (double)(read_size / total_size) * 100) + "\u001B[0m");
        
        /**
         * @note Circle animation of progressing 
         */
        System.out.print(anim_circle[(int) (counter%4)] + " "); 
        
        /**
         * @note Bitrate of processing as Mbps
         */
        bitrate_average += (((double) (1000 * read ) / 
                (double)((System.currentTimeMillis()-processTime) )) * 
                8) / 1024 / 1024;
        System.out.print(String.format("%4.2fMbps ", bitrate_average/counter)); 
        processTime = System.currentTimeMillis();
        
        /**
         * @note Total amount of processed
         */
        System.out.print(String.format("(%.2f / %.2f MBytes) ", 
                (double)(read_size/1024/1024),
                (double) total_size/1024/1024));
        
        /**
         * @note Duration time during demuxing
         */
        System.out.print(String.format("%s \r", 
                formatInterval(System.currentTimeMillis()-startTime)));
    }
}
