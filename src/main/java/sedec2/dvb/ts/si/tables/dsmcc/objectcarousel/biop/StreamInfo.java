package sedec2.dvb.ts.si.tables.dsmcc.objectcarousel.biop;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class StreamInfo {
    protected byte aDescription_length;
    protected byte[] aDescription_bytes;
    protected int duration_aSeconds;
    protected int duration_aMicroSeconds;
    protected byte audio;
    protected byte video;
    protected byte data;

    public StreamInfo(BitReadWriter brw) {
        aDescription_length = (byte) brw.readOnBuffer(8);
        aDescription_bytes = new byte[aDescription_length];
        for ( int i=0; i<aDescription_bytes.length; i++ ) {
            aDescription_bytes[i] = (byte) brw.readOnBuffer(8);
        }
        duration_aSeconds = brw.readOnBuffer(32);
        duration_aMicroSeconds = brw.readOnBuffer(16);
        audio = (byte) brw.readOnBuffer(8);
        video = (byte) brw.readOnBuffer(8);
        data = (byte) brw.readOnBuffer(8);
    }

    public int getLength() {
        return 10 + aDescription_bytes.length;
    }

    public void print() {
        Logger.d(String.format("\t aDescription_length : 0x%x \n", aDescription_length));
        Logger.d(String.format("\t aDescription_bytes : \n"));
        BinaryLogger.print(aDescription_bytes);

        Logger.d(String.format("\t duration_aSeconds : 0x%x \n", duration_aSeconds));
        Logger.d(String.format("\t duration_aMicroSeconds : 0x%x \n",
                duration_aMicroSeconds));
        Logger.d(String.format("\t audio : 0x%x \n", audio));
        Logger.d(String.format("\t video : 0x%x \n", video));
        Logger.d(String.format("\t data : 0x%x \n", data));
    }
}
