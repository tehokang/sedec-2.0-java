package sedec2.util;

import java.io.FileOutputStream;

public class FileUtility {
    public static boolean save(String filepath, byte[] buffer) {
        try {
            FileOutputStream fstream = new FileOutputStream("ttml.mfu.0x%04x.font.svg");
            fstream.write(buffer);
            fstream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } 
        return true;
    }
}
