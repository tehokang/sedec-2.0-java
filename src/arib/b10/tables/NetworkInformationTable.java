package arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import arib.b10.descriptors.Descriptor;
import arib.b10.DescriptorFactory;
import base.Table;
import util.Logger;

public class NetworkInformationTable extends Table {
    protected int network_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int network_descriptors_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    protected int transport_stream_loop_length;
    protected List<TransportStream> transport_streams = new ArrayList<>();
    
    class TransportStream {
        public int transport_stream_id;
        public int original_network_id;
        public int transport_descriptors_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    public NetworkInformationTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public int GetNetworkId() {
        return network_id;
    }
    
    public byte GetVersionNumber() {
        return version_number;
    }
    
    public byte GetCurrentNextIndicator() {
        return current_next_indicator;
    }
    
    public byte GetSectionNumber() {
        return section_number;
    }
    
    public byte GetLastSectionNumber() {
        return last_section_number;                
    }
    
    public List<Descriptor> GetNetworkDescriptors() {
        return descriptors;
    }
    
    public int GetTLVStreamLoopLength() {
        return transport_stream_loop_length;
    }
    
    public List<TransportStream> GetTLVStreams() {
        return transport_streams;
    }
    
    @Override
    protected void __decode_table_body__() {
        network_id = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        SkipOnBuffer(4);
        network_descriptors_length = ReadOnBuffer(12);
        
        for ( int i=network_descriptors_length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
        SkipOnBuffer(4);
        transport_stream_loop_length = ReadOnBuffer(12);
        
        for ( int i=transport_stream_loop_length; i>0; ) {
            TransportStream transport_stream = new TransportStream();
            transport_stream.transport_stream_id = ReadOnBuffer(16);
            transport_stream.original_network_id = ReadOnBuffer(16);
            SkipOnBuffer(4);
            transport_stream.transport_descriptors_length = ReadOnBuffer(12);
            
            for ( int j=transport_stream.transport_descriptors_length; j>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
                j-=desc.GetDescriptorLength();
                descriptors.add(desc);                
            }
            transport_streams.add(transport_stream);
        }
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("network_id : 0x%x \n", network_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("network_descriptors_length : 0x%x \n", 
                network_descriptors_length));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            Descriptor desc = descriptors.get(i);
            desc.PrintDescriptor();
        }
        
        Logger.d(String.format("TLV_stream_loop_length : 0x%x \n", transport_stream_loop_length));
        
        for ( int i=0; i<transport_streams.size(); i++ ) {
            TransportStream transport_stream = transport_streams.get(i);
            Logger.d(String.format("\t[%d] tlv_stream_id : 0x%x \n", i, 
                    transport_stream.transport_stream_id));
            Logger.d(String.format("\t[%d] original_network_id : 0x%x \n", i, 
                    transport_stream.original_network_id));
            Logger.d(String.format("\t[%d] tlv_stream_descriptors_length : 0x%x \n", 
                    i, transport_stream.transport_descriptors_length));
            
            for (int j=0; j<transport_stream.descriptors.size(); j++ ) {
                Descriptor desc = transport_stream.descriptors.get(j);
                desc.PrintDescriptor();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
