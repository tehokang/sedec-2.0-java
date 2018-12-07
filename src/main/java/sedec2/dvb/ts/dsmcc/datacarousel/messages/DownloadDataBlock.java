package sedec2.dvb.ts.dsmcc.datacarousel.messages;

import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DownloadDataBlock extends DownloadDataMessage {
    protected int moduleId;
    protected byte moduleVersion;
    protected int blockNumber;
    protected byte[] blockDataByte;

    public DownloadDataBlock(byte[] buffer) {
        super(buffer);

        moduleId = readOnBuffer(16);
        moduleVersion = (byte) readOnBuffer(8);
        skipOnBuffer(8);
        blockNumber = readOnBuffer(16);
        blockDataByte = new byte[messageLength-adaptationLength-6];

        for ( int i=0; i<blockDataByte.length; i++ ) {
            blockDataByte[i] = (byte) readOnBuffer(8);
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("moduleId : 0x%x \n", moduleId));
        Logger.d(String.format("moduleVersion : 0x%x \n", moduleVersion));
        Logger.d(String.format("blockNumber : 0x%x \n", blockNumber));
        Logger.d(String.format("blockDataByte.length : 0x%x \n", blockDataByte.length));
        BinaryLogger.print(blockDataByte);

    }

    @Override
    public int getLength() {
        int header_length = super.getLength();
        int payload_length = 6 + blockDataByte.length;

        return header_length + payload_length;
    }
}