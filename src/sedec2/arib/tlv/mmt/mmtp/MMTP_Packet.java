package sedec2.arib.tlv.mmt.mmtp;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
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
    protected List<HeaderExtensionByte> header_extension_byte = new ArrayList<>();
    protected MMTP_Payload_MPU mmtp_payload_mpu = null;
    protected MMTP_Payload_SignallingMessage mmtp_payload_signalling_message = null;
    
    public class HeaderExtensionByte {
        public byte hdr_ext_end_flag;
        public int hdr_ext_type;
        public int hdr_ext_length;
        
        /**
         * @note hdr_ext_type == 0x0001
         */
        public byte scramble_control;
        /**
         * @note hdr_ext_type == 0x0002
         * Please refer to Table 10-2 of ARIB B60
         */
        public int download_id;
        /**
         * @note hdr_ext_type == 0x0003
         * Please refer to Table 10-3 of ARIB B60
         */
        public int item_fragment_number;
        public int last_item_fragment_number;
    }
    
    public List<HeaderExtensionByte> getHeaderExtensionByte() {
        return header_extension_byte;
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
                    HeaderExtensionByte heb00 = new HeaderExtensionByte();
                    heb00.hdr_ext_end_flag = (byte) readOnBuffer(1);
                    heb00.hdr_ext_type = readOnBuffer(15);
                    heb00.hdr_ext_length = readOnBuffer(16);
                    
                    if ( heb00.hdr_ext_type == 0x0001 ) {
                        heb00.scramble_control = (byte) readOnBuffer(8);
                    } else if ( heb00.hdr_ext_type == 0x0002 ) {
                        heb00.download_id = readOnBuffer(32);
                    } else if ( heb00.hdr_ext_type == 0x0003 ) {
                        heb00.item_fragment_number = readOnBuffer(32);
                        heb00.last_item_fragment_number = readOnBuffer(32);
                    }
                    i-= (4 + heb00.hdr_ext_length);
                    header_extension_byte.add(heb00);
                }
            } 
        }
        
        if ( payload_type == 0x00 ) {
            mmtp_payload_mpu = new MMTP_Payload_MPU(this);
            
        } else if ( payload_type == 0x02 ) {
            mmtp_payload_signalling_message = new MMTP_Payload_SignallingMessage(this);        
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
            
            if ( extension_type == 0x0000 ) {
                for ( int i=0; i<header_extension_byte.size(); i++ ) {
                    HeaderExtensionByte heb = header_extension_byte.get(i);
                    Logger.d(String.format("[%d] hdr_ext_end_flag : 0x%x \n", 
                            i, heb.hdr_ext_end_flag));
                    Logger.d(String.format("[%d] hdr_ext_type : 0x%x \n", 
                            i, heb.hdr_ext_type));
                    Logger.d(String.format("[%d] hdr_ext_length : 0x%x \n", 
                            i, heb.hdr_ext_length));
                    
                    if ( heb.hdr_ext_type == 0x0001 ) {
                        Logger.d(String.format("[%d] scramble_control : 0x%x \n", 
                                i, heb.scramble_control));
                    } else if ( heb.hdr_ext_type == 0x0002 ) {
                        Logger.d(String.format("[%d] download_id : 0x%x \n", 
                                i, heb.download_id));
                    } else if ( heb.hdr_ext_type == 0x0003 ) {
                        Logger.d(String.format("[%d] item_fragment_number : 0x%x \n", 
                                i, heb.item_fragment_number));
                        Logger.d(String.format("[%d] last_item_fragment_number : 0x%x \n", 
                                i, heb.last_item_fragment_number));
                    }
                }
            }
        }
        
        if ( payload_type == 0x00 && mmtp_payload_mpu != null ) {
            mmtp_payload_mpu.print();
            
        } else if (payload_type == 0x02 && mmtp_payload_signalling_message != null ) {
            mmtp_payload_signalling_message.print();
        }
    }
}
