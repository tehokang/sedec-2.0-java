package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class UnlockedCacheDescriptor extends Descriptor {
    private int num_of_unlocked_cache_node;
    private int[] node_tag = new int[128];

    public UnlockedCacheDescriptor(BitReadWriter brw) {
        super(brw);

        if( 0 < descriptor_length ) {
            num_of_unlocked_cache_node = brw.readOnBuffer(8);

            for ( int i=0;i<num_of_unlocked_cache_node;i++ ) {
                node_tag[i] = brw.readOnBuffer(16);
            }
        }
    }

    public int getNumOfUnlockedCacheNode() {
        return num_of_unlocked_cache_node;
    }

    public int[] getNodeTag() {
        return node_tag;
    }

    public void setNumOfUnlockedCacheNode(int value) {
        num_of_unlocked_cache_node = value;
    }

    public void setNodeTag(int[] value) {
        node_tag = value;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t num_of_unlocked_cache_node : 0x%x \n", num_of_unlocked_cache_node));
        for ( int i=0; i<num_of_unlocked_cache_node; i++ ) {
            Logger.d(String.format("\t node_tag[%d] : 0x%x \n", i, node_tag[i]));
        }
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + num_of_unlocked_cache_node*2;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        if ( 0 < descriptor_length ) {
            brw.writeOnBuffer(num_of_unlocked_cache_node, 8);

            for ( int i=0; i<num_of_unlocked_cache_node; i++ ) {
                brw.writeOnBuffer(node_tag[i], 16);
            }
        }
    }


}
