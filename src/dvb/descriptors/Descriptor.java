package dvb.descriptors;

import base.BitReadWriter;

public abstract class Descriptor extends base.Descriptor {

    public Descriptor(BitReadWriter brw) {
        super(brw);
    }
}
