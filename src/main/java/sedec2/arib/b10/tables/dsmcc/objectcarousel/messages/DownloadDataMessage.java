package sedec2.arib.b10.tables.dsmcc.objectcarousel.messages;

import sedec2.arib.b10.tables.dsmcc.Message;
import sedec2.util.Logger;

public abstract class DownloadDataMessage extends Message {
    public DownloadDataMessage(byte[] buffer) {
        super(buffer);

        protocolDiscriminator = (byte) readOnBuffer(8);
        dsmccType = (byte) readOnBuffer(8);
        messageId = readOnBuffer(16);
        downloadId = readOnBuffer(32);
        skipOnBuffer(8);
        adaptationLength = (byte) readOnBuffer(8);
        messageLength = readOnBuffer(16);

        /**
         * dsmccAdaptationHeader()
         */
        if ( adaptationLength > 0 ) {
            adaptationType = (byte) readOnBuffer(8);
            adaptationDataByte = new byte[adaptationLength-1];
            for ( int i=0; i<adaptationDataByte.length; i++ ) {
                adaptationDataByte[i] = (byte) readOnBuffer(8);
            }
        }
    }

    @Override
    public void _print_() {
        Logger.d(String.format("======= DownloadDataMessage ======= (%s)\n", getClass().getName()));
        super._print_();
    }
}
