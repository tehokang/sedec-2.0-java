package com.sedec.arib.b10.tables.dsmcc.objectcarousel.messages;

import com.sedec.arib.b10.tables.dsmcc.objectcarousel.biop.ServiceGatewayInfo;
import com.sedec.arib.b10.tables.dsmcc.objectcarousel.messages.descriptors.CompatibilityDescriptor;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

/**
 * Class to represent 7.3.6 DownloadServerInitiate of ISO 13818-6
 */
public class DownloadServerInitiate extends DownloadControlMessage {
    protected byte[] serverId = new byte[20];
    protected CompatibilityDescriptor compatibilityDescriptor;
    protected int privateDataLength;
    protected byte[] privateDataByte;
    protected ServiceGatewayInfo service_gateway_info;

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
        service_gateway_info = new ServiceGatewayInfo(privateDataByte);
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

    public ServiceGatewayInfo getServiceGatewayInfo() {
        return service_gateway_info;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("serverId : \n"));
        BinaryLogger.print(serverId);

        if ( compatibilityDescriptor != null ) compatibilityDescriptor.print();
        Logger.d(String.format("privateDataLength : 0x%x \n", privateDataLength));
        BinaryLogger.print(privateDataByte);

        if ( service_gateway_info != null ) service_gateway_info.print();
    }
}
