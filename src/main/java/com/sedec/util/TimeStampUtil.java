package com.sedec.util;

import com.sedec.base.BitReadWriter;
import com.sedec.dvb.ts.container.packets.TransportStream;

public class TimeStampUtil {

    /**
    * User can get PTS (Presentation Timestamp) from TS Packet
    * @param ts one TS packet
    * @return PTS
    */
    public static long getPtsFromTS(TransportStream ts) {
        long pts = -1;
        if ( ts.getPayloadUnitStartIndicator() == 0x01 ) {
            pts = getPtsFromPES(ts.getDataByte());
        }
        return pts;
    }

    /**
     * User can get PTS (Presentation Timestamp) from PES Packet
     * @param data one PES packet
     * @return PTS
     */
    public static long getPtsFromPES(byte[] data) {
        long pts = -1;
        BitReadWriter bitReadWriter = new BitReadWriter(data);
        int startCodePrefix = bitReadWriter.readOnBuffer(24);
        if ( startCodePrefix == 0x000001 ) {
            bitReadWriter.skipOnBuffer(8);  // stream id
            bitReadWriter.skipOnBuffer(16); // payload length
            bitReadWriter.skipOnBuffer(8); // flags
            if(bitReadWriter.readOnBuffer(1) == 1) { // pts flag
                bitReadWriter.skipOnBuffer(1); // dts flag
                bitReadWriter.skipOnBuffer(6); // flags
                bitReadWriter.skipOnBuffer(8); // extend header length
                bitReadWriter.skipOnBuffer(4);
                pts = (long) bitReadWriter.readOnBuffer(3) << 30;
                bitReadWriter.skipOnBuffer(1);
                pts |= bitReadWriter.readOnBuffer(15) << 15;
                bitReadWriter.skipOnBuffer(1);
                pts |= bitReadWriter.readOnBuffer(15);
            }
        }
        return pts;
    }
}
