package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.util.Logger;

public class DataContentConfigurationTable extends Table {
    protected byte num_of_contents;
    protected List<Content> contents = new ArrayList<>();
    
    class Content {
        public int content_id;
        public byte content_version;
        public int content_size;
        public byte PU_info_flag;
        public byte content_info_flag;
        
        public byte number_of_PUs;
        public List<PU> pus = new ArrayList<>();
        
        public int number_of_nodes;
        public int[] node_tag;
        
        public byte content_descriptor_loop_length;
        public List<Descriptor> content_descriptors = new ArrayList<>();
    }
    
    class PU {
        public byte PU_tag;
        public int PU_size;
        public byte number_of_member_nodes;
        public int[] node_tag;
        public byte PU_descriptor_loop_length;
        public List<Descriptor> pu_desctiptors = new ArrayList<>();
    }
    
    public DataContentConfigurationTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }
    
    public byte getNumOfContents() {
        return num_of_contents;
    }
    
    public List<Content> getContents() {
        return contents;
    }

    @Override
    protected void __decode_table_body__() {
        num_of_contents = (byte) readOnBuffer(8);
        
        for ( int i=0; i<num_of_contents; i++ ) {
            Content content = new Content();
            content.content_id = readOnBuffer(16);
            content.content_version = (byte) readOnBuffer(8);
            content.content_size = readOnBuffer(32);
            content.PU_info_flag = (byte) readOnBuffer(1);
            content.content_info_flag = (byte) readOnBuffer(1);
            skipOnBuffer(6);
            
            if ( content.PU_info_flag == 1 ) {
                content.number_of_PUs = (byte) readOnBuffer(8);
                
                for ( int j=0; j<content.number_of_PUs; j++ ) {
                    PU pu = new PU();
                    pu.PU_tag = (byte) readOnBuffer(8);
                    pu.PU_size = readOnBuffer(32);
                    pu.number_of_member_nodes = (byte) readOnBuffer(8);
                    
                    for ( int k=0; k<pu.number_of_member_nodes; k++ ) {
                        pu.node_tag[k] = readOnBuffer(16);
                    }
                    pu.PU_descriptor_loop_length = (byte) readOnBuffer(8);
                    for ( int k=pu.PU_descriptor_loop_length; k>0; ) {
                        Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
                        k-=desc.getDescriptorLength();
                        pu.pu_desctiptors.add(desc);
                    }
                    content.pus.add(pu);
                }
            } else {
                content.number_of_nodes = readOnBuffer(16);
                for ( int j=0; j<content.number_of_nodes; j++ ) {
                    content.node_tag[j] = readOnBuffer(16);
                }
            }
            
            if ( content.content_info_flag == 1 ) {
                content.content_descriptor_loop_length = (byte) readOnBuffer(8);
                for ( int j=content.content_descriptor_loop_length; j>0; ) {
                    Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
                    j-=desc.getDescriptorLength();
                    content.content_descriptors.add(desc);
                }
            }
            contents.add(content);
        }
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("num_of_contents : 0x%x \n", num_of_contents));
        
        for ( int i=0; i<contents.size(); i++ ) {
            Content content = contents.get(i);
            
            Logger.d(String.format("[%d] content_id : 0x%x \n", i, content.content_id));
            Logger.d(String.format("[%d] content_version : 0x%x \n", i, content.content_version));
            Logger.d(String.format("[%d] content_size : 0x%x \n", i, content.content_size));
            Logger.d(String.format("[%d] PU_info_flag : 0x%x \n", i, content.PU_info_flag));
            Logger.d(String.format("[%d] content_info_flag : 0x%x \n", 
                    i, content.content_info_flag));
            
            if ( content.PU_info_flag == 1 ) {
                Logger.d(String.format("[%d] number_of_PUs : 0x%x \n", i, content.number_of_PUs));
                
                for ( int j=0; j<content.pus.size(); j++ ) {
                    PU pu = content.pus.get(j);
                    
                    Logger.d(String.format("\t [%d] PU_tag : 0x%x \n", j, pu.PU_tag));
                    Logger.d(String.format("\t [%d] PU_size : 0x%x \n", j, pu.PU_size));
                    Logger.d(String.format("\t [%d] number_of_member_nodes : 0x%x \n", 
                            j, pu.number_of_member_nodes));
                    
                    for ( int k=0; k<pu.node_tag.length; k++ ) {
                        Logger.d(String.format("\t\t [%d] node_tag : 0x%x \n", 
                                k, pu.node_tag[k]));
                    }
                    
                    Logger.d(String.format("\t [%d] PU_descriptor_loop_length : 0x%x \n", 
                            j, pu.PU_descriptor_loop_length));
                    
                    for ( int k=pu.pu_desctiptors.size(); k>0; ) {
                        pu.pu_desctiptors.get(k).print();
                    }
                }
            } else {
                Logger.d(String.format("[%d] number_of_nodes : 0x%x \n", 
                        i, content.number_of_nodes));
                
                for ( int j=0; j<content.node_tag.length; j++ ) {
                    Logger.d(String.format("\t [%d] node_tag : 0x%x \n", 
                            j, content.node_tag[j]));
                }
            }
            
            if ( content.content_info_flag == 1 ) {
                Logger.d(String.format("[%d] content_descriptor_loop_length : 0x%x \n",
                        i, content.content_descriptor_loop_length));
                
                for ( int j=content.content_descriptors.size(); j>0; ) {
                    content.content_descriptors.get(j).print();
                }
            }
        }
    }
}
