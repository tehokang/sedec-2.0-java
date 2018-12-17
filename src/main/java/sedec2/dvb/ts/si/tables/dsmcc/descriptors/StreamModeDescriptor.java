package sedec2.dvb.ts.si.tables.dsmcc.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

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
