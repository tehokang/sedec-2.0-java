package sedec2.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class TlvReader {
    protected File tlv_file = null;
    
    public TlvReader(String tlv_file) {
        this.tlv_file = new File(tlv_file);
    }
    
    public abstract boolean open();
    
    public void close() {
        tlv_file = null;
    }
    
    public abstract int filesize();
    
    public abstract boolean readable();
    
    public abstract byte[] readPacket();
    
    public List<byte[]> readPackets(int packet_count) {
        List<byte[]> tlv_packets = new ArrayList<>();
        
        for ( int i=0; i<packet_count; i++ ) {
            byte[] tlv_packet = readPacket();
            tlv_packets.add(tlv_packet);
        }
        return tlv_packets;
    }
}
