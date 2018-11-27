package sedec2.arib.tlv.container.packets;

import sedec2.arib.tlv.container.mmtp.MMTP_Packet;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

/**
 * Class to deliver Network Time Protocol Data.
 * NTP refers to Table 3-1 of chapter 3 of ARIB B60
 */
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
    protected MMTP_Packet mmtp_packet = null;

    /**
     * Constructor to decode IPv6 Packet
     * @param buffer one TLV raw buffer having synchronization byte
     */
    public IPv6Packet(byte[] buffer) {
        super(buffer);

        version = (byte) readOnBuffer(4);
        traffic_class = (byte) readOnBuffer(8);
        flow_label = readOnBuffer(20);
        payload_length = readOnBuffer(16);
        next_header = (byte) readOnBuffer(8);
        hop_limit = (byte) readOnBuffer(8);

        for ( int i=0; i<16; i++ ) {
            source_address[i] = (byte) readOnBuffer(8);
        }

        for ( int i=0; i<16; i++ ) {
            destination_address[i] = (byte) readOnBuffer(8);
        }

        transport_layer_data = new byte[payload_length];

        for ( int i=0; i<transport_layer_data.length; i++ ) {
            transport_layer_data[i] = (byte) readOnBuffer(8);
        }

        if ( next_header == 0x11 &&
                destination_address[14] == 0x01 && destination_address[15] == 0x01 ) {
            ntp = new NetworkTimeProtocolData(transport_layer_data);
        } else {
            mmtp_packet = new MMTP_Packet(transport_layer_data);
        }
    }

    public byte getVersion() {
        return version;
    }

    public byte getTrafficClass() {
        return traffic_class;
    }

    public int getFlowLabel() {
        return flow_label;
    }

    public int getPayloadLength() {
        return payload_length;
    }

    public byte getNextHeader() {
        return next_header;
    }

    public byte getHopLimit() {
        return hop_limit;
    }

    public byte[] getSourceAddress() {
        return source_address;
    }

    public byte[] getDestinationAddress() {
        return destination_address;
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
            ntp.print();
        }

        if ( null != mmtp_packet ) {
            mmtp_packet.print();
        }

        BinaryLogger.print(transport_layer_data);
    }
}
