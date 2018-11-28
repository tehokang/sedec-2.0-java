package sedec2.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Utility class to read a TLV file as one of TlvReader
 */
public class TlvFileReader extends TlvReader {
    protected static final String TAG = TlvFileReader.class.getSimpleName();
    protected DataInputStream input_stream  = null;

    /**
     * Constructor with a TLV full path
     * @param tlv_file TLV full file path
     */
    public TlvFileReader(String tlv_file) {
        super(tlv_file);
    }

    @Override
    public boolean open() {
        try {
            Logger.d(String.format("TlvFileReader opened (%s) \n",
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
        byte[] tlv_header_buffer = null;
        byte[] tlv_payload_buffer = null;

        try {
            tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
            input_stream.read(tlv_header_buffer);

            tlv_payload_buffer =
                    new byte[((tlv_header_buffer[2] & 0xff) << 8 |
                            (tlv_header_buffer[3] & 0xff))];
            input_stream.read(tlv_payload_buffer);

            output_buffer = ByteBuffer.wrap(tlv_packet);
            output_buffer.put(tlv_header_buffer);
            output_buffer.put(tlv_payload_buffer);
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
            input_stream.read(tlv_header_buffer);

            tlv_payload_buffer =
                    new byte[((tlv_header_buffer[2] & 0xff) << 8 |
                            (tlv_header_buffer[3] & 0xff))];
            input_stream.read(tlv_payload_buffer);

            output_buffer = ByteBuffer.allocate(
                    tlv_header_buffer.length + tlv_payload_buffer.length);
            output_buffer.put(tlv_header_buffer);
            output_buffer.put(tlv_payload_buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output_buffer.array();
    }
}
