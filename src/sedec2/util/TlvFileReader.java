package sedec2.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TlvFileReader extends TlvReader {
    protected final int TLV_HEADER_LENGTH = 4;
    protected DataInputStream input_stream  = null;
    protected ByteArrayOutputStream output_stream = null;
    protected RandomAccessFile input_memory_stream = null;
    protected MappedByteBuffer memory_buffer = null;
    
    public TlvFileReader(String tlv_file) {
        super(tlv_file);
    }
    
    public boolean open() {
        try {
            input_stream  = 
                    new DataInputStream(
                            new BufferedInputStream(new FileInputStream(tlv_file)));
            
            input_memory_stream = new RandomAccessFile(tlv_file, "rw");
            memory_buffer = input_memory_stream.getChannel().map(
                    FileChannel.MapMode.READ_ONLY, 0, 1024*1024*1024);
            memory_buffer.load();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public void close() {
        super.close();
        try {
            if ( input_stream != null ) { 
                input_stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int filesize() {
        try {
            if ( input_stream != null )
                return input_stream.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean readable() {
        
        try {
            if ( input_stream != null )
                return input_stream.available() > 0 ? true : false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public byte[] readPacket() {
        output_stream = new ByteArrayOutputStream();

        try {
            byte[] tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
            input_stream.read(tlv_header_buffer, 0, tlv_header_buffer.length);
            byte[] tlv_payload_buffer = 
                    new byte[((tlv_header_buffer[2] & 0xff) << 8 | (tlv_header_buffer[3] & 0xff))];
            input_stream.read(tlv_payload_buffer, 0, tlv_payload_buffer.length);

            output_stream.write(tlv_header_buffer);
            output_stream.write(tlv_payload_buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }  
        return output_stream.toByteArray();
    }
}
