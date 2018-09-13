package arib.b39.tables;

import java.util.ArrayList;
import java.util.List;

import base.Table;
import util.Logger;

public class AddressMapTable extends Table {
    protected int table_id_extension;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int num_of_service_id;
    protected List<Service> services = new ArrayList<>();

    class Service {
        public int service_id;
        public byte ip_version;
        public int service_loop_length;
        public IPv4 ipv4;
        public IPv6 ipv6;
        public byte[] private_data_byte;
    }
    
    class IPv4 {
        public byte[] src_address_32 = new byte[4];
        public byte src_address_mask_32;
        public byte[] dst_address_32 = new byte[4];
        public byte dst_address_mask_32;
    }
    
    class IPv6 {
        public byte[] src_address_128 = new byte[16];
        public byte src_address_mask_128;
        public byte[] dst_address_128 = new byte[16];
        public byte dst_address_mask_128;
    }
    
    public AddressMapTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        table_id_extension = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        num_of_service_id = ReadOnBuffer(10);
        SkipOnBuffer(6);
        
        for ( int i=0; i<num_of_service_id; i++) {
            Service service = new Service();
            service.service_id = ReadOnBuffer(16);
            service.ip_version = (byte) ReadOnBuffer(1);
            SkipOnBuffer(5);
            service.service_loop_length = ReadOnBuffer(10);
            
            for ( int k=service.service_loop_length; k>0; ) {
                if ( service.ip_version == 0 ) {
                    IPv4 ipv4 = new IPv4();
                    for ( int j=0; j<4; j++ ) 
                        ipv4.src_address_32[j] = (byte) ReadOnBuffer(8);
                    ipv4.src_address_mask_32 = (byte) ReadOnBuffer(8);
                    for ( int j=0; j<4; j++ ) 
                        ipv4.dst_address_32[j] = (byte) ReadOnBuffer(8);
                    ipv4.dst_address_mask_32 = (byte) ReadOnBuffer(8);
                    service.ipv4 = ipv4;
                    k-=10;
                } else if ( service.ip_version == 1 ) {
                    IPv6 ipv6 = new IPv6();
                    for ( int j=0; j<16; j++ ) 
                        ipv6.src_address_128[j] = (byte) ReadOnBuffer(8);
                    ipv6.src_address_mask_128 = (byte) ReadOnBuffer(8);
                    for ( int j=0; j<16; j++ ) 
                        ipv6.dst_address_128[j] = (byte) ReadOnBuffer(8);
                    ipv6.dst_address_mask_128 = (byte) ReadOnBuffer(8);
                    k-=34;
                }
                
                service.private_data_byte = new byte[k];
                for ( int n=0; n<k; n++ ) {
                    service.private_data_byte[n] = (byte) ReadOnBuffer(8);
                }
            }
            services.add(service);
        }
        
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("table_id_extension : 0x%x \n", table_id_extension));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("num_of_service_id : 0x%x \n", num_of_service_id));
        
        for ( int i=0; i<services.size(); i++ ) {
            Service service = services.get(i);
            Logger.d(String.format("\t [%d] service_id : 0x%x \n", i, service.service_id));
            Logger.d(String.format("\t [%d] ip_version : 0x%x \n", i, service.ip_version));
            Logger.d(String.format("\t [%d] service_loop_length : 0x%x \n", 
                    i, service.service_loop_length));
            
            if ( service.ip_version == 0 ) {
                Logger.d(String.format("\t [%d] ipv4.src_address_32 : %d.%d.%d.%d \n", 
                        i, service.ipv4.src_address_32[0], service.ipv4.src_address_32[1],
                        service.ipv4.src_address_32[2], service.ipv4.src_address_32[3]));
                Logger.d(String.format("\t [%d] ipv4.src_address_mask_32 : 0x%x \n", 
                        i, service.ipv4.src_address_mask_32));
            } else if (service.ip_version == 1) {
                Logger.d(String.format("\t [%d] ipv6.src_address_128 : "
                        + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                        i, service.ipv6.src_address_128[0], service.ipv6.src_address_128[1],
                        service.ipv6.src_address_128[2], service.ipv6.src_address_128[3], 
                        service.ipv6.src_address_128[4], service.ipv6.src_address_128[5],
                        service.ipv6.src_address_128[6], service.ipv6.src_address_128[7],
                        service.ipv6.src_address_128[8], service.ipv6.src_address_128[9],
                        service.ipv6.src_address_128[10], service.ipv6.src_address_128[11],
                        service.ipv6.src_address_128[12], service.ipv6.src_address_128[12],
                        service.ipv6.src_address_128[14], service.ipv6.src_address_128[15]));
                Logger.d(String.format("\t [%d] ipv6.src_address_mask_128 : 0x%x \n", 
                        i, service.ipv6.src_address_mask_128));
            }
            
            int j=1;
            Logger.p(String.format("%03d : ", j));
            for(int k=0; k<service.private_data_byte.length; k++)
            {
                Logger.p(String.format("%02x ", service.private_data_byte[k]));
                if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
