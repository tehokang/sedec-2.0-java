package com.sedec.arib.tlv.container;

import com.sedec.arib.tlv.container.packets.CompressedIpPacket;
import com.sedec.arib.tlv.container.packets.IPv4Packet;
import com.sedec.arib.tlv.container.packets.IPv6Packet;
import com.sedec.arib.tlv.container.packets.NullPacket;
import com.sedec.arib.tlv.container.packets.SignallingPacket;
import com.sedec.arib.tlv.container.packets.TypeLengthValue;

/**
 * Factory to obtain a kind of TLV like below.
 * <ul>
 * <li> {@link IPv4Packet} which contains Network Time Protocol Data
 * <li> {@link IPv6Packet} which contains Network Time Protocol Data
 * <li> {@link CompressedIpPacket} which contains a MMT packet
 * <li> {@link SignallingPacket} which contains table of TLV-SI
 * </ul>
 */
public class PacketFactory {
    public final static byte IPV4_PACKET = (byte) 0x01;
    public final static byte IPV6_PACKET = (byte) 0x02;
    public final static byte COMPRESSED_IP_PACKET = (byte) 0x03;
    public final static byte SIGNALLING_PACKET = (byte) 0xfe;
    public final static byte NULL_PACKET = (byte) 0xff;

    /**
     * Creates specific packet of a kind of TLV
     * @param buffer one TLV packet raw
     * @return one TLV packet which is decoded
     */
    public static TypeLengthValue createPacket(byte[] buffer) {
        byte packet_type = (byte)(buffer[1] & 0xff);

        switch ( packet_type ) {
            case IPV4_PACKET:
                return new IPv4Packet(buffer);
            case IPV6_PACKET:
                return new IPv6Packet(buffer);
            case COMPRESSED_IP_PACKET:
                return new CompressedIpPacket(buffer);
            case SIGNALLING_PACKET:
                return new SignallingPacket(buffer);
            case NULL_PACKET:
                return new NullPacket(buffer);
            default:
                break;
        }
        return null;
    }

    private PacketFactory() {
        /**
         * @warning Nothing to do since this factory isn't working as instance
         */
    }
}
