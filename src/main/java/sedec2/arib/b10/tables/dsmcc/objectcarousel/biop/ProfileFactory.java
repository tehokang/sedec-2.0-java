package sedec2.arib.b10.tables.dsmcc.objectcarousel.biop;

import sedec2.base.BitReadWriter;

public class ProfileFactory {
    public static final int BIOP_PROFILE_BODY = 0x49534f06;
    public static final int LITE_OPTIONS_PROFILE_BODY = 0x49534f05;

    public static TaggedProfile createProfile(BitReadWriter brw) {
        int profileId_tag = (brw.getCurrentBuffer()[0] & 0xff) << 24 |
                (brw.getCurrentBuffer()[1] & 0xff) << 16 |
                (brw.getCurrentBuffer()[2] & 0xff) << 8 |
                brw.getCurrentBuffer()[3];

        switch ( profileId_tag ) {
            case BIOP_PROFILE_BODY:
                return new BIOPProfileBody(brw);
            case LITE_OPTIONS_PROFILE_BODY:
                return new LiteOptionsProfileBody(brw);
        }
        return null;
    }

    private ProfileFactory() {
        /**
         * DO NOTHING
         */
    }
}
