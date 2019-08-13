package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class ScramblingDescriptor extends Descriptor {
    protected byte scrambling_mode;

    public ScramblingDescriptor(BitReadWriter brw) {
        super(brw);

        scrambling_mode = (byte) brw.readOnBuffer(8);
    }

    public byte getScramblingMode() {
        return scrambling_mode;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t scrambling_mode : 0x%x \n", scrambling_mode));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }
}
