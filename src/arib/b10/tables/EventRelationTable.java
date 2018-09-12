package arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import arib.b10.DescriptorFactory;
import arib.b10.descriptors.Descriptor;
import base.Table;
import util.Logger;

/**
 * @brief ARIB-B10 ERT
 */
public class EventRelationTable extends Table {
    protected int event_relation_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int information_provider_id;
    protected byte relation_type;
    protected List<EventRelation> event_relations = new ArrayList<>();
    
    class EventRelation {
        public int node_id;
        public byte collection_mode;
        public int parent_node_id;
        public byte reference_number;
        public int descriptors_loop_length;
        public List<Descriptor> descriptors = new ArrayList<>();
        
    }
    
    public EventRelationTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        event_relation_id = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = (byte) ReadOnBuffer(5);
        current_next_indicator = (byte) ReadOnBuffer(1);
        section_number = (byte) ReadOnBuffer(8);
        last_section_number = (byte) ReadOnBuffer(8);
        information_provider_id = ReadOnBuffer(16);
        relation_type = (byte) ReadOnBuffer(4);
        SkipOnBuffer(4);
        
        for ( int i=(section_length - 8 - 4); i>0; ) {
            EventRelation event_relation = new EventRelation();
            event_relation.node_id = ReadOnBuffer(16);
            event_relation.collection_mode = (byte) ReadOnBuffer(4);
            SkipOnBuffer(4);
            event_relation.parent_node_id = ReadOnBuffer(16);
            event_relation.reference_number = (byte) ReadOnBuffer(8);
            SkipOnBuffer(4);
            event_relation.descriptors_loop_length = ReadOnBuffer(12);
            
            for ( int j=event_relation.descriptors_loop_length; j>0; ) {
                Descriptor desc = 
                        (Descriptor) DescriptorFactory.CreateDescriptor(this);
                j-=desc.GetDescriptorLength();
                event_relation.descriptors.add(desc);
            }
            event_relations.add(event_relation);
        }
        
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("event_relation_id : 0x%x \n", event_relation_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", 
                current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", 
                last_section_number));
        Logger.d(String.format("information_provider_id : 0x%x \n", 
                information_provider_id));
        Logger.d(String.format("relation_type : 0x%x \n", relation_type));
        
        for ( int i=0; i<event_relations.size(); i++ ) {
            EventRelation event_relation = event_relations.get(i);
            Logger.d(String.format("\t [%d] node_id : 0x%x \n", 
                    i, event_relation.node_id));
            Logger.d(String.format("\t [%d] collection_mode : 0x%x \n",
                    i, event_relation.collection_mode));
            Logger.d(String.format("\t [%d] parent_node_id : 0x%x \n", 
                    i, event_relation.parent_node_id));
            Logger.d(String.format("\t [%d] reference_number : 0x%x \n", 
                    i, event_relation.reference_number));
            Logger.d(String.format("\t [%d] descriptors_loop_length : 0x%x \n", 
                    i, event_relation.descriptors_loop_length));
            
            for ( int j=0; i<event_relation.descriptors.size(); j++ ) {
                event_relation.descriptors.get(j).PrintDescriptor();
            }
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }

}
