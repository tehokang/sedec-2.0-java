package sedec2.dvb.ts.dsmcc.objectcarousel.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

public class NTPEndpointDescriptor extends Descriptor {
    protected int startNPT;
    protected int stopNPT;

    public NTPEndpointDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(15);
        startNPT = brw.readOnBuffer(33);
        brw.skipOnBuffer(31);
        stopNPT = brw.readOnBuffer(33);
    }

    public int getStartNPT() {
        return startNPT;
    }

    public int getStopNPT() {
        return stopNPT;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t startNPT : 0x%x \n", startNPT));
        Logger.d(String.format("\t stopNPT : 0x%x \n", stopNPT));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 14;
    }
}
