package sedec2.arib.tlv.mmt.mmtp;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

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
    protected MMTP_Payload_MPU mmtp_payload_mpu = null;
    protected MMTP_Payload_SignallingMessage mmtp_payload_signalling_message = null;
    
    class HeaderExtensionByte {
        public byte hdr_ext_end_flag;
        public int hdr_ext_type;
        public int hdr_ext_length;
        public byte[] hdr_ext_byte;
    }
    
    public MMTP_Payload_MPU getMPU() {
        return mmtp_payload_mpu;
    }
    
    public MMTP_Payload_SignallingMessage getSignallingMessage() {
        return mmtp_payload_signalling_message;
    }
    
    public byte getVersion() {
        return version;
    }
    
    public byte getPacketCounterFlag() {
        return packet_counter_flag;
    }
    
    public byte getFECType() {
        return FEC_type;
    }
    
    public byte getExtensionFlag() {
        return extension_flag;
    }
    
    public byte getRAPFlag() {
        return RAP_flag;
    }
    
    public byte getPayloadType() {
        return payload_type;
    }
    
    public int getPacketId() {
        return packet_id;
    }
    
    public int getTimestamp() {
        return timestamp;
    }
    
    public int getPacketSequenceNumber() {
        return packet_sequence_number;
    }
    
    public int getPacketCounter() {
        return packet_counter;
    }
    
    public int getExtensionType() {
        return extension_type;
    }
    
    public int getExtensionLength() {
        return extension_length;
    }
    
    public MMTP_Packet(byte[] buffer) {
        super(buffer);
        
        version = (byte) readOnBuffer(2);
        packet_counter_flag = (byte) readOnBuffer(1);
        FEC_type = (byte) readOnBuffer(2);
        skipOnBuffer(1);
        extension_flag = (byte) readOnBuffer(1);
        RAP_flag = (byte) readOnBuffer(1);
        skipOnBuffer(2);
        payload_type = (byte) readOnBuffer(6);
        packet_id = readOnBuffer(16);
        timestamp = readOnBuffer(32);
        packet_sequence_number = readOnBuffer(32);
        
        if ( packet_counter_flag == 0x01 ) {
            packet_counter = readOnBuffer(32);
        }
        
        if ( extension_flag == 0x01 ) {
            extension_type = readOnBuffer(16);
            extension_length = readOnBuffer(16);
            
            if ( extension_type == 0x0000 ) {
                for ( int i=extension_length; i>0; ) {
                    HeaderExtensionByte heb = new HeaderExtensionByte();
                    heb.hdr_ext_end_flag = (byte) readOnBuffer(1);
                    heb.hdr_ext_type = readOnBuffer(15);
                    heb.hdr_ext_length = readOnBuffer(16);
                    heb.hdr_ext_byte = new byte[heb.hdr_ext_length];
                    i-=4;
                    
                    for ( int j=0; j<heb.hdr_ext_byte.length; j++ ) {
                        heb.hdr_ext_byte[j] = (byte) readOnBuffer(8);
                        i-=1;
                    }
                    header_extension_bytes.add(heb);
                }
            } else {
                HeaderExtensionByte heb = new HeaderExtensionByte();
                heb.hdr_ext_byte = new byte[extension_length];
                for ( int j=0; j<heb.hdr_ext_byte.length; j++ ) {
                    heb.hdr_ext_byte[j] = (byte) readOnBuffer(8);
                }
            }
        }
        
        if ( payload_type == 0x00 ) {
            mmtp_payload_mpu = new MMTP_Payload_MPU(this);
        } else if ( payload_type == 0x02 ) {
            switch ( packet_id ) {
                case 0x0000:
                case 0x0001:
                case 0x8000:
                case 0x8001:
                case 0x8002:
                case 0x8003:
                case 0x8004:
                case 0x8005:
                case 0x8006:
                case 0x8007:
                    mmtp_payload_signalling_message = new MMTP_Payload_SignallingMessage(this);        
                    break;
                default:
                    Logger.d(String.format("Unknown packet_id (0x%x)\n", packet_id));
                    break;
            }
        }
    }
    
    public void print() {
        Logger.d(String.format("======= MMTP Packet ======= (%s)\n", getClass().getName()));
        Logger.d(String.format("version : 0x%x \n", version));
        Logger.d(String.format("packet_counter_flag : 0x%x \n", packet_counter_flag));
        Logger.d(String.format("FEC_type : 0x%x \n", FEC_type));
        Logger.d(String.format("extension_flag : 0x%x \n", extension_flag));
        Logger.d(String.format("RAP_flag : 0x%x \n", RAP_flag));
        Logger.d(String.format("payload_type : 0x%x \n", payload_type));
        Logger.d(String.format("packet_id : 0x%x \n", packet_id));
        Logger.d(String.format("timestamp : 0x%x \n", timestamp));
        Logger.d(String.format("packet_sequence_number : 0x%x \n", packet_sequence_number));
        
        if ( packet_counter_flag == 0x01 ) {
            Logger.d(String.format("packet_counter : 0x%x \n", packet_counter));
        }
        
        if ( extension_flag == 0x01 ) {
            Logger.d(String.format("extension_type : 0x%x \n", extension_type));
            Logger.d(String.format("extension_length : 0x%x \n", extension_length));
            
            for ( int i=0; i<header_extension_bytes.size(); i++ ) {
                HeaderExtensionByte heb = header_extension_bytes.get(i);
                Logger.d(String.format("[%d] hdr_ext_end_flag : 0x%x \n", 
                        i, heb.hdr_ext_end_flag));
                Logger.d(String.format("[%d] hdr_ext_type : 0x%x \n", 
                        i, heb.hdr_ext_type));
                Logger.d(String.format("[%d] hdr_ext_length : 0x%x \n", 
                        i, heb.hdr_ext_length));
                Logger.d(String.format("[%d] hdr_ext_byte : \n", i));
                
                BinaryLogger.print(heb.hdr_ext_byte);
            }
        }
        
        if ( payload_type == 0x00 && mmtp_payload_mpu != null ) {
            mmtp_payload_mpu.print();
        } else if (payload_type == 0x02 && mmtp_payload_signalling_message != null ) {
            switch ( packet_id ) {
                case 0x0000:
                case 0x0001:
                case 0x8000:
                case 0x8001:
                case 0x8002:
                case 0x8003:
                case 0x8004:
                case 0x8005:
                case 0x8006:
                case 0x8007:
                    mmtp_payload_signalling_message.print();
                    break;
                default:
                    Logger.d(String.format("Unknown packet_id (0x%x)\n", packet_id));
                    break;
            }
        }
    }
}
