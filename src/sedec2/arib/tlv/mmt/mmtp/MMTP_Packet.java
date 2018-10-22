package sedec2.arib.tlv.mmt.mmtp;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;

public class MMTP_Packet extends BitReadWriter {
    protected byte version;
    protected byte packet_counter_flag;
    protected byte FEC_type;
    protected byte extension_flag;
    protected byte RAP_flag;
    protected byte payload_type;
    protected int packet_id;
    protected int timestamp;
    protected int packet_sequence_number;
    protected int packet_counter;
    protected int extension_type;
    protected int extension_length;
    protected List<HeaderExtensionByte> header_extension_bytes = new ArrayList<>(); 
    protected MMTP_Payload_Type00 mmtp_payload_type00 = null;
    protected MMTP_Payload_Type02 mmtp_payload_type02 = null;
    
    class HeaderExtensionByte {
        public byte hdr_ext_end_flag;
        public int hdr_ext_type;
        public int hdr_ext_length;
        public byte[] hdr_ext_byte;
    }
    
    public MMTP_Packet(byte[] buffer) {
        super(buffer);
        
        version = (byte) ReadOnBuffer(2);
        packet_counter_flag = (byte) ReadOnBuffer(1);
        FEC_type = (byte) ReadOnBuffer(2);
        SkipOnBuffer(1);
        extension_flag = (byte) ReadOnBuffer(1);
        RAP_flag = (byte) ReadOnBuffer(1);
        SkipOnBuffer(2);
        payload_type = (byte) ReadOnBuffer(6);
        packet_id = ReadOnBuffer(16);
        timestamp = ReadOnBuffer(32);
        packet_sequence_number = ReadOnBuffer(32);
        
        if ( packet_counter_flag == 0x01 ) {
            packet_counter = ReadOnBuffer(32);
        }
        
        if ( extension_flag == 0x01 ) {
            extension_type = ReadOnBuffer(16);
            extension_length = ReadOnBuffer(16);
            
            for ( int i=extension_length; i>0; ) {
                HeaderExtensionByte heb = new HeaderExtensionByte();
                heb.hdr_ext_end_flag = (byte) ReadOnBuffer(1);
                heb.hdr_ext_type = ReadOnBuffer(15);
                heb.hdr_ext_length = ReadOnBuffer(16);
                heb.hdr_ext_byte = new byte[heb.hdr_ext_length];
                i-=4;
                
                for ( int j=0; j<heb.hdr_ext_byte.length; j++ ) {
                    heb.hdr_ext_byte[j] = (byte) ReadOnBuffer(8);
                    i-=1;
                }
            }
        }
        
        if ( payload_type == 0x00 ) {
            mmtp_payload_type00 = new MMTP_Payload_Type00(this);
        } else if (payload_type == 0x01 ) {
            mmtp_payload_type02 = new MMTP_Payload_Type02(this);
        }
    }
}
