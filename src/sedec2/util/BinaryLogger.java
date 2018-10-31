package sedec2.util;

public class BinaryLogger {
    public static boolean PRINT = false;
    public static boolean DEBUG = true;
    
    public static void print(byte[] buffer) {
        if ( PRINT == true ) {
            Logger.p(String.format("-------- %s:print -------- \n", 
                    BinaryLogger.class.getSimpleName()));

            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<buffer.length; k++) {
                Logger.p(String.format("%02x ", buffer[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
            Logger.p("\n------------------------------------\n");
        }
    }
    
    public static void debug(byte[] buffer) {
        if ( DEBUG == true ) {
            Logger.p(String.format("-------- %s:debug -------- \n", 
                    BinaryLogger.class.getSimpleName()));

            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<buffer.length; k++) {
                Logger.p(String.format("%02x ", buffer[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
            Logger.p("\n------------------------------------\n");
        }
    }
    
    public static void debug(byte[] buffer, int length) {
        if ( DEBUG == true ) {
            Logger.p(String.format("-------- %s:debug -------- \n", 
                    BinaryLogger.class.getSimpleName()));

            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<buffer.length; k++) {
                if ( k >= length ) break;
                Logger.p(String.format("%02x ", buffer[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
            Logger.p("\n------------------------------------\n");
        }
    }
}
