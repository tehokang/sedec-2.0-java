package com.sedec.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Wrapper class to utilize file processing like writing
 */
public class FileUtility {
    protected static final String TAG = FileUtility.class.getSimpleName();

    /**
     * Saves byte buffer as a file
     * @param filepath target path to save with file name
     * @param buffer to write
     * @return true if succeed else return false
     */
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

    /**
     * Saves String as a file
     * @param filepath target path to save with file name
     * @param string to write
     * @return true if succeed else return false
     */
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
