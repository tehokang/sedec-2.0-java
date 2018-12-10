package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class Profile {
    protected int profileId_tag;
    protected int profile_data_length;

    public Profile(BitReadWriter brw) {
        profileId_tag = brw.readOnBuffer(32);
        profile_data_length = brw.readOnBuffer(32);
    }

    public void print() {
        Logger.d(String.format("- %s - \n", getClass().getName()));
        Logger.d(String.format("profileId_tag : 0x%x \n", profileId_tag));
        Logger.d(String.format("profile_data_length : 0x%x (%d) \n",
                profile_data_length, profile_data_length));
    }
}
