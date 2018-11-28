package sedec2.util;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class to read specific TLV file
 */
public abstract class TlvReader {
    protected static final String TAG = TlvReader.class.getSimpleName();

    protected File tlv_file = null;
    protected final int TLV_HEADER_LENGTH = 4;
    protected ByteBuffer output_buffer = null;

    /**
     * Constructor with TLV full path
     * @param tlv_file Full path of TLV
     */
    public TlvReader(String tlv_file) {
        this.tlv_file = new File(tlv_file);
    }

    /**
     * Opens a TLV file
     * @return true if succeed to open else return false
     */
    public abstract boolean open();

    /**
     * Closes a TLV file
     */
    public void close() {
        tlv_file = null;
    }

    /**
     * Gets size of TLV file
     * @return file size
     */
    public long filesize() {
        return tlv_file.length();
    }

    /**
     * Gets status of file buffer during processing like reading
     * @return remaining size of file buffer
     */
    public abstract long readable();

    /**
     * Gets a TLV packet from TLV file and internal buffer point will move
     * @return byte buffer as a TLV packet
     */
    public abstract byte[] readPacket();

    /**
     * Gets a TLV packet from TLV file and internal buffer point will move
     * @param tlv_packet byte buffer as a TLV packet which did read
     * @return size of a TLV packet
     */
    public abstract int readPacket(byte[] tlv_packet);

    /**
     * Gets TLV packets from TLV file as much as user wants to get.
     * @param packet_count count which user wants
     * @return list of TLV packets as byte buffer
     */
    public List<byte[]> readPackets(int packet_count) {
        List<byte[]> tlv_packets = new ArrayList<>();

        for ( int i=0; i<packet_count; i++ ) {
            byte[] tlv_packet = readPacket();
            tlv_packets.add(tlv_packet);
        }
        return tlv_packets;
    }
}
