package arib.b39.descriptors;

import arib.b39.DescriptorFactory;
import base.BitReadWriter;
import util.Logger;

public class MPEGH_NetworkDownloadContentDescriptor extends Descriptor {
    protected byte reboot;
    protected byte add_on;
    protected byte compatibility_flag;
    protected byte text_info_flag;
    protected int component_size;
    protected byte session_protocol_number;
    protected int session_id;
    protected byte retry;
    protected int connect_timer;
    protected byte address_type;
    
    protected IPv4 ipv4 = new IPv4();
    protected IPv6 ipv6 = new IPv6();
    
    protected byte URL_length;
    protected byte[] URL_byte;
    protected Descriptor compatibilityDescriptor;
    protected byte private_data_length;
    protected byte[] private_data_byte;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte text_length;
    protected byte[] text_char;
    
    class IPv4 {
        public byte[] address = new byte[4];
        public int port_number;   
    }
    
    class IPv6 {
        public byte[] address = new byte[16];
        public int port_number;   
    }
    
    public MPEGH_NetworkDownloadContentDescriptor(BitReadWriter brw) {
        super(brw);
        
        reboot = (byte) brw.ReadOnBuffer(1);
        add_on = (byte) brw.ReadOnBuffer(1);
        compatibility_flag = (byte) brw.ReadOnBuffer(1);
        text_info_flag = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(4);
        component_size = brw.ReadOnBuffer(32);
        session_protocol_number = (byte) brw.ReadOnBuffer(8);
        session_id = brw.ReadOnBuffer(32);
        retry = (byte) brw.ReadOnBuffer(8);
        
        if ( address_type == 0x00 ) {
            for ( int j=0; j<4; j++ ) 
                ipv4.address[j] = (byte) brw.ReadOnBuffer(8);
            ipv4.port_number = brw.ReadOnBuffer(16);
        }
        
        if ( address_type == 0x01 ) {
            for ( int j=0; j<16; j++ ) 
                ipv6.address[j] = (byte) brw.ReadOnBuffer(8);
            ipv6.port_number = brw.ReadOnBuffer(16);
        }
        
        if ( address_type == 0x02 ) {
            URL_length = (byte) brw.ReadOnBuffer(8);
            URL_byte = new byte[URL_length];
            
            for ( int i=0; i<URL_length; i++ ) {
                URL_byte[i] = (byte) brw.ReadOnBuffer(8);
            }
        }
        
        if ( compatibility_flag == 1 ) {
            compatibilityDescriptor = (Descriptor) DescriptorFactory.CreateDescriptor(brw);
        }
        
        private_data_length = (byte) brw.ReadOnBuffer(8);
        private_data_byte = new byte[private_data_length];
        
        for ( int i=0; i<private_data_length; i++ ) {
            private_data_byte[i] = (byte) brw.ReadOnBuffer(8);
        }
        
        if ( text_info_flag == 1 ) {
            ISO_639_language_code[0] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code[1] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code[2] = (byte) brw.ReadOnBuffer(8);
            
            text_length = (byte) brw.ReadOnBuffer(8);
            text_char = new byte[text_length];
            
            for ( int i=0; i<text_length; i++ ) {
                text_char[i] = (byte) brw.ReadOnBuffer(8);
            }
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t reboot : 0x%x \n", reboot)); 
        Logger.d(String.format("\t add_on : 0x%x \n", add_on));
        Logger.d(String.format("\t compatibility_flag : 0x%x \n", compatibility_flag));
        Logger.d(String.format("\t text_info_flag : 0x%x \n", text_info_flag));
        Logger.d(String.format("\t component_size : 0x%x \n", component_size));
        Logger.d(String.format("\t session_protocol_number : 0x%x \n", session_protocol_number)); 
        Logger.d(String.format("\t session_id : 0x%x \n", session_id));
        Logger.d(String.format("\t retry : 0x%x \n", retry));
        
        if ( address_type == 0x00 ) {
            Logger.d(String.format("\t ipv4.address : %d.%d.%d.%d \n", 
                    ipv4.address[1], ipv4.address[1], ipv4.address[2], ipv4.address[3]));
            Logger.d(String.format("\t ipv4.port_number : 0x%x \n", ipv4.port_number));
        }
        
        if ( address_type == 0x01 ) {
            Logger.d(String.format("\t [%d] ipv6.address : "
                    + "%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x:%x%x \n", 
                    ipv6.address[0], ipv6.address[1],
                    ipv6.address[2], ipv6.address[3], 
                    ipv6.address[4], ipv6.address[5],
                    ipv6.address[6], ipv6.address[7],
                    ipv6.address[8], ipv6.address[9],
                    ipv6.address[10], ipv6.address[11],
                    ipv6.address[12], ipv6.address[12],
                    ipv6.address[14], ipv6.address[15]));
            Logger.d(String.format("\t ipv6.port_number : 0x%x \n", ipv6.port_number));
        }
        
        if ( address_type == 0x02 ) {
            Logger.d(String.format("\t URL_length : 0x%x \n", URL_length)); 
            Logger.d(String.format("\t URL_byte : 0x%x \n", new String(URL_byte))); 
        }
        
        if ( compatibility_flag == 1 ) {
            compatibilityDescriptor.PrintDescriptor();
        }
        
        Logger.d(String.format("\t private_data_length : 0x%x \n", private_data_length));
        int j=1;
        Logger.p(String.format("%03d : ", j));
        for(int k=0; k<private_data_byte.length; k++)
        {
            Logger.p(String.format("%02x ", private_data_byte[k]));
            if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
        
        if ( text_info_flag == 1 ) {
            Logger.d(String.format("\t ISO_639_language_code : %s \n", 
                    new String(ISO_639_language_code)));
            
            Logger.d(String.format("\t text_length : 0x%x \n", text_length));
            Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 16;
        
        if ( address_type == 0x00 ) {
            descriptor_length += 6;
        }
        
        if ( address_type == 0x01 ) {
            descriptor_length += 18;
        }
        
        if ( address_type == 0x02 ) {
            descriptor_length += (1 + URL_byte.length);
        }
        
        if ( compatibility_flag == 1 ) {
            descriptor_length += compatibilityDescriptor.GetDescriptorLength();
        }
        
        descriptor_length += (1 + private_data_byte.length);
        
        if ( text_info_flag == 1 ) {
            descriptor_length += (4 + text_char.length);
        }
    }
}
