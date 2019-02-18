package sedec2.util;

import java.util.List;

/**
 * Base class to read from specific resource
 */
public interface PacketReader {

    public boolean open();

    public void close();

    public long filesize();

    public long readable();

    public byte[] readPacket();

    public int readPacket(byte[] packet);

    public List<byte[]> readPackets(int packet_count);
}
