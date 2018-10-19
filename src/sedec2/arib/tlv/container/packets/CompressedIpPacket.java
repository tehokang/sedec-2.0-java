package sedec2.arib.tlv.container.packets;

import sedec2.util.Logger;

public class CompressedIpPacket extends TypeLengthValue {
    protected int CID;
    protected byte SN;
    protected byte CID_header_type;
    protected IPv4HeaderWoLength ipv4_header_wo_length = new IPv4HeaderWoLength();
    protected UDPHeaderWoLength udp_header_wo_length = new UDPHeaderWoLength();
    protected IPv6HeaderWoLength ipv6_header_wo_length = new IPv6HeaderWoLength();
    protected int identification;
    protected PacketDataByte packet_data_byte = new PacketDataByte();
    
    class IPv4HeaderWoLength {
        public byte version;
        public byte IHL;
        public byte type_of_service;
        public int identification;
        public byte flags;
        public int fragment_offset;
        public byte time_to_live;
        public byte protocol;
        public byte[] source_address = new byte[4];
        public byte[] destination_address = new byte[4];
        
        public void Print() {
            Logger.d(String.format("version : 0x%x \n", version));
            Logger.d(String.format("IHL : 0x%x \n", IHL));
            Logger.d(String.format("type_of_service : 0x%x \n", type_of_service));
            Logger.d(String.format("identification : 0x%x \n", identification));
            Logger.d(String.format("flags : 0x%x \n", flags));
            Logger.d(String.format("fragment_offset : 0x%x \n", fragment_offset));
            Logger.d(String.format("time_to_live : 0x%x \n", time_to_live));
            Logger.d(String.format("protocol : 0x%x \n", protocol));
            
            Logger.d(String.format("\t [%d] source_address : %d.%d.%d.%d \n", 
                    source_address[0], source_address[1],
                    source_address[2], source_address[3]));
            
            Logger.d(String.format("\t [%d] destination_address : %d.%d.%d.%d \n", 
                    destination_address[0], destination_address[1],
                    destination_address[2], destination_address[3]));
        }
    }
    
    class IPv6HeaderWoLength {
        public byte version;
        public byte traffic_class;
        public int flow_label;
        public byte next_header;
        public byte hop_limit;
        public byte[] source_address = new byte[16];
        public byte[] destination_address = new byte[16];
        
        public void Print() {
            Logger.d(String.format("version : 0x%x \n", version));
            Logger.d(String.format("traffic_class : 0x%x \n", traffic_class));
            Logger.d(String.format("flow_label : 0x%x \n", flow_label));
            Logger.d(String.format("next_header : 0x%x \n", next_header));
            Logger.d(String.format("hop_limit : 0x%x \n", hop_limit));
            Logger.d(String.format("source_address : "
                    + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                    source_address[0], source_address[1],
                    source_address[2], source_address[3], 
                    source_address[4], source_address[5],
                    source_address[6], source_address[7],
                    source_address[8], source_address[9],
                    source_address[10], source_address[11],
                    source_address[12], source_address[12],
                    source_address[14], source_address[15]));
            
            Logger.d(String.format("destination_address : "
                    + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                    destination_address[0], destination_address[1],
                    destination_address[2], destination_address[3], 
                    destination_address[4], destination_address[5],
                    destination_address[6], destination_address[7],
                    destination_address[8], destination_address[9],
                    destination_address[10], destination_address[11],
                    destination_address[12], destination_address[12],
                    destination_address[14], destination_address[15]));
        }
    }
    
    class UDPHeaderWoLength {
        public int source_port;
        public int destination_port;
        
        public void Print() {
            Logger.d(String.format("source_port : 0x%x \n", source_port));
            Logger.d(String.format("destination_port : 0x%x \n", destination_port));
        }
    }
    
    class PacketDataByte {
        public byte[] data;
        
