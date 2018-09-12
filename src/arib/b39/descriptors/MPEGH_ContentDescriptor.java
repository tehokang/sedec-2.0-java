package arib.b39.descriptors;

import java.util.ArrayList;
import java.util.List;

import base.BitReadWriter;
import util.Logger;

public class MPEGH_ContentDescriptor extends Descriptor {
    List<Content> contents = new ArrayList<>();
    
    class Content {
        public byte content_nibble_level_1;
        public byte content_nibble_level_2;
        public byte user_nibble_1;
        public byte user_nibble_2;
    }
    
    public MPEGH_ContentDescriptor(BitReadWriter brw) {
        super(brw);
        
        for ( int i=descriptor_length; i>0; ) {
            Content content = new Content();
            content.content_nibble_level_1 = (byte) brw.ReadOnBuffer(4);
            content.content_nibble_level_2 = (byte) brw.ReadOnBuffer(4);
            content.user_nibble_1 = (byte) brw.ReadOnBuffer(4);
            content.user_nibble_2 = (byte) brw.ReadOnBuffer(4);
            
            contents.add(content);
            i-=2;
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
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
