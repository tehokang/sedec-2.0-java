package com.sedec.arib.tlv.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class RemoteControlKeyDescriptor extends Descriptor {
    protected byte num_of_remote_control_key_id;
    protected List<RemoteControlKeyId> remote_control_key_ids = new ArrayList<>();

    class RemoteControlKeyId {
        public byte remote_control_key_id;
        public int service_id;
    }

    public RemoteControlKeyDescriptor(BitReadWriter brw) {
        super(brw);

        num_of_remote_control_key_id = (byte) brw.readOnBuffer(8);
        for ( int i=0; i<num_of_remote_control_key_id; i++ ) {
            RemoteControlKeyId key = new RemoteControlKeyId();
            key.remote_control_key_id = (byte) brw.readOnBuffer(8);
            key.service_id = brw.readOnBuffer(16);
            remote_control_key_ids.add(key);
            brw.skipOnBuffer(16);
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t num_of_remote_control_key_id : %d \n",
                num_of_remote_control_key_id));

        for ( int i=0; i<remote_control_key_ids.size(); i++ ) {
            Logger.d(String.format("\t [%d] remote_control_key_id : %d \n",
                    i, remote_control_key_ids.get(i).remote_control_key_id));
            Logger.d(String.format("\t [%d] service_id : %d \n",
                    i, remote_control_key_ids.get(i).service_id));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;

        descriptor_length += (num_of_remote_control_key_id * 5);
    }
}
