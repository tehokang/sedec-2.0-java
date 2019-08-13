package com.sedec.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class RelatedBroadcasterDescriptor extends Descriptor {
    protected byte num_of_broadcaster_id;
    protected byte num_of_affiliation_id;
    protected byte num_of_original_network_id;

    protected List<BroadcasterId> broadcaster_ids = new ArrayList<>();
    protected byte[] affiliation_id;
    protected int[] original_network_id;

    public class BroadcasterId {
        public int network_id;
        public byte broadcaster_id;
    }

    public RelatedBroadcasterDescriptor(BitReadWriter brw) {
        super(brw);

        num_of_broadcaster_id = (byte) brw.readOnBuffer(4);
        num_of_affiliation_id = (byte) brw.readOnBuffer(4);
        num_of_original_network_id = (byte) brw.readOnBuffer(4);

        brw.skipOnBuffer(4);

        for ( int i=0; i<num_of_broadcaster_id; i++ ) {
            BroadcasterId bid = new BroadcasterId();
            bid.network_id = brw.readOnBuffer(16);
            bid.broadcaster_id = (byte) brw.readOnBuffer(8);
            broadcaster_ids.add(bid);
        }

        affiliation_id = new byte[num_of_affiliation_id];
        for ( int i=0; i<num_of_affiliation_id; i++ ) {
            affiliation_id[i] = (byte) brw.readOnBuffer(8);
        }

        original_network_id = new int[num_of_original_network_id];
        for ( int i=0; i<num_of_original_network_id; i++ ) {
            original_network_id[i] = brw.readOnBuffer(16);
        }
    }

    public byte getNumOfBroadcasterId() {
        return num_of_broadcaster_id;
    }

    public byte getNumOfAffiliationId() {
        return num_of_affiliation_id;
    }

    public byte getNumOfOriginalNetworkId() {
        return num_of_original_network_id;
    }

    public List<BroadcasterId> getBroadcasterIds() {
        return broadcaster_ids;
    }

    public byte[] getAffiliationId() {
        return affiliation_id;
    }

    public int[] getOriginalNetworkId() {
        return original_network_id;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t num_of_broadcaster_id : 0x%x \n", num_of_broadcaster_id));
        Logger.d(String.format("\t num_of_affiliation_id : 0x%x \n", num_of_affiliation_id));
        Logger.d(String.format("\t num_of_original_network_id : 0x%x \n",
                num_of_original_network_id));

        for ( int i=0; i<broadcaster_ids.size(); i++ ) {
            Logger.d(String.format("\t [%d] network_id : 0x%x \n",
                    i, broadcaster_ids.get(i).network_id));
            Logger.d(String.format("\t [%d] broadcast_id : 0x%x \n",
                    i, broadcaster_ids.get(i).broadcaster_id));
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
                2 + (broadcaster_ids.size()*3) + num_of_affiliation_id +
                (num_of_original_network_id*2);
    }

}
