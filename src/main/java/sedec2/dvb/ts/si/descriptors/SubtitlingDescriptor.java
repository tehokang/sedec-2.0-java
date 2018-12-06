package sedec2.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class SubtitlingDescriptor extends Descriptor {
    List<Subtitling> subtitlings = new ArrayList<>();

    public class Subtitling {
        public byte[] ISO_639_language_code = new byte[3];
        public byte subtitling_type;
        public int composition_page_id;
        public int ancillary_page_id;
    }

    public SubtitlingDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            Subtitling subtitling = new Subtitling();
            subtitling.ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            subtitling.ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            subtitling.ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

            subtitling.subtitling_type = (byte) brw.readOnBuffer(8);
            subtitling.composition_page_id = brw.readOnBuffer(16);
            subtitling.ancillary_page_id = brw.readOnBuffer(16);
            i-=8;
            subtitlings.add(subtitling);
        }
    }

    public List<Subtitling> getSubtitlings() {
        return subtitlings;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<subtitlings.size(); i++ ) {
            Subtitling subtitling = subtitlings.get(i);
            Logger.d(String.format("\t [%d] ISO_639_language_code : %s \n",
                    i, new String(subtitling.ISO_639_language_code)));
            Logger.d(String.format("\t [%d] subtitling_type : 0x%x \n",
                    i, subtitling.subtitling_type));
            Logger.d(String.format("\t [%d] composition_page_id : 0x%x \n",
                    i, subtitling.composition_page_id));
            Logger.d(String.format("\t [%d] ancillary_page_id : 0x%x \n",
                    i, subtitling.ancillary_page_id));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 8;
    }
}
