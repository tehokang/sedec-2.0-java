package sedec2.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TlvFileReader {
    protected File tlv_file = null;
    protected final int TLV_HEADER_LENGTH = 4;
    protected DataInputStream input_stream  = null;
    protected ByteArrayOutputStream output_stream = null;
    
    public TlvFileReader(String tlv_file) {
        this.tlv_file = new File(tlv_file);
    }
    
    public boolean open() {
        try {
            input_stream  = 
                    new DataInputStream(
                            new BufferedInputStream(new FileInputStream(tlv_file)));
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
    
    public void close() throws IOException {
        if ( input_stream != null ) input_stream.close();
    }
    
    public int filesize() throws IOException {
        if ( input_stream == null ) return 0;
        return input_stream.available();
    }
    
    public boolean readable() throws IOException {
        if ( input_stream == null ) return false;
        return input_stream.available() > 0 ? true : false;
    }
    
    public byte[] readPacket() throws IOException {
        /**
         * @note Making a packet of TLV which has a sync byte as beginning
         * In other words, user should put a perfect TLV packet with sync byte into. 
         */
        byte[] tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
        input_stream.read(tlv_header_buffer, 0, tlv_header_buffer.length);  
        
        byte[] tlv_payload_buffer = 
                new byte[((tlv_header_buffer[2] & 0xff) << 8 | (tlv_header_buffer[3] & 0xff))];
        input_stream.read(tlv_payload_buffer, 0, tlv_payload_buffer.length);

        output_stream = new ByteArrayOutputStream();
        output_stream.write(tlv_header_buffer);
        output_stream.write(tlv_payload_buffer);
        return output_stream.toByteArray();
    }
    
    public List<byte[]> readPackets(int packet_count) throws IOException {
        List<byte[]> tlv_packets = new ArrayList<>();
        
        for ( int i=0; i<packet_count; i++ ) {
            byte[] tlv_packet = readPacket();
            tlv_packets.add(tlv_packet);
        }
        return tlv_packets;
    }
}
