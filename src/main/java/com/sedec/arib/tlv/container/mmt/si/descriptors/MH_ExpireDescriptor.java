package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_ExpireDescriptor extends Descriptor {
    private int time_mode;
    private long UTC_time;
    private int passed_seconds;

    public MH_ExpireDescriptor(BitReadWriter brw) {
        super(brw);

        time_mode = brw.readOnBuffer(8);

        if ( 0x01 == time_mode ) {
            UTC_time = brw.readLongOnBuffer(64);
        } else if ( 0x04 == time_mode ) {
            brw.skipOnBuffer(8);
            passed_seconds = brw.readOnBuffer(32);
        }
    }

    public int getTimeMode() {
        return time_mode;
    }

    public double getUTCTime() {
        return UTC_time;
    }

    public int getPassedSeconds() {
        return passed_seconds;
    }

    public void setTimeMode(int value) {
        time_mode =  value;
    }

    public void setUTCTime(long value) {
        UTC_time = value;
    }

    public void setPassedSeconds(int value) {
        passed_seconds = value;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t time_mode : 0x%x \n", time_mode));
        if ( 0x01 == time_mode ) {
            Logger.d(String.format("\t UTC_time : %lf \n", UTC_time));
        } else if ( 0x04 == time_mode ) {
            Logger.d(String.format("\t passed_seconds : 0x%x (%d) \n",
                    passed_seconds, passed_seconds));
        }
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        if ( 0x01 == time_mode ) {
            descriptor_length = 9;
        } else if ( 0x04 == time_mode ) {
            descriptor_length = 6;
        }
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        if ( 0 < descriptor_length ) {
            brw.writeOnBuffer(time_mode, 8);

            if ( 0x01 == time_mode ) {
                brw.writeOnBuffer(UTC_time, 64);
            } else if ( 0x04 == time_mode ) {
                brw.writeOnBuffer(0x0f, 8);
                brw.writeOnBuffer(passed_seconds, 32);
            }
        }
    }
}
