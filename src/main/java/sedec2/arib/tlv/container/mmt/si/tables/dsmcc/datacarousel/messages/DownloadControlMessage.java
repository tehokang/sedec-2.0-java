package sedec2.arib.tlv.container.mmt.si.tables.dsmcc.datacarousel.messages;

import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.Message;
import sedec2.util.Logger;

public abstract class DownloadControlMessage extends Message {
    public DownloadControlMessage(byte[] buffer) {
        super(buffer);

        protocolDiscriminator = (byte) readOnBuffer(8);
        dsmccType = (byte) readOnBuffer(8);
        messageId = readOnBuffer(16);
        transactionId = readOnBuffer(32);
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
        Logger.d(String.format("======= DownloadControlMessage ======= (%s)\n", getClass().getName()));
        super._print_();
    }

    @Override
    public abstract void print();
}
