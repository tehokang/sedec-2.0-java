package sedec2.arib.tlv.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ExtendedEventDescriptor extends Descriptor {
    protected byte descriptor_number;
    protected byte last_descriptor_number;
    protected byte[] ISO_639_language_code = new byte[3];
    protected int length_of_items;
    protected List<Item> items = new ArrayList<>();
    protected int text_length;
    protected byte[] text_char;
    
    class Item {
        public byte item_description_length;
        public byte[] item_description_char;
        public int item_length;
        public byte[] item_char;
    }
    
    public MH_ExtendedEventDescriptor(BitReadWriter brw) {
        super();
        
        descriptor_tag = brw.ReadOnBuffer(16);
        descriptor_length = brw.ReadOnBuffer(16);
        descriptor_number = (byte) brw.ReadOnBuffer(4);
        last_descriptor_number = (byte) brw.ReadOnBuffer(4);
        
        ISO_639_language_code[0] = (byte) brw.ReadOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.ReadOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.ReadOnBuffer(8);
        
        length_of_items = (byte) brw.ReadOnBuffer(16);
        
        for ( int i=length_of_items; i>0; ) {
            Item item = new Item();
            item.item_description_length = (byte) brw.ReadOnBuffer(8);
            item.item_description_char = new byte[item.item_description_length];
            for ( int j=0; j<item.item_description_char.length; j++ ) {
                item.item_description_char[j] = (byte) brw.ReadOnBuffer(8);
            }
            
            item.item_length = brw.ReadOnBuffer(16);
            item.item_char = new byte[item.item_length];
            
            for ( int j=0; j<item.item_char.length; j++ ) {
                item.item_char[j] = (byte) brw.ReadOnBuffer(8);
            }
            i-= (3 + item.item_description_length + item.item_length);
            items.add(item);
        }
        
        text_length = brw.ReadOnBuffer(16);
        text_char = new byte[text_length];
        
        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t descriptor_number : 0x%x \n", descriptor_number));
        Logger.d(String.format("\t last_descriptor_number : 0x%x \n", last_descriptor_number));
        Logger.d(String.format("\t ISO_639_language_code : %s \n", 
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t length_of_items : 0x%x \n", length_of_items));
        
        for ( int i=0; i<items.size(); i++ ) {
            Item item = items.get(i);
            Logger.d(String.format("\t [%d] item_description_length : 0x%x \n",
                    i, item.item_description_length));
            Logger.d(String.format("\t [%d] item_description_char : %s \n",
                    i, new String(item.item_description_char)));
            Logger.d(String.format("\t [%d] item_length : 0x%x \n", 
                    i, item.item_length));
            Logger.d(String.format("\t [%d] length_of_items : %s \n", 
                    i, new String(item.item_char)));
        }
        Logger.d(String.format("\t text_length : 0x%x \n", text_length));
        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    public int GetDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 4;
    }
    
    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 6;
        
        for ( int i=0; i<items.size(); i++ ) {
            Item item = items.get(i);
            descriptor_length+=(1+item.item_description_char.length);
            descriptor_length+=(2+item.item_char.length);
        }
        descriptor_length += (2+text_char.length);
    }
}
