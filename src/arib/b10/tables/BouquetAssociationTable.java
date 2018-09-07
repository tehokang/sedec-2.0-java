package arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import arib.b10.DescriptorFactory;
import arib.b10.descriptors.Descriptor;
import base.Table;
import util.Logger;

public class BouquetAssociationTable extends Table {
    protected int bouquet_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int bouquet_descriptors_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    protected int transport_stream_loop_length;
    protected List<TransportStream> transport_streams = new ArrayList<>();
    
    class TransportStream {
        public int transport_stream_id;
        public int original_network_id;
        public int transport_descriptors_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    public BouquetAssociationTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        bouquet_id = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        SkipOnBuffer(4);
        bouquet_descriptors_length = ReadOnBuffer(12);
        
        for ( int i=bouquet_descriptors_length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
        
        SkipOnBuffer(4);
        transport_stream_loop_length = ReadOnBuffer(12);
        for ( int i=transport_stream_loop_length; i>0; ) {
            TransportStream stream = new TransportStream();
            stream.transport_stream_id = ReadOnBuffer(16);
            stream.original_network_id = ReadOnBuffer(16);
            SkipOnBuffer(4);
            stream.transport_descriptors_length = ReadOnBuffer(12);
            
            for ( int j=stream.transport_descriptors_length; j>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
                j-=desc.GetDescriptorLength();
                stream.descriptors.add(desc);
            }
            
            transport_streams.add(stream);
            i-= ( 6 + stream.transport_descriptors_length );
        }
        
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("bouquet_id : 0x%x \n", bouquet_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", 
                current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", 
                last_section_number));
        Logger.d(String.format("bouquet_descriptors_length : 0x%x \n", 
                bouquet_descriptors_length));
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }
        
        Logger.d(String.format("transport_stream_loop_length : 0x%x \n", 
                transport_stream_loop_length));
        
        for ( int i=0; i<transport_streams.size(); i++ ) {
            TransportStream stream = transport_streams.get(i);
            Logger.d(String.format("\t[%d] transport_stream_id : 0x%x \n", 
                    i, stream.transport_stream_id));
            Logger.d(String.format("\t[%d] original_network_id : 0x%x \n", 
                    i, stream.original_network_id));
            Logger.d(String.format("\t[%d] transport_descriptors_length : 0x%x \n", 
                    i, stream.transport_descriptors_length));
            
            for ( int j=0; j<stream.descriptors.size(); j++ ) {
                stream.descriptors.get(j).PrintDescriptor();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%x%x%x%x \n", 
                ((checksum_CRC32 >> 24) & 0xff), 
                ((checksum_CRC32 >> 16) & 0xff), 
                ((checksum_CRC32 >> 8) & 0xff), 
                (checksum_CRC32 & 0xff)));
    }
}
