package sedec2.arib.tlv.container.packets;

import sedec2.arib.tlv.mmt.mmtp.MMTP_Packet;
import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class CompressedIpPacket extends TypeLengthValue {
    protected int CID;
    protected byte SN;
    protected byte CID_header_type;
    protected IPv4HeaderWoLength ipv4_header_wo_length = new IPv4HeaderWoLength();
    protected UDPHeaderWoLength udp_header_wo_length = new UDPHeaderWoLength();
    protected IPv6HeaderWoLength ipv6_header_wo_length = new IPv6HeaderWoLength();
    protected int identification;
    protected PacketData packet_data_byte = null;
    
    public class IPv4HeaderWoLength {
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
        
        public void print() {
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
    
    public class IPv6HeaderWoLength {
        public byte version;
        public byte traffic_class;
        public int flow_label;
        public byte next_header;
        public byte hop_limit;
        public byte[] source_address = new byte[16];
        public byte[] destination_address = new byte[16];
        
        public void print() {
            Logger.d(String.format("version : 0x%x \n", version));
            Logger.d(String.format("traffic_class : 0x%x \n", traffic_class));
            Logger.d(String.format("flow_label : 0x%x \n", flow_label));
            
            String header_type = "unknown";
            if ( next_header == 0x06 ) {
                header_type = "tcp";
            } else if ( next_header == 0x11 ) {
                header_type = "udp";
            }
            
            Logger.d(String.format("next_header : 0x%x (%s) \n", 
                    next_header, header_type));
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
    
    public class UDPHeaderWoLength {
        public int source_port;
        public int destination_port;
        
        public void print() {
            Logger.d(String.format("source_port : 0x%x \n", source_port));
            Logger.d(String.format("destination_port : 0x%x \n", destination_port));
        }
    }
    
    public class PacketData {
        public byte[] data;
        public MMTP_Packet mmtp_packet = null;
        
        public PacketData(int length, BitReadWriter brw) {
            data = new byte[length];
            for ( int i=0; i<data.length; i++ ) {
                data[i] = (byte) brw.readOnBuffer(8);
            }
            
            mmtp_packet = new MMTP_Packet(data);
        }
        
        public void print() {
            if ( mmtp_packet != null ) {
                mmtp_packet.print();
            }
            
            BinaryLogger.print(data);
        }
    }
    
    public CompressedIpPacket(byte[] buffer) {
        super(buffer);
        
        CID = readOnBuffer(12);
        SN = (byte) readOnBuffer(4);
        CID_header_type = (byte) readOnBuffer(8);
        
        if ( CID_header_type == 0x20 ) {
            ipv4_header_wo_length.version = (byte) readOnBuffer(4);
            ipv4_header_wo_length.IHL = (byte) readOnBuffer(4);
            ipv4_header_wo_length.type_of_service = (byte) readOnBuffer(8);
            ipv4_header_wo_length.identification = readOnBuffer(16);
            ipv4_header_wo_length.flags = (byte) readOnBuffer(3);
            ipv4_header_wo_length.fragment_offset = readOnBuffer(13);
            ipv4_header_wo_length.time_to_live = (byte) readOnBuffer(8);
            ipv4_header_wo_length.protocol = (byte) readOnBuffer(8);

            for ( int i=0; i<4; i++ ) 
                ipv4_header_wo_length.source_address[i] = (byte) readOnBuffer(8);
            
            for ( int i=0; i<4; i++ ) 
                ipv4_header_wo_length.destination_address[i] = (byte) readOnBuffer(8);
            
            udp_header_wo_length.source_port = readOnBuffer(16);
            udp_header_wo_length.destination_port = readOnBuffer(16);
            
            packet_data_byte = new PacketData(length-3-20, this);
            
        } else if ( CID_header_type == 0x21 ) {
            identification = readOnBuffer(16);
            packet_data_byte = new PacketData(length-3-2, this);
            
        } else if ( CID_header_type == 0x60 ) {
            ipv6_header_wo_length.version = (byte) readOnBuffer(4);
            ipv6_header_wo_length.traffic_class = (byte) readOnBuffer(8);
            ipv6_header_wo_length.flow_label = readOnBuffer(20);
            ipv6_header_wo_length.next_header = (byte) readOnBuffer(8);
            ipv6_header_wo_length.hop_limit = (byte) readOnBuffer(8);
            
            for ( int i=0; i<16; i++ ) 
                ipv6_header_wo_length.source_address[i] = (byte) readOnBuffer(8);
            
            for ( int i=0; i<16; i++ ) 
                ipv6_header_wo_length.destination_address[i] = (byte) readOnBuffer(8);
            
            udp_header_wo_length.source_port = readOnBuffer(16);
            udp_header_wo_length.destination_port = readOnBuffer(16);
            
            packet_data_byte = new PacketData(length-3-6-16-16-4, this);
            
        } else if ( CID_header_type == 0x61 ) {
            packet_data_byte = new PacketData(length-3, this);
        }
    }

    public int getCID() {
        return CID;
    }
    
    public byte getSN() {
        return SN;
    }
    
    public byte getCIDHeaderType() {
        return CID_header_type;
    }
    
    public IPv4HeaderWoLength getIpv4HeaderWoLength() {
        return ipv4_header_wo_length;
    }
    
    public UDPHeaderWoLength getUdpHeaderWoLength() {
        return udp_header_wo_length;
    }
    
    public IPv6HeaderWoLength getIpv6HeaderWoLength() {
        return ipv6_header_wo_length;
    }
    
    public int getIdentification() {
        return identification;
    }
    
    public PacketData getPacketData() {
        return packet_data_byte;
    }
    
    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("CID : 0x%x \n", CID));
        Logger.d(String.format("SN : 0x%x \n", SN));
        Logger.d(String.format("CID_header_type : 0x%x \n", CID_header_type));
        
        if ( CID_header_type == 0x20 ) {
            ipv4_header_wo_length.print();
            udp_header_wo_length.print();
            packet_data_byte.print();
        } else if ( CID_header_type == 0x21 ) {
            Logger.d(String.format("Identification : 0x%x \n", identification));
            packet_data_byte.print();
        } else if ( CID_header_type == 0x60 ) {
            ipv6_header_wo_length.print();
            udp_header_wo_length.print();
            packet_data_byte.print();
        } else if ( CID_header_type == 0x61 ) {
            packet_data_byte.print();
        }
    }
}
