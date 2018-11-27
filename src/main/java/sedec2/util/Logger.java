package sedec2.util;

/**
 * Class to print logs
 */
public class Logger
{
    public static void p(String msg) {
        if ( DEBUG ) {
            System.out.print(msg);
        }
    }

    public static void d(String tag, String msg) {
        if (false == tag.isEmpty() && DEBUG ) {
            System.out.print(m_tag + "[" + tag + "]" + "[D] " +  msg);
        }
    }

    public static void d(String msg) {
        /* Pure Java */
        if ( DEBUG ) System.out.print(m_tag + "[D] " + msg);
        /* Android */
        //if(DEBUG) Log.d(tag, "[D]" + msg);
    }

    public static void i(String tag, String msg) {
        if (false == tag.isEmpty() && INFO ) {
            System.out.print(m_tag + "[" + tag + "]" + "[I] " +  msg);
        }
    }

    public static void i(String msg) {
        /* Pure Java */
        if ( INFO ) System.out.print(m_tag + "[I] " +  msg);
        /* Android */
        //if(INFO) Log.i(tag, "[I]" + msg);
    }

    public static void w(String tag, String msg) {
        if (false == tag.isEmpty() && WARN ) {
            System.out.print(m_tag + "[" + tag + "]" + "[W] " +  msg);
        }
    }

    public static void w(String msg) {
        /* Pure Java */
        if ( WARN ) System.out.print(m_tag + "[W] " +  msg);
        /* Android */
        //if(WARN) Log.w(tag, "[W]" + msg);
    }

    public static void e(String tag, String msg) {
        if (false == tag.isEmpty() && ERROR) {
            System.out.print(m_tag + "\033[1;31m[" + tag + "]" +
                    "[E] " +  msg + "\u001B[0m");
        }
    }

    public static void e(String msg) {
        /* Pure Java */
        if ( ERROR ) System.out.print(m_tag + "\033[1;31m[E] " +
                "\u001B[0m" + msg);
        /* Android */
        //if(ERROR) Log.e(tag, "[E]" + msg);
    }

    /**
     * Flag to enable/disable {@link Logger#d(String)}
     */
    public static boolean DEBUG = true;

    /**
     * Flag to enable/disable {@link Logger#i(String)}
     */
    public static boolean INFO = true;

    /**
     * Flag to enable/disable {@link Logger#w(String)}
     */
    public static boolean WARN = true;

    /**
     * Flag to enable/disable {@link Logger#e(String)}
     */
    public static boolean ERROR = true;

    protected static String m_tag = "SEDEC2";
}
