package sedec2.util;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Utility class to read a TLV HTTP as one of TlvReader
 */
public class HttpTlvPacketReader extends HttpPacketReader {
    protected static final String TAG = HttpTlvPacketReader.class.getSimpleName();
    protected final int TLV_HEADER_LENGTH = 4;
    protected ByteBuffer output_buffer = null;
    protected long total_read_bytes = 0;
    /**
     * Constructor with a TLV full path
     * @param url TLV full file path
     */
    public HttpTlvPacketReader(String url) {
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
    public int readPacket(byte[] tlv_packet) {
        byte[] tlv_header_buffer = null;
        byte[] tlv_payload_buffer = null;

        try {
            tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
            int offset = 0;
            while( offset < tlv_header_buffer.length ) {
                int read_bytes = input_stream.read(tlv_header_buffer,
                        offset, tlv_header_buffer.length-offset);
                if ( read_bytes == -1 ) break;
                offset += read_bytes;
            }

            tlv_payload_buffer =
                    new byte[((tlv_header_buffer[2] & 0xff) << 8 |
                            (tlv_header_buffer[3] & 0xff))];

            offset = 0;
            while( offset < tlv_payload_buffer.length ) {
                int read_bytes = input_stream.read(tlv_payload_buffer,
                        offset, tlv_payload_buffer.length-offset);
                if ( read_bytes == -1 ) break;
                offset += read_bytes;
            }

            output_buffer = ByteBuffer.wrap(tlv_packet);
            output_buffer.put(tlv_header_buffer);
            output_buffer.put(tlv_payload_buffer);

            total_read_bytes += output_buffer.array().length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tlv_header_buffer.length + tlv_payload_buffer.length;
    }

    @Override
    public byte[] readPacket() {
        byte[] tlv_header_buffer = null;
        byte[] tlv_payload_buffer = null;

        try {
            tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
            int offset = 0;
            while( offset < tlv_header_buffer.length ) {
                int read_bytes = input_stream.read(tlv_header_buffer,
                        offset, tlv_header_buffer.length-offset);
                if ( read_bytes == -1 ) break;
                offset += read_bytes;
            }

            tlv_payload_buffer =
                    new byte[((tlv_header_buffer[2] & 0xff) << 8 |
                            (tlv_header_buffer[3] & 0xff))];
            offset = 0;
            while( offset < tlv_payload_buffer.length ) {
                int read_bytes = input_stream.read(tlv_payload_buffer,
                        offset, tlv_payload_buffer.length-offset);
                if ( read_bytes == -1 ) break;
                offset += read_bytes;
            }

            output_buffer = ByteBuffer.allocate(
                    tlv_header_buffer.length + tlv_payload_buffer.length);
            output_buffer.put(tlv_header_buffer);
            output_buffer.put(tlv_payload_buffer);

            total_read_bytes += output_buffer.array().length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output_buffer.array();
    }
}
