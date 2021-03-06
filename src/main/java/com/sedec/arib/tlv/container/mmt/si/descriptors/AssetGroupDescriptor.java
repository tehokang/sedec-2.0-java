package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class AssetGroupDescriptor extends Descriptor {
    protected byte group_identification;
    protected byte selection_level;

    public AssetGroupDescriptor(BitReadWriter brw) {
        super(brw);

        group_identification = (byte) brw.readOnBuffer(8);
        selection_level = (byte) brw.readOnBuffer(8);
    }

    public byte getGroupIdentification() {
        return group_identification;
    }

    public byte getSelectionLevel() {
        return selection_level;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t group_identification : 0x%x \n", group_identification));
        Logger.d(String.format("\t selection_level : 0x%x \n", selection_level));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }

}
