package sedec2.dvb.ts.dsmcc.datacarousel.messages;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public abstract class DownloadDataMessage extends Message {
    public DownloadDataMessage(BitReadWriter brw) {
        super(brw);

        protocolDiscriminator = (byte) brw.readOnBuffer(8);
        dsmccType = (byte) brw.readOnBuffer(8);
        messageId = brw.readOnBuffer(16);
        downloadId = brw.readOnBuffer(32);
        brw.skipOnBuffer(8);
        adaptationLength = (byte) brw.readOnBuffer(8);
        messageLength = brw.readOnBuffer(16);

        /**
         * dsmccAdaptationHeader()
         */
        if ( adaptationLength > 0 ) {
            adaptationType = (byte) brw.readOnBuffer(8);
            adaptationDataByte = new byte[adaptationLength-1];
            for ( int i=0; i<adaptationDataByte.length; i++ ) {
                adaptationDataByte[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    @Override
    public void _print_() {
        Logger.d(String.format("======= DownloadDataMessage ======= (%s)\n", getClass().getName()));
        super._print_();
    }
}
