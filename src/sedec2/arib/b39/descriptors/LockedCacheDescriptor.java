package sedec2.arib.b39.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class LockedCacheDescriptor extends Descriptor {
    private int num_of_locked_cache_node;
    private int[] node_tag = new int[128];
    
    public LockedCacheDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            num_of_locked_cache_node = brw.ReadOnBuffer(8);
            
            for ( int i=0; i<num_of_locked_cache_node; i++ ) {
                node_tag[i] = brw.ReadOnBuffer(16);
            }
        }
    }

    public int GetNumOfLockedCache() {
        return num_of_locked_cache_node;
    }
    
    public int[] GetNodeTag() {
        return node_tag;
    }
    
    public void SetNumOfLockedCache(int value) {
        num_of_locked_cache_node = value;
    }
    
    public void SetNodeTag(int []value) {
        node_tag = value;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t num_of_locked_cache_node : 0x%x \n", num_of_locked_cache_node));
        for ( int i=0;i<num_of_locked_cache_node; i++ ) {
            Logger.d(String.format("\t node_tag[%d] : 0x%x \n", i, node_tag[i]));
        }
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + num_of_locked_cache_node*2;

    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            brw.WriteOnBuffer(num_of_locked_cache_node, 8);
            
            for ( int i=0; i<num_of_locked_cache_node; i++ ) {
                brw.WriteOnBuffer(node_tag[i], 16);
            }
        }
    }

    
}
