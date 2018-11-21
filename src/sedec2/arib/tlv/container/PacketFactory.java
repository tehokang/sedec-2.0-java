package sedec2.arib.tlv.container;

import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.IPv4Packet;
import sedec2.arib.tlv.container.packets.IPv6Packet;
import sedec2.arib.tlv.container.packets.NullPacket;
import sedec2.arib.tlv.container.packets.SignallingPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;

public class PacketFactory {
    public final static byte IPV4_PACKET = (byte) 0x01;
    public final static byte IPV6_PACKET = (byte) 0x02;
    public final static byte COMPRESSED_IP_PACKET = (byte) 0x03;
    public final static byte SIGNALLING_PACKET = (byte) 0xfe;
    public final static byte NULL_PACKET = (byte) 0xff;

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
