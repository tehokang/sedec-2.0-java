package com.sedec.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class ExtendedEventDescriptor extends Descriptor {
    protected byte descriptor_number;
    protected byte last_descriptor_number;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte length_of_items;
    protected List<Item> items = new ArrayList<>();
    protected byte text_length;
    protected byte[] text_char;

    public class Item {
        public byte item_description_length;
        public byte[] item_description_char;
        public byte item_length;
        public byte[] item_char;
    }

    public ExtendedEventDescriptor(BitReadWriter brw) {
        super(brw);

        descriptor_number = (byte) brw.readOnBuffer(4);
        last_descriptor_number = (byte) brw.readOnBuffer(4);
        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);
        length_of_items = (byte) brw.readOnBuffer(8);

        for ( int i=length_of_items; i>0; ) {
            Item item = new Item();
            item.item_description_length = (byte) brw.readOnBuffer(8);
            item.item_description_char = new byte[item.item_description_length];
            for ( int j=0; j<item.item_description_char.length; j++ ) {
                item.item_description_char[j] = (byte) brw.readOnBuffer(8);
            }
            item.item_length = (byte) brw.readOnBuffer(8);
            item.item_char = new byte[item.item_length];

            for ( int j=0; j<item.item_char.length; j++ ) {
                item.item_char[j] = (byte) brw.readOnBuffer(8);
            }
            i-= (2 + item.item_description_char.length + item.item_char.length);

            items.add(item);
        }

        text_length = (byte) brw.readOnBuffer(8);
        text_char = new byte[text_length];

        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte getDescriptorNumber() {
        return descriptor_number;
    }

    public byte getLastDescriptorNumber() {
        return last_descriptor_number;
    }

    public byte[] getISO639LanguageCode() {
        return ISO_639_language_code;
    }

    public byte getLengthOfItems() {
        return length_of_items;
    }

    public List<Item> getItems() {
        return items;
    }

    public byte[] getTextChar() {
        return text_char;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t descriptor_number : 0x%x \n", descriptor_number));
        Logger.d(String.format("\t last_descriptor_number : 0x%x \n", last_descriptor_number));
        Logger.d(String.format("\t ISO_639_language_code : %s \n",
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t length_of_items : 0x%x \n", length_of_items));

        for ( int i=0; i<items.size(); i++ ) {
            Item item = items.get(i);
            Logger.d(String.format("\t [%d] item_description_char : %s \n",
                    i, new String(item.item_description_char)));
            Logger.d(String.format("\t [%d] item_char : %s \n",
                    i, new String(item.item_char)));
        }

        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5;

        for ( int i=0; i<items.size(); i++ ) {
            Item item = items.get(i);
            descriptor_length +=
                    ( 2 + item.item_description_char.length + item.item_char.length);
        }
        descriptor_length += (1 + text_char.length);
    }
}
