package com.sedec.arib.b10.tables.dsmcc.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.base.Descriptor;
import com.sedec.util.Logger;

/**
 * Descriptor to describe Table 8-4 NTP Endpoint Descriptor of ISO 13818-6
 */
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
