package sedec2.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class FileUtility {
    public static boolean save(String filepath, byte[] buffer) {
        try {
            FileOutputStream fstream = new FileOutputStream(filepath);
            fstream.write(buffer);
            fstream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean save(String filepath, String string) {
        try {
            PrintWriter out;
            out = new PrintWriter(filepath);
            out.println(string);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
