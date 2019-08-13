package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class FrequencyListDescriptor extends Descriptor {
    protected byte coding_type;
    protected int[] centre_frequency;

    public FrequencyListDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(6);;
        coding_type = (byte) brw.readOnBuffer(2);
        centre_frequency = new int[(descriptor_length-1)/4];

        for ( int i=0; i<centre_frequency.length; i++ ) {
            centre_frequency[i] = brw.readOnBuffer(32);
        }
    }

    public byte getCodingType() {
        return coding_type;
    }

    public int[] getCentreFrequency() {
        return centre_frequency;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t coding_type : 0x%x \n", coding_type));
        for ( int i=0; i<centre_frequency.length; i++ ) {
            Logger.d(String.format("\t [%d] centre_frequency : 0x%x \n",
                    i, centre_frequency[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + centre_frequency.length*4;
    }

}
