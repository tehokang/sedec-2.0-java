package sedec2.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TlvFileReader extends TlvReader {
    protected DataInputStream input_stream  = null;
    protected ByteArrayOutputStream output_stream = null;
    
    public TlvFileReader(String tlv_file) {
        super(tlv_file);
    }
    
    public boolean open() {
        try {
            Logger.d(String.format("TlvFileReader opened (%s) \n",
                    tlv_file.getName()));
            input_stream  = 
                    new DataInputStream(
                            new BufferedInputStream(new FileInputStream(tlv_file)));
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
                input_stream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public long filesize() {
        return tlv_file.length();
    }
    
    @Override
    public boolean readable() {
        try {
            if ( input_stream != null ) {
                return input_stream.available() > 0 ? true : false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public byte[] readPacket() {
        output_stream = new ByteArrayOutputStream();

        try {
            byte[] tlv_header_buffer = new byte[TLV_HEADER_LENGTH];
            input_stream.read(tlv_header_buffer);
            
            byte[] tlv_payload_buffer = 
                    new byte[((tlv_header_buffer[2] & 0xff) << 8 | 
                            (tlv_header_buffer[3] & 0xff))];
            input_stream.read(tlv_payload_buffer);

            output_stream.write(tlv_header_buffer);
            output_stream.write(tlv_payload_buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }  
        return output_stream.toByteArray();
    }
}
