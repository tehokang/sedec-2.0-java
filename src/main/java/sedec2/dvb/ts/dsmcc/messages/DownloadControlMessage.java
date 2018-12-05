package sedec2.dvb.ts.dsmcc.messages;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public abstract class DownloadControlMessage extends Message {
    public DownloadControlMessage(BitReadWriter brw) {
        super(brw);

        protocolDiscriminator = (byte) brw.readOnBuffer(8);
        dsmccType = (byte) brw.readOnBuffer(8);
        messageId = brw.readOnBuffer(16);
        transactionId = brw.readOnBuffer(32);
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
        Logger.d(String.format("======= DownloadControlMessage ======= (%s)\n", getClass().getName()));
        super._print_();
    }

    @Override
    public abstract void print();
}
