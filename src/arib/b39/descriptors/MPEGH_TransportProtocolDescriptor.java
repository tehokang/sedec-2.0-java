package arib.b39.descriptors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.BitReadWriter;
import util.Logger;

/**
 * @brief TransportProtocolDescriptor
 * @note Verified
 */
public class MPEGH_TransportProtocolDescriptor extends Descriptor {
    private static final int PROTOCOL_OBJECT_CAROUSEL = 0x0001;
    private static final int PROTOCOL_HTTP = 0x0003;
    private static final int PROTOCOL_DATA_CAROUSEL = 0x0004;
    private static final int PROTOCOL_MMT_NON_TIMED = 0x0005;
    
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
    
    public MPEGH_TransportProtocolDescriptor(BitReadWriter brw) {
        super(brw);
        
        protocol_id = brw.ReadOnBuffer(16);
        transport_protocol_label = (byte) brw.ReadOnBuffer(8);
        
        if ( 0 < descriptor_length-3 ) {
            switch ( protocol_id ) {
                case PROTOCOL_OBJECT_CAROUSEL:
                    transport.remote_connection = (byte) brw.ReadOnBuffer(1);
                    brw.SkipOnBuffer(7);
                    if ( 0x01 == transport.remote_connection ) {
                        transport.original_network_id = brw.ReadOnBuffer(16);
                        transport.transport_stream_id = brw.ReadOnBuffer(16);
                        transport.service_id = brw.ReadOnBuffer(16);
                    }
                    transport.component_tag = (byte) brw.ReadOnBuffer(8);
                    break;
                case PROTOCOL_HTTP:
                case PROTOCOL_MMT_NON_TIMED:
                    channel_transport.URL_base_length = (byte) brw.ReadOnBuffer(8);
                    for ( int i=0; i<channel_transport.URL_base_length; i++ ) {
                        channel_transport.URL_base_byte[i] = (byte) brw.ReadOnBuffer(8);
                    }
                    channel_transport.URL_extension_count = (byte) brw.ReadOnBuffer(8);
                    
                    for ( int i=0; i<channel_transport.URL_extension_count; i++ ) {
                        UrlExtension ext = new UrlExtension();
                        ext.URL_extension_length = (byte) brw.ReadOnBuffer(8);
                        for ( int j=0; j<ext.URL_extension_length; j++ ) {
                            ext.URL_exntension_byte[j] = (byte) brw.ReadOnBuffer(8);
                        }
                        url_extensions.add(ext);
                    }
                    break;
                case PROTOCOL_DATA_CAROUSEL:
                    transport.remote_connection = (byte) brw.ReadOnBuffer(1);
                    brw.SkipOnBuffer(7);
                    if ( 0x01 == transport.remote_connection ) {
                        transport.original_network_id = brw.ReadOnBuffer(16);
                        transport.transport_stream_id = brw.ReadOnBuffer(16);
                        transport.service_id = brw.ReadOnBuffer(16);
                    }
                    transport.component_tag = (byte) brw.ReadOnBuffer(8);
                    break;
            }
        }
    }

    public byte[] GetBaseUrl() { 
        return channel_transport.URL_base_byte;
    }
    
    public int GetProtocolId() {
        return protocol_id;
    }
    
    public byte GetTransportProtocolLabel() {
        return transport_protocol_label;
    }
    
    public byte GetRemoteConnection() {
        return transport.remote_connection;
    }
    
    public int GetOriginalNetworkId() {
        return transport.original_network_id;
    }
    
    public int GetTransportStreamId() {
        return transport.transport_stream_id;
    }
    
    public int GetServiceId() {
        return transport.service_id;
    }
    
    public byte GetComponentTag() {
        return transport.component_tag;
    }
    
    public void SetBaseUrl(byte[] base_url) {
        channel_transport.URL_base_length = (byte) Arrays.toString(base_url).length();
        System.arraycopy(base_url, 0, channel_transport.URL_base_byte, 0, 
                channel_transport.URL_base_length);
        channel_transport.URL_extension_count = 0;
    }
    
    public void SetProtocolId(int value) {
        protocol_id = value;
    }
    
    public void SetTransportProtocolLabel(byte value) {
        transport_protocol_label = value;
    }
    
    public void SetRemoteConnection(byte value) {
        transport.remote_connection = value;
    }
    
    public void SetOriginalNetworkId(int value) {
        transport.original_network_id = value;
    }
    
    public void SetTransportStreamId(int value) {
        transport.transport_stream_id = value;
    }
    
    public void SetServiceId(int value) {
        transport.service_id = value;
    }
    
    public void SetComponentTag(byte value) {
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
            case PROTOCOL_MMT_NON_TIMED:
                {
                    selector_byte_length = 1 + channel_transport.URL_base_length +
                                            1 + channel_transport.URL_extension_count;
                    
                    for ( int i=0; i<url_extensions.size(); i++ ) {
                        selector_byte_length += 1 + url_extensions.get(i).URL_extension_length;
                        
                    }
                }
                break;
            /**
             * @note It is related in IPTVFJ STD-0010 version 2.0
             **/
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
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(protocol_id, 16);
        brw.WriteOnBuffer(transport_protocol_label, 8);

        switch(protocol_id)
        {
            case PROTOCOL_OBJECT_CAROUSEL:
                brw.WriteOnBuffer(transport.remote_connection, 1);
                brw.WriteOnBuffer(0x7f, 7);
                if(0x01 == transport.remote_connection)
                {
                    brw.WriteOnBuffer(transport.original_network_id, 16);
                    brw.WriteOnBuffer(transport.transport_stream_id, 16);
                    brw.WriteOnBuffer(transport.service_id, 16);
                }
                brw.WriteOnBuffer(transport.component_tag, 8);
                break;
            case PROTOCOL_HTTP:
            case PROTOCOL_MMT_NON_TIMED:
                brw.WriteOnBuffer(channel_transport.URL_base_length, 8);
                for(int i=0;i<channel_transport.URL_base_length;i++)
                    brw.WriteOnBuffer(channel_transport.URL_base_byte[i], 8);

                brw.WriteOnBuffer(channel_transport.URL_extension_count, 8);
                
                for ( int i=0; i<url_extensions.size(); i++ ) { 
                    brw.WriteOnBuffer(url_extensions.get(i).URL_extension_length, 8);
                    for ( int j=0;j<url_extensions.get(i).URL_extension_length;j++ ) {
                        brw.WriteOnBuffer(url_extensions.get(i).URL_exntension_byte[j], 8);
                    }
                }
                break;
            /**
             * @note It is related in IPTVFJ STD-0010 version 2.0
             **/
            case PROTOCOL_DATA_CAROUSEL:
                brw.WriteOnBuffer(transport.remote_connection, 1);
                brw.WriteOnBuffer(0x7f, 7);
                if(0x01 == transport.remote_connection)
                {
                    brw.WriteOnBuffer(transport.original_network_id, 16);
                    brw.WriteOnBuffer(transport.transport_stream_id, 16);
                    brw.WriteOnBuffer(transport.service_id, 16);
                }
                brw.WriteOnBuffer(transport.component_tag, 8);
                break;
            default:
                break;
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t protocol_id : 0x%x \n", protocol_id));
        Logger.d(String.format("\t transport_protocol_label : 0x%x \n", transport_protocol_label));

        switch(protocol_id) {
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
            case PROTOCOL_MMT_NON_TIMED:
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
            /**
             * @note It is related in IPTVFJ STD-0010 version 2.0
             **/
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
        Logger.d("\n");
    }
}
