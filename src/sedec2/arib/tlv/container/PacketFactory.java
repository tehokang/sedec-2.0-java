package sedec2.arib.tlv.container;

import sedec2.arib.tlv.container.packets.CompressedIpPacket;
import sedec2.arib.tlv.container.packets.IPv4Packet;
import sedec2.arib.tlv.container.packets.IPv6Packet;
import sedec2.arib.tlv.container.packets.NullPacket;
import sedec2.arib.tlv.container.packets.SignallingPacket;
import sedec2.arib.tlv.container.packets.TypeLengthValue;

public class PacketFactory {
    public final static int IPV4_PACKET = 0x01;
    public final static int IPV6_PACKET = 0x02;
    public final static int COMPRESSED_IP_PACKET = 0x03;
    public final static int SIGNALLING_PACKET = 0xfe;
    public final static int NULL_PACKET = 0xff;

    public static TypeLengthValue CreatePacket(byte[] buffer) {
        int table_id = (buffer[1] & 0xff);
        
        switch(table_id) {
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
