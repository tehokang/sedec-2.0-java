package sedec2.arib.ts.container;

import sedec2.arib.ts.container.packets.TransportStream;

/**
 * Factory to obtain a kind of TS like below.
 * <ul>
 * <li> {@link TransportStream} which contains table of ISO-13818
 * </ul>
 */
public class PacketFactory {
    public final static byte TS_188_PACKET = (byte) 0x47;

    /**
     * Creates specific packet of a kind of TS
     * @param buffer one TS packet raw
     * @return one TS packet which is decoded
     */
    public static TransportStream createPacket(byte[] buffer) {
        byte packet_type = (byte)(buffer[0] & 0xff);

        switch ( packet_type ) {
            case TS_188_PACKET:
                return new TransportStream(buffer);
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
