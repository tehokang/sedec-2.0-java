package sedec2.util;

/**
 * Class to print byte buffer as WYSIWYG
 */
public class BinaryLogger {
    protected static final String TAG = BinaryLogger.class.getSimpleName();

    /**
     * A flag which can show result of {@link BinaryLogger#print(byte[])}
     */
    public static boolean PRINT = false;

    /**
     * A flag which can show result of {@link BinaryLogger#debug(byte[])}
     */
    public static boolean DEBUG = true;

    /**
     * Prints buffer from beginning to end
     * @param buffer to print
     */
    public static void print(byte[] buffer) {
        if ( PRINT == true ) {
            Logger.p(TAG, String.format("-------- %s:print -------- \n",
                    BinaryLogger.class.getSimpleName()));

            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<buffer.length; k++) {
                Logger.p(String.format("%02x ", buffer[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
            Logger.p(TAG, "\n------------------------------------\n");
        }
    }

    /**
     * Prints buffer from beginning to length of parameter
     * @param buffer to print
     * @param length to print by length
     */
    public static void print(byte[] buffer, int length) {
        if ( PRINT == true ) {
            Logger.p(TAG, String.format("-------- %s:debug -------- \n",
                    BinaryLogger.class.getSimpleName()));

            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<buffer.length; k++) {
                if ( k >= length ) break;
                Logger.p(String.format("%02x ", buffer[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
            Logger.p(TAG, "\n------------------------------------\n");
        }
    }

    /**
     * Prints buffer from beginning to end
     * @param buffer to print
     */
    public static void debug(byte[] buffer) {
        if ( DEBUG == true ) {
            Logger.p(TAG, String.format("-------- %s:debug -------- \n",
                    BinaryLogger.class.getSimpleName()));

            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<buffer.length; k++) {
                Logger.p(String.format("%02x ", buffer[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
            Logger.p(TAG, "\n------------------------------------\n");
        }
    }

    /**
     * Prints buffer from beginning to length of parameter
     * @param buffer to print
     * @param length to print by length
     */
    public static void debug(byte[] buffer, int length) {
        if ( DEBUG == true ) {
            Logger.p(TAG, String.format("-------- %s:debug -------- \n",
                    BinaryLogger.class.getSimpleName()));

            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<buffer.length; k++) {
                if ( k >= length ) break;
                Logger.p(String.format("%02x ", buffer[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
            Logger.p(TAG, "\n------------------------------------\n");
        }
    }
}
