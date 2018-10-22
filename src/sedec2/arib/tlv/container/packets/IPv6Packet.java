package sedec2.arib.tlv.container.packets;

import sedec2.util.Logger;

public class IPv6Packet extends TypeLengthValue {
    protected byte version;
    protected byte traffic_class;
    protected int flow_label;
    protected int payload_length;
    protected byte next_header;
    protected byte hop_limit;
    protected byte[] source_address = new byte[16];
    protected byte[] destination_address = new byte[16];
    protected byte[] transport_layer_data;
    
    protected NetworkTimeProtocolData ntp = null;
    
    public IPv6Packet(byte[] buffer) {
        super(buffer);
        
        version = (byte) ReadOnBuffer(4);
        traffic_class = (byte) ReadOnBuffer(8);
        flow_label = ReadOnBuffer(20);
        payload_length = ReadOnBuffer(16);
        next_header = (byte) ReadOnBuffer(8);
        hop_limit = (byte) ReadOnBuffer(8);
        
        for ( int i=0; i<16; i++ ) {
            source_address[i] = (byte) ReadOnBuffer(8);
        }
        
        for ( int i=0; i<16; i++ ) {
            destination_address[i] = (byte) ReadOnBuffer(8);
        }
        
        transport_layer_data = new byte[payload_length];
        
        for ( int i=0; i<transport_layer_data.length; i++ ) {
            transport_layer_data[i] = (byte) ReadOnBuffer(8);
        }
        
        if ( next_header == 0x11 && 
                destination_address[14] == 0x01 && destination_address[15] == 0x01 ) {
            ntp = new NetworkTimeProtocolData(transport_layer_data);
        }
    }

    public byte GetVersion() {
        return version;
    }
    
    public byte GetTrafficClass() {
        return traffic_class;
    }
    
    public int GetFlowLabel() {
        return flow_label;
    }
    
    public int GetPayloadLength() {
        return payload_length;
    }
    
    public byte GetNextHeader() {
        return next_header;
    }
    
    public byte GetHopLimit() {
        return hop_limit;
    }
    
    public byte[] GetSourceAddress() {
        return source_address;
    }
    
    public byte[] GetDestinationAddress() {
        return destination_address;
    }
    
    public byte[] GetTransportLayerData() {
        return transport_layer_data;
    }
    
    @Override
    public void PrintTypeLengthValue() {
        super.PrintTypeLengthValue();
        
        Logger.d(String.format("version : 0x%x \n",  version));
        Logger.d(String.format("traffic_class : 0x%x \n", traffic_class));
        Logger.d(String.format("flow_label : 0x%x \n", flow_label));
        Logger.d(String.format("payload_length : 0x%x \n", payload_length));
        
        String header_type = "unknown";
        if ( next_header == 0x06 ) {
            header_type = "tcp";
        } else if ( next_header == 0x11 ) {
            header_type = "udp";
        }
        
        Logger.d(String.format("next_header : 0x%x (%s)\n", next_header, header_type));
        Logger.d(String.format("hop_limit : 0x%x \n", hop_limit));
        
        Logger.d(String.format("source_address : " +
                "%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x \n", 
                source_address[0], source_address[1],
                source_address[2], source_address[3], 
                source_address[4], source_address[5],
                source_address[6], source_address[7],
                source_address[8], source_address[9],
                source_address[10], source_address[11],
                source_address[12], source_address[12],
                source_address[14], source_address[15]));
        
        Logger.d(String.format("destination_address : " +
                "%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x \n", 
                destination_address[0], destination_address[1],
                destination_address[2], destination_address[3], 
                destination_address[4], destination_address[5],
                destination_address[6], destination_address[7],
                destination_address[8], destination_address[9],
                destination_address[10], destination_address[11],
                destination_address[12], destination_address[12],
                destination_address[14], destination_address[15]));
        
        if ( null != ntp ) {
            ntp.Print();
        }
        
        Logger.d(String.format("data_byte : \n" ));
        int j=1;
        Logger.p(String.format("%03d : ", j));
        for(int k=0; k<transport_layer_data.length; k++)
        {
            Logger.p(String.format("%02x ", transport_layer_data[k]));
            if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
    }
}
