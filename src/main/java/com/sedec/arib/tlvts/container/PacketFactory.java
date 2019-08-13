package com.sedec.arib.tlvts.container;

import com.sedec.arib.tlvts.container.packets.TlvTransportStream;

public class PacketFactory {
    public final static byte TS_188_PACKET = (byte) 0x47;

    /**
     * Creates specific packet of a kind of TS
     * @param buffer one TS packet raw
     * @return one TS packet which is decoded
     */
    public static TlvTransportStream createPacket(byte[] buffer) {
        byte packet_type = (byte)(buffer[0] & 0xff);

        switch ( packet_type ) {
            case TS_188_PACKET:
                return new TlvTransportStream(buffer);
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
