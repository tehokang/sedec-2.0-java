package com.sedec.util;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Utility class to read a TLV HTTP as one of TlvReader
 */
public class HttpTsPacketReader extends HttpPacketReader {
    protected static final String TAG = HttpTlvPacketReader.class.getSimpleName();
    protected final int TLV_HEADER_LENGTH = 4;
    protected ByteBuffer output_buffer = null;
    protected long total_read_bytes = 0;
    /**
     * Constructor with a TLV full path
     * @param url TLV full file path
     */
    public HttpTsPacketReader(String url) {
        super(url);
    }

    @Override
    public long readable() {
        if ( conn != null ) {
            return conn.getContentLengthLong() - total_read_bytes;
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
