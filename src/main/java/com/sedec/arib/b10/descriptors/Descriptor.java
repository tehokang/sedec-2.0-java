package com.sedec.arib.b10.descriptors;

import com.sedec.base.BitReadWriter;

public abstract class Descriptor extends com.sedec.base.Descriptor {

    public Descriptor(BitReadWriter brw) {
        super(brw);
    }
}
