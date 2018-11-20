package sedec2.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TlvMemoryReader extends TlvReader {
    protected MappedByteBuffer memory_buffer = null;
    protected RandomAccessFile input_stream = null;
    
    public TlvMemoryReader(String tlv_file) {
        super(tlv_file);
    }
    
    @Override
    public boolean open() {
        try {
            Logger.d(String.format("TlvMemoryReader opened (%s) \n",
                    tlv_file.getName()));

            input_stream = new RandomAccessFile(tlv_file, "r");
            memory_buffer = input_stream.getChannel().map(
                    FileChannel.MapMode.READ_ONLY, 0, input_stream.length());
            memory_buffer.load();
            return memory_buffer.isLoaded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void close() {
        super.close();
        
        if ( memory_buffer != null ) {
            memory_buffer.clear();
            memory_buffer = null;
        }
        
        try {
            input_stream.close();
            input_stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean readable() {
        if ( memory_buffer == null ) return false;
        return memory_buffer.remaining() > 0 ? true : false;
    }
    
    @Override
    public int readPacket(byte[] tlv_packet) {
        byte[] tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
        memory_buffer.get(tlv_header_buffer);
        
        byte[] tlv_payload_buffer = 
                new byte[((tlv_header_buffer[2] & 0xff) << 8 | 
                        (tlv_header_buffer[3] & 0xff))];
        memory_buffer.get(tlv_payload_buffer);
        
        output_buffer = ByteBuffer.wrap(tlv_packet);
        output_buffer.put(tlv_header_buffer);
        output_buffer.put(tlv_payload_buffer);
        return tlv_header_buffer.length + tlv_payload_buffer.length;
    }
    
    @Override
    public byte[] readPacket() {
        byte[] tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
        memory_buffer.get(tlv_header_buffer);
        
        byte[] tlv_payload_buffer = 
                new byte[((tlv_header_buffer[2] & 0xff) << 8 | 
                        (tlv_header_buffer[3] & 0xff))];
        memory_buffer.get(tlv_payload_buffer);
        
        output_buffer = ByteBuffer.allocate(
                tlv_header_buffer.length + tlv_payload_buffer.length);
        output_buffer.put(tlv_header_buffer);
        output_buffer.put(tlv_payload_buffer);
        return output_buffer.array();
    }
}
