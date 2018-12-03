package sedec2.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TsFileReader extends PacketReader {
    protected static final String TAG = TsFileReader.class.getSimpleName();
    protected DataInputStream input_stream  = null;

    /**
     * Constructor with a TS full path
     * @param ts_file TS full file path
     */
    public TsFileReader(String ts_file) {
        super(ts_file);
    }

    @Override
    public boolean open() {
        try {
            Logger.d(String.format("TsFileReader opened (%s) \n",
                    tlv_file.getName()));
            input_stream  =
                    new DataInputStream(
                            new BufferedInputStream(new FileInputStream(tlv_file)));
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
    public int readPacket(byte[] tlv_packet) {
        byte[] ts_buffer = null;

        try {
            ts_buffer = new byte[188];
            input_stream.read(ts_buffer);

            output_buffer = ByteBuffer.wrap(tlv_packet);
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
