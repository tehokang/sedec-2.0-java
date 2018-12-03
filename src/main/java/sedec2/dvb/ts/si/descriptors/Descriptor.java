package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;

public abstract class Descriptor extends sedec2.base.Descriptor {

    public Descriptor(BitReadWriter brw) {
        super(brw);
    }
}
