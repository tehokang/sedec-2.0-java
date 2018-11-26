package sedec2.util;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class TlvReader {
    protected File tlv_file = null;
    protected final int TLV_HEADER_LENGTH = 4;
    protected ByteBuffer output_buffer = null;

    public TlvReader(String tlv_file) {
        this.tlv_file = new File(tlv_file);
    }

    public abstract boolean open();

    public void close() {
        tlv_file = null;
    }

    public long filesize() {
        return tlv_file.length();
    }

    public abstract long readable();

    public abstract byte[] readPacket();

    public abstract int readPacket(byte[] tlv_packet);

    public List<byte[]> readPackets(int packet_count) {
        List<byte[]> tlv_packets = new ArrayList<>();

        for ( int i=0; i<packet_count; i++ ) {
            byte[] tlv_packet = readPacket();
            tlv_packets.add(tlv_packet);
        }
        return tlv_packets;
    }
}
