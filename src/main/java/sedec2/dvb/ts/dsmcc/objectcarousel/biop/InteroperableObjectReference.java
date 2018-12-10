package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class InteroperableObjectReference {
    protected int type_id_length;
    protected byte[] type_id_byte;
    protected byte[] alignment_gap;
    protected int taggedProfiles_count;
    protected List<taggedProfile> tagged_profiles = new ArrayList<>();

    public class taggedProfile {
        public int profileId_tag;
        public int profile_data_length;
        public byte[] profile_data_byte;
    }

    public InteroperableObjectReference(BitReadWriter brw) {
        type_id_length = brw.readOnBuffer(32);

        type_id_byte = new byte[type_id_length];
        for ( int i=0; i<type_id_byte.length; i++ ) {
            type_id_byte[i] = (byte) brw.readOnBuffer(8);
        }

        if ( type_id_length % 4 != 0 ) {
            alignment_gap = new byte[4-(type_id_length%4)];
            for ( int i=0; i<alignment_gap.length; i++ ) {
                alignment_gap[i] = (byte) brw.readOnBuffer(8);
            }
        }

        taggedProfiles_count = brw.readOnBuffer(32);
        for ( int i=0; i<taggedProfiles_count; i++ ) {
            taggedProfile profile = new taggedProfile();
            profile.profileId_tag = brw.readOnBuffer(32);
            profile.profile_data_length = brw.readOnBuffer(32);
            profile.profile_data_byte = new byte[profile.profile_data_length];
            for ( int k=0; k<profile.profile_data_byte.length; k++ ) {
                profile.profile_data_byte[k] = (byte) brw.readOnBuffer(8);
            }
            tagged_profiles.add(profile);
        }
    }

    public int getTypeIdLength() {
        return type_id_length;
    }

    public byte[] getTypeIdByte() {
        return type_id_byte;
    }

    public byte[] getAlignmentGap() {
        return alignment_gap;
    }

    public int getTaggedProfileCount() {
        return taggedProfiles_count;
    }

    public List<taggedProfile> getTaggedProfiles() {
        return tagged_profiles;
    }

    public void print() {
        Logger.d(String.format("- %s - \n", getClass().getName()));
        Logger.d(String.format("type_id_length : 0x%x \n", type_id_length));
        Logger.d(String.format("type_id_byte : %s \n", new String(type_id_byte)));
        Logger.d(String.format("taggedProfile_count : 0x%x \n", taggedProfiles_count));

        for ( int i=0; i<tagged_profiles.size(); i++ ) {
            taggedProfile profile = tagged_profiles.get(i);
            Logger.d(String.format("[%d] profileId_tag : 0x%x \n",
                    i, profile.profileId_tag));
            Logger.d(String.format("[%d] profile_data_length : 0x%x \n",
                    i, profile.profile_data_length));
            Logger.d(String.format("[%d] profile_Data_byte : \n", i));
            BinaryLogger.print(profile.profile_data_byte);
        }
    }
}
