package sedec2.util;

public class BinaryLogger {
    public static boolean DEBUG = false;
    
    public static void Print(byte[] buffer) {
        if ( DEBUG == true ) {
            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<buffer.length; k++) {
                Logger.p(String.format("%02x ", buffer[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
            Logger.p("\n");
        }
    }
    
    public static void Debug(byte[] buffer) {
        int j=1;
        Logger.p(String.format("%03d : ", j));
        for(int k=0; k<buffer.length; k++) {
            Logger.p(String.format("%02x ", buffer[k]));
            if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
        Logger.p("\n");
    }
}
