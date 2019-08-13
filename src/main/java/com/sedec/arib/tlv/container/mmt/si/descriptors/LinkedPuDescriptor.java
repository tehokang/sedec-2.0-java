package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class LinkedPuDescriptor extends Descriptor {
    private int num_of_linked_PU;
    private byte[] linked_PU_tag = new byte[256];

    public LinkedPuDescriptor(BitReadWriter brw) {
        super(brw);

        if ( 0 < descriptor_length ) {
            num_of_linked_PU = brw.readOnBuffer(8);

            for ( int i=0;i<num_of_linked_PU; i++ ) {
                linked_PU_tag[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public int getNumOfLinkedPu() {
        return num_of_linked_PU;
    }

    public byte[] getLinkedPuTag() {
        return linked_PU_tag;
    }

    public void setNumOfLinkedPu(int value) {
        num_of_linked_PU = value;
    }

    public void setLinkedPuTag(byte[] value) {
        linked_PU_tag = value;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t num_of_linked_PU : 0x%x \n", num_of_linked_PU));
        Logger.d(String.format("\t linked_PU_tag : %s \n", new String(linked_PU_tag)));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + linked_PU_tag.length;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        if ( 0 < descriptor_length ) {
            brw.writeOnBuffer(num_of_linked_PU, 8);

            for ( int i=0; i<num_of_linked_PU; i++ ) {
                brw.writeOnBuffer(linked_PU_tag[i], 8);
            }
        }
    }



}
