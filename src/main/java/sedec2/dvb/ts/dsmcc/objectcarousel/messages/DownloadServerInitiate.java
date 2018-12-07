package sedec2.dvb.ts.dsmcc.objectcarousel.messages;

import sedec2.dvb.ts.dsmcc.objectcarousel.messages.descriptors.CompatibilityDescriptor;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DownloadServerInitiate extends DownloadControlMessage {
    protected byte[] serverId = new byte[20];
    protected CompatibilityDescriptor compatibilityDescriptor;
    protected int privateDataLength;
    protected byte[] privateDataByte;

    public DownloadServerInitiate(byte[] buffer) {
        super(buffer);

        for ( int i=0; i<serverId.length; i++ ) {
            serverId[i] = (byte) readOnBuffer(8);
        }
        compatibilityDescriptor = new CompatibilityDescriptor(this);
        privateDataLength = readOnBuffer(16);
        privateDataByte = new byte[privateDataLength];
        for ( int i=0; i<privateDataByte.length; i++ ) {
            privateDataByte[i] = (byte) readOnBuffer(8);
        }
    }

    public byte[] getServerId() {
        return serverId;
    }

    public CompatibilityDescriptor getCompatibilityDescriptor() {
        return compatibilityDescriptor;
    }

    public byte[] getPrivateDataByte() {
        return privateDataByte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("serverId : \n"));
        BinaryLogger.print(serverId);

        if ( compatibilityDescriptor != null ) compatibilityDescriptor.print();
        Logger.d(String.format("privateDataLength : 0x%x \n", privateDataLength));
        BinaryLogger.print(privateDataByte);

    }
}
