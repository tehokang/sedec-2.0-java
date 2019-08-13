package com.sedec.dvb.ts.si.tables.dsmcc;

import com.sedec.base.BitReadWriter;
import com.sedec.dvb.ts.si.tables.dsmcc.objectcarousel.messages.DownloadInfoIndication;
import com.sedec.dvb.ts.si.tables.dsmcc.objectcarousel.messages.DownloadServerInitiate;
import com.sedec.util.Logger;

/**
 * Abstraction class to describe header of both dsmccMessageHeader of DSI, DII and
 * dsmccDownloadDataHeader of DDB.
 * - dsmccMessageHeader of Table 2-1 in ISO 13818-6 2.DSM-CC Message Header
 * - dsmccDownloadDataHeader of Table 7-3 in ISO 13818-6
 *
 * {@link DownloadInfoIndication} as ObjectCarousel
 * {@link DownloadServerInitiate} as ObjectCarousel
 * {@link com.sedec.dvb.ts.si.tables.dsmcc.datacarousel.messages.DownloadInfoIndication} as DataCarousel
 * {@link com.sedec.dvb.ts.si.tables.dsmcc.datacarousel.messages.DownloadServerInitiate} as DataCarousel
 *
 * The messages should be defined by the kind of carousel of Data or Object.
 */
public abstract class Message extends BitReadWriter {
    protected byte protocolDiscriminator;
    protected byte dsmccType;
    protected int messageId;
    protected int transactionId;
    protected int downloadId;
    protected byte adaptationLength;
    protected int messageLength;
    protected byte adaptationType;
    protected byte[] adaptationDataByte;

    public Message(byte[] buffer) {
        super(buffer);

    }

    public byte getProtocolDiscriminator() {
        return protocolDiscriminator;
    }

    public byte getDsmccType() {
        return dsmccType;
    }

    public int getMessageId() {
        return messageId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public byte getAdaptationLength() {
        return adaptationLength;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public byte getAdaptationType() {
        return adaptationType;
    }

    public byte[] getAdaptationDataByte() {
        return adaptationDataByte;
    }

    public int getLength() {
        return 12 + adaptationLength;
    }

    public void _print_() {
        Logger.d(String.format("protocolDiscriminator : 0x%x \n", protocolDiscriminator));
        Logger.d(String.format("dsmccType : 0x%x \n", dsmccType));
        Logger.d(String.format("messageId : 0x%x \n", messageId));
        Logger.d(String.format("downloadId : 0x%x \n", downloadId));
        Logger.d(String.format("transactionId : 0x%x \n", transactionId));
        Logger.d(String.format("adaptationLength : 0x%x \n", adaptationLength));
        Logger.d(String.format("messageLength : 0x%x \n", messageLength));
        Logger.d(String.format("adaptationType : 0x%x \n", adaptationType));
        if ( adaptationDataByte != null ) {
            Logger.d(String.format("adaptationDataByte.length :\n",
                    adaptationDataByte.length));
        }
    }

    public abstract void print();
}
