package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class DataBroadcastIdDescriptor extends Descriptor {
    protected int data_broadcast_id;
    protected byte[] id_selector_byte;

    public DataBroadcastIdDescriptor(BitReadWriter brw) {
        super(brw);

        data_broadcast_id = brw.readOnBuffer(16);
        id_selector_byte = new byte[descriptor_length-2];
        for ( int i=0; i<id_selector_byte.length; i++) {
            id_selector_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getDataBroadcastId() {
        return data_broadcast_id;
    }

    public byte[] getIdSelectorByte() {
        return id_selector_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t data_broadcast_id : 0x%x \n", data_broadcast_id));
        for ( int i=0; i<id_selector_byte.length; i++ ) {
            Logger.d(String.format("\t id_selector_byte[%d] : 0x%x \n",
                    i, id_selector_byte[i]));
        }

    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + id_selector_byte.length;
    }
}
