package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;

public abstract class Descriptor extends com.sedec.base.Descriptor {

    public Descriptor(BitReadWriter brw) {
        super(brw);
    }
}
