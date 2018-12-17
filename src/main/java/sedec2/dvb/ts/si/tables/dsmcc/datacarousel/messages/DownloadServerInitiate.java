package sedec2.dvb.ts.si.tables.dsmcc.datacarousel.messages;

import sedec2.dvb.ts.si.tables.dsmcc.datacarousel.messages.descriptors.CompatibilityDescriptor;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

/**
 * Class to represent 7.3.6 DownloadServerInitiate of ISO 13818-6
 */
public class DownloadServerInitiate extends DownloadControlMessage {
    protected byte[] serverId = new byte[20];
    protected CompatibilityDescriptor compatibilityDescriptor;
    protected int privateDataLength;
    protected byte[] privateDataByte;
    protected GroupInfoIndication group_info_indication;

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

    public GroupInfoIndication getGroupInfoIndication() {
        return group_info_indication;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("serverId : \n"));
        BinaryLogger.print(serverId);

        if ( compatibilityDescriptor != null ) compatibilityDescriptor.print();
        if ( privateDataByte != null ) {
            Logger.d(String.format("privateDataLength : 0x%x \n", privateDataByte.length));
            BinaryLogger.print(privateDataByte);
        }

        if ( group_info_indication != null ) group_info_indication.print();
    }
}
