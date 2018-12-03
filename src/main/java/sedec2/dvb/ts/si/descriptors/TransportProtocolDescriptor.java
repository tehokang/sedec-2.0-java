package sedec2.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class TransportProtocolDescriptor extends Descriptor {
    private static final int PROTOCOL_OBJECT_CAROUSEL = 0x0001;
    private static final int PROTOCOL_HTTP = 0x0003;
    private static final int PROTOCOL_DATA_CAROUSEL = 0x0004;

    private int protocol_id;
    private byte transport_protocol_label;
    private Transport transport = new Transport();
    private List<UrlExtension> url_extensions = new ArrayList<>();
    private ChannelTransport channel_transport = new ChannelTransport();

    class Transport {
        public byte remote_connection;
        public int original_network_id;
        public int transport_stream_id;
        public int service_id;
        public byte component_tag;
    }

    class UrlExtension {
        public byte URL_extension_length;
        public byte[] URL_exntension_byte = new byte[256];
    }

    class ChannelTransport {
        public byte URL_base_length;
        public byte[] URL_base_byte = new byte[256];
        public byte URL_extension_count;
    }

    public TransportProtocolDescriptor(BitReadWriter brw) {
        super(brw);

        protocol_id = brw.readOnBuffer(16);
        transport_protocol_label = (byte) brw.readOnBuffer(8);

        if ( 0 < descriptor_length-3 ) {
            switch ( protocol_id ) {
                case PROTOCOL_OBJECT_CAROUSEL:
                    transport.remote_connection = (byte) brw.readOnBuffer(1);
                    brw.skipOnBuffer(7);
                    if ( 0x01 == transport.remote_connection ) {
                        transport.original_network_id = brw.readOnBuffer(16);
                        transport.transport_stream_id = brw.readOnBuffer(16);
                        transport.service_id = brw.readOnBuffer(16);
                    }
                    transport.component_tag = (byte) brw.readOnBuffer(8);
                    break;
                case PROTOCOL_HTTP:
                    channel_transport.URL_base_length = (byte) brw.readOnBuffer(8);
                    for ( int i=0; i<channel_transport.URL_base_length; i++ ) {
                        channel_transport.URL_base_byte[i] = (byte) brw.readOnBuffer(8);
                    }
                    channel_transport.URL_extension_count = (byte) brw.readOnBuffer(8);

                    for ( int i=0; i<channel_transport.URL_extension_count; i++ ) {
                        UrlExtension ext = new UrlExtension();
                        ext.URL_extension_length = (byte) brw.readOnBuffer(8);
                        for ( int j=0; j<ext.URL_extension_length; j++ ) {
                            ext.URL_exntension_byte[j] = (byte) brw.readOnBuffer(8);
                        }
                        url_extensions.add(ext);
                    }
                    break;
                case PROTOCOL_DATA_CAROUSEL:
                    transport.remote_connection = (byte) brw.readOnBuffer(1);
                    brw.skipOnBuffer(7);
                    if ( 0x01 == transport.remote_connection ) {
                        transport.original_network_id = brw.readOnBuffer(16);
                        transport.transport_stream_id = brw.readOnBuffer(16);
                        transport.service_id = brw.readOnBuffer(16);
                    }
                    transport.component_tag = (byte) brw.readOnBuffer(8);
                    break;
            }
        }
    }

    public byte[] getBaseUrl() {
        return channel_transport.URL_base_byte;
    }

    public int getProtocolId() {
        return protocol_id;
    }

    public byte getTransportProtocolLabel() {
        return transport_protocol_label;
    }

    public byte getRemoteConnection() {
        return transport.remote_connection;
    }

    public int getOriginalNetworkId() {
        return transport.original_network_id;
    }

    public int getTransportStreamId() {
        return transport.transport_stream_id;
    }

    public int getServiceId() {
        return transport.service_id;
    }

    public byte getComponentTag() {
        return transport.component_tag;
    }

    public void setBaseUrl(byte[] base_url) {
        channel_transport.URL_base_length = (byte) Arrays.toString(base_url).length();
        System.arraycopy(base_url, 0, channel_transport.URL_base_byte, 0,
                channel_transport.URL_base_length);
        channel_transport.URL_extension_count = 0;
    }

    public void setProtocolId(int value) {
        protocol_id = value;
    }

    public void setTransportProtocolLabel(byte value) {
        transport_protocol_label = value;
    }

    public void setRemoteConnection(byte value) {
        transport.remote_connection = value;
    }

    public void setOriginalNetworkId(int value) {
        transport.original_network_id = value;
    }

    public void setTransportStreamId(int value) {
        transport.transport_stream_id = value;
    }

    public void setServiceId(int value) {
        transport.service_id = value;
    }

    public void setComponentTag(byte value) {
        transport.component_tag = value;
    }

    @Override
    protected void updateDescriptorLength() {
        int selector_byte_length = 0;
        switch ( protocol_id )
        {
            case PROTOCOL_OBJECT_CAROUSEL:
                if ( 0x01 == transport.remote_connection )
                    selector_byte_length = 8;
                else
                    selector_byte_length = 2;
                break;
            case PROTOCOL_HTTP:
                {
                    selector_byte_length = 1 + channel_transport.URL_base_length +
                                            1 + channel_transport.URL_extension_count;

                    for ( int i=0; i<url_extensions.size(); i++ ) {
                        selector_byte_length += 1 + url_extensions.get(i).URL_extension_length;

                    }
                }
                break;
            case PROTOCOL_DATA_CAROUSEL:
                if(0x01 == transport.remote_connection)
                    selector_byte_length = 8;
                else
                    selector_byte_length = 2;
                break;
            default:
                break;
        }
        descriptor_length = 3 + selector_byte_length;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        brw.writeOnBuffer(protocol_id, 16);
        brw.writeOnBuffer(transport_protocol_label, 8);

        switch(protocol_id)
        {
            case PROTOCOL_OBJECT_CAROUSEL:
                brw.writeOnBuffer(transport.remote_connection, 1);
                brw.writeOnBuffer(0x7f, 7);
                if(0x01 == transport.remote_connection)
                {
                    brw.writeOnBuffer(transport.original_network_id, 16);
                    brw.writeOnBuffer(transport.transport_stream_id, 16);
                    brw.writeOnBuffer(transport.service_id, 16);
                }
                brw.writeOnBuffer(transport.component_tag, 8);
                break;
            case PROTOCOL_HTTP:
                brw.writeOnBuffer(channel_transport.URL_base_length, 8);
                for(int i=0;i<channel_transport.URL_base_length;i++)
                    brw.writeOnBuffer(channel_transport.URL_base_byte[i], 8);

                brw.writeOnBuffer(channel_transport.URL_extension_count, 8);

                for ( int i=0; i<url_extensions.size(); i++ ) {
                    brw.writeOnBuffer(url_extensions.get(i).URL_extension_length, 8);
                    for ( int j=0;j<url_extensions.get(i).URL_extension_length;j++ ) {
                        brw.writeOnBuffer(url_extensions.get(i).URL_exntension_byte[j], 8);
                    }
                }
                break;
            case PROTOCOL_DATA_CAROUSEL:
                brw.writeOnBuffer(transport.remote_connection, 1);
                brw.writeOnBuffer(0x7f, 7);
                if(0x01 == transport.remote_connection)
                {
                    brw.writeOnBuffer(transport.original_network_id, 16);
                    brw.writeOnBuffer(transport.transport_stream_id, 16);
                    brw.writeOnBuffer(transport.service_id, 16);
                }
                brw.writeOnBuffer(transport.component_tag, 8);
                break;
            default:
                break;
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t protocol_id : 0x%x \n", protocol_id));
        Logger.d(String.format("\t transport_protocol_label : 0x%x \n", transport_protocol_label));

        switch(protocol_id)
        {
            case PROTOCOL_OBJECT_CAROUSEL:
                {
                    Logger.d(String.format("\t remote_connection : 0x%x \n",
                            transport.remote_connection));;
                    if(0x01 == transport.remote_connection)
                    {
                        Logger.d(String.format("\t original_network_id : 0x%x \n",
                                transport.original_network_id));
                        Logger.d(String.format("\t transport_stream_id : 0x%x \n",
                                transport.transport_stream_id));
                        Logger.d(String.format("\t service_id : 0x%x \n",
                                transport.service_id));
                    }
                    Logger.d(String.format("\t component_tag : 0x%x \n",
                            transport.component_tag));
                }
                break;
            case PROTOCOL_HTTP:
                {
                    Logger.d(String.format("\t URL_base_length : 0x%x \n",
                            channel_transport.URL_base_length));
                    Logger.d(String.format("\t URL_base_byte : %s \n",
                            new String(channel_transport.URL_base_byte)));
                    Logger.d(String.format("\t URL_extension_count : 0x%x \n",
                            channel_transport.URL_extension_count));

                    for ( int i=0;i<url_extensions.size(); i++ ) {
                        Logger.d(String.format("\t managed_URL_length : 0x%x \n",
                                url_extensions.get(i).URL_extension_length));
                        Logger.d(String.format("\t managed_URL_byte : %s \n",
                                new String(url_extensions.get(i).URL_exntension_byte)));
                    }
                }
                break;
            case PROTOCOL_DATA_CAROUSEL:
                {
                    Logger.d(String.format("\t remote_connection : 0x%x \n",
                            transport.remote_connection));
                    if(0x01 == transport.remote_connection)
                    {
                        Logger.d(String.format("\t original_network_id : 0x%x \n",
                                transport.original_network_id));
                        Logger.d(String.format("\t transport_stream_id : 0x%x \n",
                                transport.transport_stream_id));
                        Logger.d(String.format("\t service_id : 0x%x \n",
                                transport.service_id));
                    }
                    Logger.d(String.format("\t component_tag : 0x%x \n",
                            transport.component_tag));
                }
                break;
            default:
                break;
        }
    }

}
