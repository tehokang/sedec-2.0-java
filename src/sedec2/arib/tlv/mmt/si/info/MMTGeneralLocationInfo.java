package sedec2.arib.tlv.mmt.si.info;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MMTGeneralLocationInfo {
    protected byte location_type;
    protected LocationType0x00 type0 = new LocationType0x00();
    protected LocationType0x01 type1 = new LocationType0x01();
    protected LocationType0x02 type2 = new LocationType0x02();
    protected LocationType0x03 type3 = new LocationType0x03();
    protected LocationType0x04 type4 = new LocationType0x04();
    protected LocationType0x05 type5 = new LocationType0x05();
    protected int length = 0;
    
    class LocationType0x00 {
        public int packet_id;
    }
    
    class LocationType0x01 {
        public byte[] ipv4_src_addr = new byte[4];
        public byte[] ipv4_dst_addr = new byte[4];
        public int dst_port;
        public int packet_id;
    }
    
    class LocationType0x02 {
        public byte[] ipv6_src_addr = new byte[16];
        public byte[] ipv6_dst_addr = new byte[16];
        public int dst_port;
        public int packet_id;
    }
    
    class LocationType0x03 {
        public int network_id;
        public int MPEG_2_transport_stream_id;
        public int MPEG_2_PID;
    }
    
    class LocationType0x04 {
        public byte[] ipv6_src_addr = new byte[16];
        public byte[] ipv6_dst_addr = new byte[16];
        public int dst_port;
        public int MPEG_2_PID;
    }
    
    class LocationType0x05 {
        public byte URL_length;
        public byte[] URL_byte;
    }
    
    public MMTGeneralLocationInfo(BitReadWriter brw) {
        location_type = (byte) brw.readOnBuffer(8);
        
        switch ( location_type ) {
            case 0x00:
                type0.packet_id = brw.readOnBuffer(16);
                length = 2;
                break;
            case 0x01:
                for ( int i=0; i<4; i++ ) 
                    type1.ipv4_src_addr[i] = (byte) brw.readOnBuffer(8);
                for ( int i=0; i<4; i++ )
                    type1.ipv4_dst_addr[i] = (byte) brw.readOnBuffer(8);
                type1.dst_port = brw.readOnBuffer(16);
                type1.packet_id = brw.readOnBuffer(16);
                length=12;
                break;
            case 0x02:
                for ( int j=0; j<16; j++ ) 
                    type2.ipv6_src_addr[j] = (byte) brw.readOnBuffer(8);
                for ( int j=0; j<16; j++ ) 
                    type2.ipv6_dst_addr[j] = (byte) brw.readOnBuffer(8);
                type2.dst_port = brw.readOnBuffer(16);
                type2.packet_id = brw.readOnBuffer(16);
                length=36;
                break;
            case 0x03:
                type3.network_id = brw.readOnBuffer(16);
                type3.MPEG_2_transport_stream_id = brw.readOnBuffer(16);
                brw.skipOnBuffer(3);
                type3.MPEG_2_PID = brw.readOnBuffer(13);
                length=6;
                break;
            case 0x04:
                for ( int j=0; j<16; j++ ) 
                    type4.ipv6_src_addr[j] = (byte) brw.readOnBuffer(8);
                for ( int j=0; j<16; j++ ) 
                    type4.ipv6_dst_addr[j] = (byte) brw.readOnBuffer(8);
                type4.dst_port = brw.readOnBuffer(16);
                brw.skipOnBuffer(3);
                type4.MPEG_2_PID = brw.readOnBuffer(13);
                length=36;
                break;
            case 0x05:
                type5.URL_length = (byte) brw.readOnBuffer(8);
                type5.URL_byte = new byte[type5.URL_length];
                for ( int j=0; j<type5.URL_length; j++ ) {
                    type5.URL_byte[j] = (byte) brw.readOnBuffer(8);
                }
                length=(8+type5.URL_length);
                break;
        }
    }
    
    public void print() {
        Logger.d(String.format("--- MMT_general_location_info --- (%s)\n", getClass().getName()));
        Logger.d(String.format("\t location_type : 0x%x \n", location_type));
        
        switch ( location_type ) {
            case 0x00:
                Logger.d(String.format("\t type0.packet_id : 0x%x \n", type0.packet_id));
                break;
            case 0x01:
                Logger.d(String.format("\t type1.ipv4_src_addr : %d.%d.%d.%d \n",
                        type1.ipv4_src_addr[0], type1.ipv4_src_addr[1], 
                        type1.ipv4_src_addr[2], type1.ipv4_src_addr[3]));
                Logger.d(String.format("\t type1.ipv4_dst_addr : %d.%d.%d.%d \n",
                        type1.ipv4_dst_addr[0], type1.ipv4_dst_addr[1], 
                        type1.ipv4_dst_addr[2], type1.ipv4_dst_addr[3]));
                Logger.d(String.format("\t type1.dst_port : 0x%x \n", type1.dst_port));
                Logger.d(String.format("\t type1.packet_id : 0x%x \n", type1.packet_id));
                break;
            case 0x02:
                Logger.d(String.format("\t type2.ipv6_src_addr : "
                        + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                        type2.ipv6_src_addr[0], type2.ipv6_src_addr[1],
                        type2.ipv6_src_addr[2], type2.ipv6_src_addr[3], 
                        type2.ipv6_src_addr[4], type2.ipv6_src_addr[5],
                        type2.ipv6_src_addr[6], type2.ipv6_src_addr[7],
                        type2.ipv6_src_addr[8], type2.ipv6_src_addr[9],
                        type2.ipv6_src_addr[10], type2.ipv6_src_addr[11],
                        type2.ipv6_src_addr[12], type2.ipv6_src_addr[12],
                        type2.ipv6_src_addr[14], type2.ipv6_src_addr[15]));
                
                Logger.d(String.format("\t type2.ipv6_src_addr : "
                        + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                        type2.ipv6_dst_addr[0], type2.ipv6_dst_addr[1],
                        type2.ipv6_dst_addr[2], type2.ipv6_dst_addr[3], 
                        type2.ipv6_dst_addr[4], type2.ipv6_dst_addr[5],
                        type2.ipv6_dst_addr[6], type2.ipv6_dst_addr[7],
                        type2.ipv6_dst_addr[8], type2.ipv6_dst_addr[9],
                        type2.ipv6_dst_addr[10], type2.ipv6_dst_addr[11],
                        type2.ipv6_dst_addr[12], type2.ipv6_dst_addr[12],
                        type2.ipv6_dst_addr[14], type2.ipv6_dst_addr[15]));
                Logger.d(String.format("\t type2.dst_port : 0x%x \n", type2.dst_port));
                Logger.d(String.format("\t type2.packet_id : 0x%x \n", type2.packet_id));
                break;
            case 0x03:
                Logger.d(String.format("\t type3.network_id : 0x%x \n", type3.network_id));
                Logger.d(String.format("\t type3.MPEG_2_transport_stream_id : 0x%x \n", 
                        type3.MPEG_2_transport_stream_id));
                Logger.d(String.format("\t type3.MPEG_2_PID : 0x%x \n", type3.MPEG_2_PID));
                break;
            case 0x04:
                Logger.d(String.format("\t type2.ipv6_src_addr : "
                        + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                        type4.ipv6_src_addr[0], type4.ipv6_src_addr[1],
                        type4.ipv6_src_addr[2], type4.ipv6_src_addr[3], 
                        type4.ipv6_src_addr[4], type4.ipv6_src_addr[5],
                        type4.ipv6_src_addr[6], type4.ipv6_src_addr[7],
                        type4.ipv6_src_addr[8], type4.ipv6_src_addr[9],
                        type4.ipv6_src_addr[10], type4.ipv6_src_addr[11],
                        type4.ipv6_src_addr[12], type4.ipv6_src_addr[12],
                        type4.ipv6_src_addr[14], type4.ipv6_src_addr[15]));
                
                Logger.d(String.format("\t type2.ipv6_src_addr : "
                        + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                        type4.ipv6_dst_addr[0], type4.ipv6_dst_addr[1],
                        type4.ipv6_dst_addr[2], type4.ipv6_dst_addr[3], 
                        type4.ipv6_dst_addr[4], type4.ipv6_dst_addr[5],
                        type4.ipv6_dst_addr[6], type4.ipv6_dst_addr[7],
                        type4.ipv6_dst_addr[8], type4.ipv6_dst_addr[9],
                        type4.ipv6_dst_addr[10], type4.ipv6_dst_addr[11],
                        type4.ipv6_dst_addr[12], type4.ipv6_dst_addr[12],
                        type4.ipv6_dst_addr[14], type4.ipv6_dst_addr[15]));
                Logger.d(String.format("\t type4.dst_port : 0x%x \n", type4.dst_port));
                Logger.d(String.format("\t type4.MPEG_2_PID : 0x%x \n", type4.MPEG_2_PID));
                break;
            case 0x05:
                Logger.d(String.format("\t type5.URL_length : 0x%x \n", type5.URL_length));
                Logger.d(String.format("\t type5.URL_byte : %s \n", new String(type5.URL_byte)));
                break;
        }
    }
    
    public byte getLocationType() {
        return location_type;
    }
    
    public LocationType0x00 getType0() {
        return type0;
    }
    
    public LocationType0x01 getType1() {
        return type1;
    }
    
    public LocationType0x02 getType2() {
        return type2;
    }
    
    public LocationType0x03 getType3() {
        return type3;
    }
    
    public LocationType0x04 getType4() {
        return type4;
    }
    
    public LocationType0x05 getType5() {
        return type5;
    }
    
    public int getLength() {
        return length;
    }
}
