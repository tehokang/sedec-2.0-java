package sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class InteroperableObjectReference {
    protected int type_id_length;
    protected byte[] type_id_byte;
    protected byte[] alignment_gap;
    protected int taggedProfiles_count;
    protected List<TaggedProfile> tagged_profiles = new ArrayList<>();

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
            TaggedProfile profile = ProfileFactory.createProfile(brw);
            tagged_profiles.add(profile);
        }
    }

    public int getLength() {
        int length = 4 + type_id_byte.length;
        if ( alignment_gap != null ) {
            length += alignment_gap.length;
        }

        length += 4;
        for ( int i=0; i<tagged_profiles.size(); i++ ) {
            length += tagged_profiles.get(i).getLength();
        }
        return length;
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

    public List<TaggedProfile> getTaggedProfiles() {
        return tagged_profiles;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t type_id_length : 0x%x \n", type_id_length));
        Logger.d(String.format("\t type_id_byte : %s \n", new String(type_id_byte)));
        Logger.d(String.format("\t taggedProfile_count : 0x%x \n", taggedProfiles_count));

        for ( int i=0; i<tagged_profiles.size(); i++ ) {
            tagged_profiles.get(i).print();
        }
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
