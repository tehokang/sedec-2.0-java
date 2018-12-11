package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

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

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t profileId_tag : 0x%x \n", profileId_tag));
        Logger.d(String.format("\t profile_data_length : 0x%x (%d) \n",
                profile_data_length, profile_data_length));
    }
}
