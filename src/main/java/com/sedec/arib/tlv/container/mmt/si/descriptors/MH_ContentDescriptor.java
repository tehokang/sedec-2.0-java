package com.sedec.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_ContentDescriptor extends Descriptor {
    protected List<Content> contents = new ArrayList<>();

    public class Content {
        public byte content_nibble_level_1;
        public byte content_nibble_level_2;
        public byte user_nibble_1;
        public byte user_nibble_2;
    }

    public MH_ContentDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            Content content = new Content();
            content.content_nibble_level_1 = (byte) brw.readOnBuffer(4);
            content.content_nibble_level_2 = (byte) brw.readOnBuffer(4);
            content.user_nibble_1 = (byte) brw.readOnBuffer(4);
            content.user_nibble_2 = (byte) brw.readOnBuffer(4);

            contents.add(content);
            i-=2;
        }
    }

    public List<Content> getContent() {
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
            Logger.d(String.format("\t [%d] user_nibble_1 : 0x%x \n",
                    i, content.user_nibble_1));
            Logger.d(String.format("\t [%d] user_nibble_2 : 0x%x \n",
                    i, content.user_nibble_2));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = contents.size() * 2;
    }

}
