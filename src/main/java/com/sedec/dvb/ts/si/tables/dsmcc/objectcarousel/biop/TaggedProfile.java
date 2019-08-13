package com.sedec.dvb.ts.si.tables.dsmcc.objectcarousel.biop;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class TaggedProfile {
    protected int profileId_tag;
    protected int profile_data_length;

    public TaggedProfile(BitReadWriter brw) {
        profileId_tag = brw.readOnBuffer(32);
        profile_data_length = brw.readOnBuffer(32);
    }

    public int getProfileIdTag() {
        return profileId_tag;
    }

    public int getProfileDataLength() {
        return profile_data_length;
    }

    public int getLength() {
        return 8;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t profileId_tag : 0x%x \n", profileId_tag));
        Logger.d(String.format("\t profile_data_length : 0x%x (%d) \n",
                profile_data_length, profile_data_length));
    }
}
