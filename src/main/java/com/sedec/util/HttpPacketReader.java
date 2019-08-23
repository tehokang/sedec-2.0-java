package com.sedec.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class to read specific file
 */
public abstract class HttpPacketReader implements PacketReader {
    protected static final String TAG = HttpPacketReader.class.getSimpleName();
    protected URL url= null;
    protected URLConnection conn = null;
    protected InputStream input_stream = null;

    /**
     * Constructor with full path
     * @param url Full path
     */
    public HttpPacketReader(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a file
     * @return true if succeed to open else return false
     */
    @Override
    public boolean open() {
        try {
            conn = url.openConnection();
            input_stream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Closes a file
     */
    @Override
    public void close() {
        try {
            input_stream.close();
            input_stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets size of file
     * @return file size
     */
    @Override
    public long filesize() {
        return conn.getContentLengthLong();
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
