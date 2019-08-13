package com.sedec.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FileTs188PacketReader extends FilePacketReader {
    protected static final String TAG = FileTs188PacketReader.class.getSimpleName();
    protected DataInputStream input_stream  = null;
    protected ByteBuffer output_buffer = null;

    /**
     * Constructor with a TS full path
     * @param ts_file TS full file path
     */
    public FileTs188PacketReader(String ts_file) {
        super(ts_file);
    }

    @Override
    public boolean open() {
        try {
            Logger.d(String.format("TsFileReader opened (%s) \n",
                    file.getName()));
            input_stream  =
                    new DataInputStream(
                            new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        super.close();
        try {
            if ( input_stream != null ) {
                input_stream.close();
                input_stream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long readable() {
        try {
            if ( input_stream != null ) {
                return input_stream.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int readPacket(byte[] ts_packet) {
        byte[] ts_buffer = null;

        try {
            ts_buffer = new byte[188];
            input_stream.read(ts_buffer);

            output_buffer = ByteBuffer.wrap(ts_packet);
            output_buffer.put(ts_buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ts_buffer.length;
    }

    @Override
    public byte[] readPacket() {
        byte[] ts_buffer = null;

        try {
            ts_buffer = new byte[188];
            input_stream.read(ts_buffer);

            output_buffer = ByteBuffer.allocate(ts_buffer.length);
            output_buffer.put(ts_buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output_buffer.array();
    }
}
