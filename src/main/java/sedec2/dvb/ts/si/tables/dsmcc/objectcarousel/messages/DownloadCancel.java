package sedec2.dvb.ts.si.tables.dsmcc.objectcarousel.messages;

import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DownloadCancel extends DownloadControlMessage {
    protected int downloadId;
    protected int moduleId;
    protected int blockNumber;
    protected byte downloadCancelReason;
    protected int privateDataLength;
    protected byte[] privateDataByte;

    public DownloadCancel(byte[] buffer) {
        super(buffer);

        downloadId = readOnBuffer(32);
        moduleId = readOnBuffer(16);
        blockNumber = readOnBuffer(16);
        downloadCancelReason = (byte) readOnBuffer(8);
        skipOnBuffer(8);
        privateDataLength = readOnBuffer(16);
        privateDataByte = new byte[privateDataLength];
        for ( int i=0; i<privateDataByte.length; i++ ) {
            privateDataByte[i] = (byte) readOnBuffer(8);
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("downloadId : 0x%x \n", downloadId));
        Logger.d(String.format("moduleId : 0x%x \n", moduleId));
        Logger.d(String.format("blockNumber : 0x%x \n", blockNumber));
        Logger.d(String.format("downloadCancelReason : 0x%x \n", downloadCancelReason));
        Logger.d(String.format("privateDataLength : 0x%x \n", privateDataLength));
        BinaryLogger.print(privateDataByte);
    }

    @Override
    public int getLength() {
        int header_length = super.getLength();
        int payload_length = 12 + privateDataByte.length;

        return header_length + payload_length;
    }
}
