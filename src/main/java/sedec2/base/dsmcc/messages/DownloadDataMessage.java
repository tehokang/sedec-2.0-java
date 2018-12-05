package sedec2.base.dsmcc.messages;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public abstract class DownloadDataMessage extends BitReadWriter {
    protected byte protocolDiscriminator;
    protected byte dsmccType;
    protected int messageId;
    protected int downloadId;
    protected byte adaptationLength;
    protected int messageLength;
    protected byte adaptationType;
    protected byte[] adaptationDataByte;

    public DownloadDataMessage(byte[] buffer) {
        super(buffer);

        protocolDiscriminator = (byte) readOnBuffer(8);
        dsmccType = (byte) readOnBuffer(8);
        messageId = readOnBuffer(16);
        downloadId = readOnBuffer(32);
        skipOnBuffer(8);
        adaptationLength = (byte) readOnBuffer(8);
        messageLength = readOnBuffer(16);

        adaptationType = (byte) readOnBuffer(8);
        adaptationDataByte = new byte[adaptationLength-1];
        for ( int i=0; i<adaptationDataByte.length; i++ ) {
            adaptationDataByte[i] = (byte) readOnBuffer(8);
        }
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
        return 13 + adaptationDataByte.length;
    }

    public void print() {
        Logger.d(String.format("protocolDiscriminator : 0x%x \n", protocolDiscriminator));
        Logger.d(String.format("dsmccType : 0x%x \n", dsmccType));
        Logger.d(String.format("messageId : 0x%x \n", messageId));
        Logger.d(String.format("downloadId : 0x%x \n", downloadId));
        Logger.d(String.format("adaptationLength : 0x%x \n", adaptationLength));
        Logger.d(String.format("messageLength : 0x%x \n", messageLength));
        Logger.d(String.format("adaptationType : 0x%x \n", adaptationType));
        Logger.d(String.format("adaptationDataByte :\n"));
        BinaryLogger.print(adaptationDataByte);
    }
}
