package mpegh.descriptors;

import base.BitReadWriter;
import util.Logger;

public class LinkedPuDescriptor extends Descriptor {
    private int num_of_linked_PU;
    private byte[] linked_PU_tag = new byte[256];
    
    public LinkedPuDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            num_of_linked_PU = brw.ReadOnBuffer(8);
            
            for ( int i=0;i<num_of_linked_PU; i++ ) {
                linked_PU_tag[i] = (byte) brw.ReadOnBuffer(8);
            }
        }
    }
    
    public int GetNumOfLinkedPu() {
        return num_of_linked_PU;
    }
    
    public byte[] GetLinkedPuTag() {
        return linked_PU_tag;
    }
    
    public void SetNumOfLinkedPu(int value) {
        num_of_linked_PU = value;
    }
    
    public void SetLinkedPuTag(byte[] value) {
        linked_PU_tag = value;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\tnum_of_linked_PU : 0x%x \n", num_of_linked_PU));
        Logger.d(String.format("\tlinked_PU_tag : %s \n", new String(linked_PU_tag)));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + linked_PU_tag.length;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            brw.WriteOnBuffer(num_of_linked_PU, 8);
            
            for ( int i=0; i<num_of_linked_PU; i++ ) {
                brw.WriteOnBuffer(linked_PU_tag[i], 8);
            }
        }
    }
    
    

}
