package com.sedec.arib.b10.tables.dsmcc.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.base.Descriptor;
import com.sedec.util.Logger;

/**
 * Descriptor to describe Table 8-5 Stream Mode Descriptor of ISO 13818-6
 */
public class StreamModeDescriptor extends Descriptor {
    protected byte streamMode;

    public StreamModeDescriptor(BitReadWriter brw) {
        super(brw);

        streamMode = (byte) brw.readOnBuffer(8);
        brw.skipOnBuffer(8);
    }

    public byte getStreamMode() {
        return streamMode;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t streamMode : 0x%x \n", streamMode));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }

}
