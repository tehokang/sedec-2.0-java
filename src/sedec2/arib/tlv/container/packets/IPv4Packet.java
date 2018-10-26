package sedec2.arib.tlv.container.packets;

import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class IPv4Packet extends TypeLengthValue {
    protected byte version;
    protected byte header_length;
    protected byte type_of_service;
    protected int total_length;
    protected int identification;
    protected byte flags;
    protected int fragment_offset;
    protected byte time_to_live;
    protected byte protocol;
    protected int header_checksum;
    protected byte[] source_address = new byte[4];
    protected byte[] destination_address = new byte[4];
    protected int options;
    protected byte[] transport_layer_data;
    
    protected NetworkTimeProtocolData ntp = null;
    protected MMTP_Packet mmtp_packet = null;
    
    public IPv4Packet(byte[] buffer) {
        super(buffer);
        
        version = (byte) readOnBuffer(4);
        header_length = (byte) readOnBuffer(4);
        type_of_service = (byte) readOnBuffer(8);
        total_length = readOnBuffer(16);
        
        identification = readOnBuffer(16);
        flags = (byte) readOnBuffer(3);
        fragment_offset = readOnBuffer(13);
        time_to_live = (byte) readOnBuffer(8);
        protocol = (byte) readOnBuffer(8);
        header_checksum = readOnBuffer(16);
        
        source_address[0] = (byte) readOnBuffer(8);
        source_address[1] = (byte) readOnBuffer(8);
        source_address[2] = (byte) readOnBuffer(8);
        source_address[3] = (byte) readOnBuffer(8);
        
        destination_address[0] = (byte) readOnBuffer(8);
        destination_address[1] = (byte) readOnBuffer(8);
        destination_address[2] = (byte) readOnBuffer(8);
        destination_address[3] = (byte) readOnBuffer(8);
        
        options = readOnBuffer(32);

        transport_layer_data = new byte[total_length-8-4-4-4];

        for ( int i=0; i<transport_layer_data.length; i++ ) {
            transport_layer_data[i] = (byte) readOnBuffer(8);
        }
        
        if ( protocol == 0x11 &&
                destination_address[0] == 224 && destination_address[1] == 0 &&
                destination_address[2] == 1 && destination_address[3] == 1 ) {
            ntp = new NetworkTimeProtocolData(transport_layer_data);
        } else {
            mmtp_packet = new MMTP_Packet(transport_layer_data);
        }
    }

    public byte getVersion() {
        return version;
    }
    
    public byte getHeaderLength() {
        return header_length;
    }
    
    public byte getTypeOfService() {
        return type_of_service;
    }
    
    public int getTotalLength() {
        return total_length;
    }
    
    public int getIdentification() {
        return identification;
    }
    
    public byte getFlags() {
        return flags;
    }
    
    public int getFragmentOffset() {
        return fragment_offset;
    }
    
    public byte getTimeToLive() {
        return time_to_live;
    }
    
    public byte getProtocol() {
        return protocol;
    }
    
    public int getHeaderChecksum() {
        return header_checksum;
    }
    
    public byte[] getSourceAddress() {
        return source_address;
    }
    
    public byte[] getDestinationAddress() {
        return destination_address;
    }
    
    public int getOptions() {
        return options;
    }
    
    public byte[] getTransportLayerData() {
        return transport_layer_data;
    }
    
    public NetworkTimeProtocolData getNtp() {
        return ntp;
    }
    
    public MMTP_Packet getMmtp() {
        return mmtp_packet;
    }
    
    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("version : 0x%x \n", version));
        Logger.d(String.format("header_length : 0x%x \n", header_length));
        Logger.d(String.format("type_of_service : 0x%x \n", type_of_service));
        Logger.d(String.format("total_length : 0x%x \n", total_length));
        Logger.d(String.format("identification : 0x%x \n",  identification));
        Logger.d(String.format("flags : 0x%x \n", flags));
        Logger.d(String.format("fragment_offset : 0x%x \n", fragment_offset));
        Logger.d(String.format("time_to_live : 0x%x \n", time_to_live));
        Logger.d(String.format("protocol : 0x%x \n", protocol));
        Logger.d(String.format("header_checksum : 0x%x \n", header_checksum));
        Logger.d(String.format("source_address : %d.%d.%d.%d \n", 
                source_address[0], source_address[1], source_address[2], source_address[3]));
        Logger.d(String.format("destination_address : %d.%d.%d.%d \n", 
                destination_address[0], destination_address[1],
                destination_address[2], destination_address[3]));
        
        Logger.d(String.format("options : 0x%x \n", options));
        
        if ( null != ntp ) {
            ntp.print();
        }
        
        if ( null != mmtp_packet ) {
            mmtp_packet.print();
        }
        
        BinaryLogger.print(transport_layer_data);
    }
}