        public void Print() {
            Logger.d(String.format("packate_data_byte : \n" ));
            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<data.length; k++)
            {
                Logger.p(String.format("%02x ", data[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
        }
    }
    
    public CompressedIpPacket(byte[] buffer) {
        super(buffer);
        
        CID = ReadOnBuffer(12);
        SN = (byte) ReadOnBuffer(4);
        CID_header_type = (byte) ReadOnBuffer(8);
        
        if ( CID_header_type == 0x20 ) {
            ipv4_header_wo_length.version = (byte) ReadOnBuffer(4);
            ipv4_header_wo_length.IHL = (byte) ReadOnBuffer(4);
            ipv4_header_wo_length.type_of_service = (byte) ReadOnBuffer(8);
            ipv4_header_wo_length.identification = ReadOnBuffer(16);
            ipv4_header_wo_length.flags = (byte) ReadOnBuffer(3);
            ipv4_header_wo_length.fragment_offset = ReadOnBuffer(13);
            ipv4_header_wo_length.time_to_live = (byte) ReadOnBuffer(8);
            ipv4_header_wo_length.protocol = (byte) ReadOnBuffer(8);

            for ( int i=0; i<4; i++ ) 
                ipv4_header_wo_length.source_address[i] = (byte) ReadOnBuffer(8);
            
            for ( int i=0; i<4; i++ ) 
                ipv4_header_wo_length.destination_address[i] = (byte) ReadOnBuffer(8);
            
            udp_header_wo_length.source_port = ReadOnBuffer(16);
            udp_header_wo_length.destination_port = ReadOnBuffer(16);
            
            packet_data_byte.data = new byte[length-3-20];
            for ( int i=0; i<packet_data_byte.data.length; i++ ) {
                packet_data_byte.data[i] = (byte) ReadOnBuffer(8);
            }
            
        } else if ( CID_header_type == 0x21 ) {
            identification = ReadOnBuffer(16);
            packet_data_byte.data = new byte[length-3-2];
            for ( int i=0; i<packet_data_byte.data.length; i++ ) {
                packet_data_byte.data[i] = (byte) ReadOnBuffer(8);
            }
            
        } else if ( CID_header_type == 0x60 ) {
            ipv6_header_wo_length.version = (byte) ReadOnBuffer(4);
            ipv6_header_wo_length.traffic_class = (byte) ReadOnBuffer(8);
            ipv6_header_wo_length.flow_label = ReadOnBuffer(20);
            ipv6_header_wo_length.next_header = (byte) ReadOnBuffer(8);
            ipv6_header_wo_length.hop_limit = (byte) ReadOnBuffer(8);
            
            for ( int i=0; i<16; i++ ) 
                ipv6_header_wo_length.source_address[i] = (byte) ReadOnBuffer(8);
            
            for ( int i=0; i<16; i++ ) 
                ipv6_header_wo_length.destination_address[i] = (byte) ReadOnBuffer(8);
            
            udp_header_wo_length.source_port = ReadOnBuffer(16);
            udp_header_wo_length.destination_port = ReadOnBuffer(16);
            
            packet_data_byte.data = new byte[length-3-6-16-16-4];
            for ( int i=0; i<packet_data_byte.data.length; i++ ) {
                packet_data_byte.data[i] = (byte) ReadOnBuffer(8);
            }
        } else if ( CID_header_type == 0x61 ) {
            packet_data_byte.data = new byte[length-3];
            for ( int i=0; i<packet_data_byte.data.length; i++ ) {
                packet_data_byte.data[i] = (byte) ReadOnBuffer(8);
            }
        }
    }

    @Override
    public void PrintTypeLengthValue() {
        super.PrintTypeLengthValue();
        
        Logger.d(String.format("CID : 0x%x \n", CID));
        Logger.d(String.format("SN : 0x%x \n", SN));
        Logger.d(String.format("CID_header_type : 0x%x \n", CID_header_type));
        
        if ( CID_header_type == 0x20 ) {
            ipv4_header_wo_length.Print();
            udp_header_wo_length.Print();
            packet_data_byte.Print();
        } else if ( CID_header_type == 0x21 ) {
            Logger.d(String.format("Identification : 0x%x \n", identification));
            packet_data_byte.Print();
        } else if ( CID_header_type == 0x60 ) {
            ipv6_header_wo_length.Print();
            udp_header_wo_length.Print();
            packet_data_byte.Print();
        } else if ( CID_header_type == 0x61 ) {
            packet_data_byte.Print();
        }
    }
}
