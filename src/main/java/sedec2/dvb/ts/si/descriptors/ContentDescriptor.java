package sedec2.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ContentDescriptor extends Descriptor {
    protected List<Content> contents = new ArrayList<>();

    public class Content {
        public byte content_nibble_level_1;
        public byte content_nibble_level_2;
        public byte user_byte;
    }

    public ContentDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            Content content = new Content();
            content.content_nibble_level_1 = (byte) brw.readOnBuffer(4);
            content.content_nibble_level_2 = (byte) brw.readOnBuffer(4);
            content.user_byte = (byte) brw.readOnBuffer(8);
            contents.add(content);
            i-=2;
        }
    }

    public List<Content> getContents() {
        return contents;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<contents.size(); i++ ) {
            Content content = contents.get(i);
            Logger.d(String.format("\t [%d] content_nibble_level_1 : 0x%x \n",
                    i, content.content_nibble_level_1));
            Logger.d(String.format("\t [%d] content_nibble_level_2 : 0x%x \n",
                    i, content.content_nibble_level_2));
            Logger.d(String.format("\t [%d] user_byte : 0x%x \n",
                    i, content.user_byte));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = contents.size() * 2;
    }
}
