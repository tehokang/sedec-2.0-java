package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class CountryAvailabilityDescriptor extends Descriptor {
    protected byte country_availability_flag;
    protected int[] country_code;

    public CountryAvailabilityDescriptor(BitReadWriter brw) {
        super(brw);

        country_availability_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(7);

        country_code = new int[(descriptor_length-1)/3];
        for ( int i=0; i<country_code.length; i++ ) {
            country_code[i] = brw.readOnBuffer(24);
        }
    }

    public byte getCountryAvailabilityFlag() {
        return country_availability_flag;
    }

    public int[] getCountryCode() {
        return country_code;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t country_availability_flag : 0x%x \n",
                country_availability_flag));

        for ( int i=0; i<country_code.length; i++ ) {
            Logger.d(String.format("\t [%d] country_code : 0x%x \n",
                    i, country_code[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + (country_code.length * 3);
    }
}
