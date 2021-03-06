package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class EventPackageDescriptor extends Descriptor {
    protected byte MMT_package_id_length;
    protected byte[] MMT_package_id_byte;

    public EventPackageDescriptor(BitReadWriter brw) {
        super(brw);

        MMT_package_id_length = (byte) brw.readOnBuffer(8);
        MMT_package_id_byte = new byte[MMT_package_id_length];

        for ( int i=0; i<MMT_package_id_length; i++ ) {
            MMT_package_id_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte getMMTPackageIdLength() {
        return MMT_package_id_length;
    }

    public byte[] getMMTPackageIdByte() {
        return MMT_package_id_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t MMT_package_id_length : 0x%x \n", MMT_package_id_length));
        Logger.d(String.format("\t MMT_package_id_byte : %s \n", new String(MMT_package_id_byte)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + MMT_package_id_byte.length;
    }

}
