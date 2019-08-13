package com.sedec.dvb.ts.si.tables.dsmcc.datacarousel.messages;

import com.sedec.dvb.ts.si.tables.dsmcc.Message;
import com.sedec.util.Logger;

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
