package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class RelatedBroadcasterDescriptor extends Descriptor {
    protected byte num_of_broadcaster_id;
    protected byte num_of_affiliation_id;
    protected byte num_of_original_network_id;
    
    protected int[] network_id;
    protected byte[] broadcaster_id;
    protected byte[] affiliation_id;
    protected int[] original_network_id;
    
    public RelatedBroadcasterDescriptor(BitReadWriter brw) {
        super(brw);
        
        num_of_broadcaster_id = (byte) brw.ReadOnBuffer(4);
        num_of_affiliation_id = (byte) brw.ReadOnBuffer(4);
        num_of_original_network_id = (byte) brw.ReadOnBuffer(4);
        
        brw.SkipOnBuffer(4);
        
        network_id = new int[num_of_broadcaster_id];
        broadcaster_id = new byte[num_of_broadcaster_id];
        
        for ( int i=0; i<num_of_broadcaster_id; i++ ) {
            network_id[i] = brw.ReadOnBuffer(16);
            broadcaster_id[i] = (byte) brw.ReadOnBuffer(8);
        }
        
        affiliation_id = new byte[num_of_affiliation_id];
        for ( int i=0; i<num_of_affiliation_id; i++ ) {
            affiliation_id[i] = (byte) brw.ReadOnBuffer(8);
        }
        
        original_network_id = new int[num_of_original_network_id];
        for ( int i=0; i<num_of_original_network_id; i++ ) {
            original_network_id[i] = brw.ReadOnBuffer(16);
        }
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t num_of_broadcaster_id : 0x%x \n", num_of_broadcaster_id));
        Logger.d(String.format("\t num_of_affiliation_id : 0x%x \n", num_of_affiliation_id));
        Logger.d(String.format("\t num_of_original_network_id : 0x%x \n", 
                num_of_original_network_id));
        
        for ( int i=0; i<num_of_broadcaster_id; i++ ) {
            Logger.d(String.format("\t [%d] network_id : 0x%x \n", i, network_id[i])); 
            Logger.d(String.format("\t [%d] broadcast_id : 0x%x \n", i, broadcaster_id[i]));
        }
        
        for ( int i=0; i<num_of_affiliation_id; i++ ) {
            Logger.d(String.format("\t [%d] affiliation_id : 0x%x \n", i, affiliation_id[i])); 
        }
        
        for ( int i=0; i<num_of_original_network_id; i++ ) {
            Logger.d(String.format("\t [%d] original_network_id : 0x%x \n", 
                    i, original_network_id[i])); 
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 
                2 + (num_of_broadcaster_id*3) + num_of_affiliation_id + 
                (num_of_original_network_id*2);
    }

}
