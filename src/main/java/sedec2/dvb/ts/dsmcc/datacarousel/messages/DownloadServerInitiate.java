package sedec2.dvb.ts.dsmcc.datacarousel.messages;

import sedec2.base.BitReadWriter;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.descriptors.CompatibilityDescriptor;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DownloadServerInitiate extends DownloadControlMessage {
    protected byte[] serverId = new byte[20];
    protected CompatibilityDescriptor compatibilityDescriptor;
    protected int privateDataLength;
    protected byte[] privateDataByte;
    protected GroupInfoIndication group_info_indication;
    public DownloadServerInitiate(BitReadWriter brw) {
        super(brw);

        for ( int i=0; i<serverId.length; i++ ) {
            serverId[i] = (byte) brw.readOnBuffer(8);
        }
        compatibilityDescriptor = new CompatibilityDescriptor(brw);
        privateDataLength = brw.readOnBuffer(16);
        privateDataByte = new byte[privateDataLength];
        for ( int i=0; i<privateDataByte.length; i++ ) {
            privateDataByte[i] = (byte) brw.readOnBuffer(8);
        }
        group_info_indication = new GroupInfoIndication(privateDataByte);
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

        if ( group_info_indication != null ) group_info_indication.print();
    }
}
