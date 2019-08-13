package com.sedec.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class to read specific file
 */
public abstract class FilePacketReader implements PacketReader {
    protected static final String TAG = FilePacketReader.class.getSimpleName();
    protected File file = null;

    /**
     * Constructor with full path
     * @param file Full path
     */
    public FilePacketReader(String file) {
        this.file = new File(file);
    }

    /**
     * Opens a file
     * @return true if succeed to open else return false
     */
    @Override
    public abstract boolean open();

    /**
     * Closes a file
     */
    @Override
    public void close() {
        file = null;
    }

    /**
     * Gets size of file
     * @return file size
     */
    @Override
    public long filesize() {
        return file.length();
    }

    /**
     * Gets status of file buffer during processing like reading
     * @return remaining size of file buffer
     */
    @Override
    public abstract long readable();

    /**
     * Gets a packet from file and internal buffer point will move
     * @return byte buffer as a packet
     */
    @Override
    public abstract byte[] readPacket();

    /**
     * Gets a packet from file and internal buffer point will move
     * @param packet a byte buffer as a packet which did read
     * @return size of a packet
     */
    @Override
    public abstract int readPacket(byte[] packet);

    /**
     * Gets packets from file as much as user wants to get.
     * @param packet_count count which user wants
     * @return list of packets as byte buffer
     */
    @Override
    public List<byte[]> readPackets(int packet_count) {
        List<byte[]> packets = new ArrayList<>();

        for ( int i=0; i<packet_count; i++ ) {
            byte[] packet = readPacket();
            packets.add(packet);
        }
        return packets;
    }
}
