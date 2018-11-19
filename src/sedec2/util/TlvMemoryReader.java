package sedec2.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TlvMemoryReader extends TlvReader {
    protected final int TLV_HEADER_LENGTH = 4;
    protected MappedByteBuffer memory_buffer = null;
    protected RandomAccessFile input_memory_stream = null;
    protected ByteArrayOutputStream output_stream = null;
    
    public TlvMemoryReader(String tlv_file) {
        super(tlv_file);
    }
    
    @Override
    public boolean open() {
        try {
            input_memory_stream = new RandomAccessFile(tlv_file, "r");
            memory_buffer = input_memory_stream.getChannel().map(
                    FileChannel.MapMode.READ_ONLY, 0, input_memory_stream.length());
            memory_buffer.load();
            return memory_buffer.isLoaded();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public void close() {
        memory_buffer.clear();
        memory_buffer = null;
        
        try {
            input_memory_stream.close();
            input_memory_stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int filesize()  {
        if ( memory_buffer == null ) return 0;
        return memory_buffer.capacity();
    }
    
    @Override
    public boolean readable() {
        if ( memory_buffer == null ) return false;
        return memory_buffer.remaining() > 0 ? true : false;
    }
    
    @Override
    public byte[] readPacket() {
        byte[] tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
        memory_buffer.get(tlv_header_buffer);
        
        byte[] tlv_payload_buffer = 
                new byte[((tlv_header_buffer[2] & 0xff) << 8 | (tlv_header_buffer[3] & 0xff))];
        memory_buffer.get(tlv_payload_buffer);
        
        try {
            output_stream = new ByteArrayOutputStream();
            output_stream.write(tlv_header_buffer);
            output_stream.write(tlv_payload_buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output_stream.toByteArray();
    }
}
