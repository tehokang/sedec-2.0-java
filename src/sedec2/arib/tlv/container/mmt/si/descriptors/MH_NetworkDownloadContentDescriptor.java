package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.arib.tlv.container.mmt.si.DescriptorFactory;
import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MH_NetworkDownloadContentDescriptor extends Descriptor {
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

    public class IPv4 {
        public byte[] address = new byte[4];
        public int port_number;
    }

    public class IPv6 {
        public byte[] address = new byte[16];
        public int port_number;
    }

    public MH_NetworkDownloadContentDescriptor(BitReadWriter brw) {
        super(brw);

        reboot = (byte) brw.readOnBuffer(1);
        add_on = (byte) brw.readOnBuffer(1);
        compatibility_flag = (byte) brw.readOnBuffer(1);
        text_info_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(4);
        component_size = brw.readOnBuffer(32);
        session_protocol_number = (byte) brw.readOnBuffer(8);
        session_id = brw.readOnBuffer(32);
        retry = (byte) brw.readOnBuffer(8);
        connect_timer = brw.readOnBuffer(24);
        address_type = (byte) brw.readOnBuffer(8);

        if ( address_type == 0x00 ) {
            for ( int j=0; j<4; j++ )
                ipv4.address[j] = (byte) brw.readOnBuffer(8);
            ipv4.port_number = brw.readOnBuffer(16);
        }

        if ( address_type == 0x01 ) {
            for ( int j=0; j<16; j++ )
                ipv6.address[j] = (byte) brw.readOnBuffer(8);
            ipv6.port_number = brw.readOnBuffer(16);
        }

        if ( address_type == 0x02 ) {
            URL_length = (byte) brw.readOnBuffer(8);
            URL_byte = new byte[URL_length];

            for ( int i=0; i<URL_byte.length; i++ ) {
                URL_byte[i] = (byte) brw.readOnBuffer(8);
            }
        }

        if ( compatibility_flag == 1 ) {
            compatibilityDescriptor = DescriptorFactory.createDescriptor(brw);
        }

        private_data_length = (byte) brw.readOnBuffer(8);
        private_data_byte = new byte[private_data_length];

        for ( int i=0; i<private_data_length; i++ ) {
            private_data_byte[i] = (byte) brw.readOnBuffer(8);
        }

        if ( text_info_flag == 1 ) {
            ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

            text_length = (byte) brw.readOnBuffer(8);
            text_char = new byte[text_length];

            for ( int i=0; i<text_char.length; i++ ) {
                text_char[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public byte getReboot() {
        return reboot;
    }

    public byte getAddOn() {
        return add_on;
    }

    public byte getCompatibilityFlag() {
        return compatibility_flag;
    }

    public byte getTextInfoFlag() {
        return text_info_flag;
    }

    public int getComponentSize() {
        return component_size;
    }

    public byte getSessionProtocolNumber() {
        return session_protocol_number;
    }

    public int getSessionId() {
        return session_id;
    }

    public byte getRetry() {
        return retry;
    }

    public int getConnectTimer() {
        return connect_timer;
    }

    public byte getAddressType() {
        return address_type;
    }

    public IPv4 getIpv4() {
        return ipv4;
    }

    public IPv6 getIpv6() {
        return ipv6;
    }

    public byte getURLLength() {
        return URL_length;
    }

    public byte[] getURLByte() {
        return URL_byte;
    }

    public Descriptor getCompatibilityDescriptor() {
        return compatibilityDescriptor;
    }

    public byte getPrivateDataLength() {
        return private_data_length;
    }

    public byte[] getPrivateDataByte() {
        return private_data_byte;
    }

    public byte[] getISO639LanguageCode() {
        return ISO_639_language_code;
    }

    public byte getTextLength() {
        return text_length;
    }

    public byte[] getTextChar() {
        return text_char;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t reboot : 0x%x \n", reboot));
        Logger.d(String.format("\t add_on : 0x%x \n", add_on));
        Logger.d(String.format("\t compatibility_flag : 0x%x \n", compatibility_flag));
        Logger.d(String.format("\t text_info_flag : 0x%x \n", text_info_flag));
        Logger.d(String.format("\t component_size : 0x%x \n", component_size));
        Logger.d(String.format("\t session_protocol_number : 0x%x \n", session_protocol_number));
        Logger.d(String.format("\t session_id : 0x%x \n", session_id));
        Logger.d(String.format("\t retry : 0x%x \n", retry));
        Logger.d(String.format("\t connect_timer : 0x%x \n", connect_timer));
        Logger.d(String.format("\t address_type : 0x%x \n", address_type));

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
            compatibilityDescriptor.print();
        }

        Logger.d(String.format("\t private_data_length : 0x%x \n", private_data_length));
        Logger.d(String.format("\t private_data_byte : \n"));
        BinaryLogger.print(private_data_byte);

        if ( text_info_flag == 1 ) {
            Logger.d(String.format("\t ISO_639_language_code : %s \n",
                    new String(ISO_639_language_code)));

            Logger.d(String.format("\t text_length : 0x%x \n", text_length));
            Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 15;

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
            descriptor_length += compatibilityDescriptor.getDescriptorLength();
        }

        descriptor_length += (1 + private_data_byte.length);

        if ( text_info_flag == 1 ) {
            descriptor_length += (4 + text_char.length);
        }
    }
}
