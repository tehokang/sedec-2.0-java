package sedec2.arib.tlv.container.packets;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * Base class of kinds of TLV, every TLV packets has to inherited from this.
 *
 * <ul>
 * <li> {@link IPv4Packet} which contains Network Time Protocol Data
 * <li> {@link IPv6Packet} which contains Network Time Protocol Data
 * <li> {@link CompressedIpPacket} which contains a MMT packet
 * <li> {@link SignallingPacket} which contains table of TLV-SI
 * </ul>
 */
public class TypeLengthValue extends BitReadWriter {
    protected byte sync = 0x01;
    protected byte packet_type;
    protected int length;

    /**
     * Constructor to decode header of TLV
     * @param buffer one TLV packet as byte buffer
     */
    public TypeLengthValue(byte[] buffer) {
        super(buffer);

        sync = (byte) readOnBuffer(2);
        skipOnBuffer(6);

        packet_type = (byte) readOnBuffer(8);
        length = readOnBuffer(16);
    }

    public byte getPacketType() {
        return packet_type;
    }

    public int getLength() {
        return 4 + length;
    }

    public void print() {
        Logger.d(String.format("======= TLV Header ======= (%s)\n", getClass().getName()));
        Logger.d(String.format("sync : 0x%x \n", sync));
        Logger.d(String.format("packet_type : 0x%x \n", packet_type));
        Logger.d(String.format("length : 0x%x (%d) \n", length, length));
        Logger.d(String.format("-------------------------- \n"));
    }
}
