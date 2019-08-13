package com.sedec.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class TeletextDescriptor extends Descriptor {
    protected List<Teletext> teletexts = new ArrayList<>();

    public class Teletext {
        public byte[] ISO_639_language_code = new byte[3];
        public byte teletext_type;
        public byte teletext_magazine_number;
        public byte teletext_page_number;
    }

    public TeletextDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            Teletext teletext = new Teletext();
            teletext.ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            teletext.ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            teletext.ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

            teletext.teletext_type = (byte) brw.readOnBuffer(5);
            teletext.teletext_magazine_number = (byte) brw.readOnBuffer(3);
            teletext.teletext_page_number = (byte) brw.readOnBuffer(8);

            teletexts.add(teletext);
            i-=5;
        }
    }

    public List<Teletext> getTeletexts() {
        return teletexts;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<teletexts.size(); i++ ) {
            Teletext teletext = teletexts.get(i);
            Logger.d(String.format("\t [%d] ISO_639_langauge_code : %s \n",
                    i, new String(teletext.ISO_639_language_code)));
            Logger.d(String.format("\t [%d] teletext_type : 0x%x \n",
                    i, teletext.teletext_type));
            Logger.d(String.format("\t [%d] teletext_magazine_number : 0x%x \n",
                    i, teletext.teletext_magazine_number));
            Logger.d(String.format("\t [%d] teletext_page_number : 0x%x \n",
                    i, teletext.teletext_page_number));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = teletexts.size()*5;
    }

}
