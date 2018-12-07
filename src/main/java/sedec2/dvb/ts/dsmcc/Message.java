package sedec2.dvb.ts.dsmcc;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

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
        Logger.d(String.format("adaptationDataByte.length :\n", adaptationDataByte.length));
    }

    public abstract void print();
}
