package sedec2.arib.tlv.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MPU_DownloadContentDescriptor extends Descriptor {
    protected byte reboot;
    protected byte add_on;
    protected byte compatibility_flag;
    protected byte item_info_flag;
    protected byte text_info_flag;
    protected int component_size;
    protected int download_id;
    protected int time_our_value_DAM;
    protected int leak_rate;
    protected int component_tag;
    protected Descriptor compatibility_descriptor;
    
    protected int num_of_items;
    protected List<ItemInfo> item_infos = new ArrayList<>();
    protected byte private_data_length;
    protected byte[] private_data_byte;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte text_length;
    protected byte[] text_char;
    
    class ItemInfo {
        public int item_id;
        public int item_size;
        public byte item_info_length;
        public byte[] item_info_byte;
    }
    
    public MPU_DownloadContentDescriptor(BitReadWriter brw) {
        super(brw);
        
        reboot = (byte) brw.ReadOnBuffer(1);
        add_on = (byte) brw.ReadOnBuffer(1);
        component_tag = (byte) brw.ReadOnBuffer(1);
        item_info_flag = (byte) brw.ReadOnBuffer(1);
        text_info_flag = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(3);
        component_size = brw.ReadOnBuffer(32);
        download_id = brw.ReadOnBuffer(32);
        time_our_value_DAM = brw.ReadOnBuffer(32);
        leak_rate = brw.ReadOnBuffer(22);
        brw.SkipOnBuffer(2);
        component_tag = brw.ReadOnBuffer(16);
        
        if ( compatibility_flag == 1 ) {
            compatibility_descriptor = (Descriptor) DescriptorFactory.CreateDescriptor(brw);
        }
        
        if ( item_info_flag == 1 ) {
            num_of_items = brw.ReadOnBuffer(16);
            
            for ( int i=0; i<num_of_items; i++ ) {
                ItemInfo item_info = new ItemInfo();
                item_info.item_id = brw.ReadOnBuffer(32);
                item_info.item_size = brw.ReadOnBuffer(32);
                item_info.item_info_length = (byte) brw.ReadOnBuffer(8);
                
                item_info.item_info_byte = new byte[item_info.item_info_length];
                for ( int j=0; j<item_info.item_info_length; j++ ) {
                    item_info.item_info_byte[j] = (byte) brw.ReadOnBuffer(8);
                }
                item_infos.add(item_info);
            }
        }
        
        private_data_length = (byte) brw.ReadOnBuffer(8);
        private_data_byte = new byte[private_data_length];
        
        for ( int i=0; i<private_data_length; i++ ) {
            private_data_byte[i] = (byte) brw.ReadOnBuffer(8);
        }
        
        if ( text_info_flag == 1) {
            ISO_639_language_code[0] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code[1] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code[2] = (byte) brw.ReadOnBuffer(8);
            
            text_length = (byte) brw.ReadOnBuffer(8);
            text_char = new byte[text_length];
            
            for ( int i=0; i<text_char.length; i++ ) {
                text_char[i] = (byte) brw.ReadOnBuffer(8);
            }
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t reboot : 0x%x \n", reboot));
        Logger.d(String.format("\t add_on : 0x%x \n", add_on));
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
        Logger.d(String.format("\t item_info_flag : 0x%x \n", item_info_flag));
        Logger.d(String.format("\t text_info_flag : 0x%x \n", text_info_flag));
        Logger.d(String.format("\t component_size : 0x%x \n", component_size));
        Logger.d(String.format("\t download_id : 0x%x \n", download_id));
        Logger.d(String.format("\t time_our_value_DAM : 0x%x \n", time_our_value_DAM));
        Logger.d(String.format("\t leak_rate : 0x%x \n", leak_rate));
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
        
        if ( compatibility_flag == 1 ) {
            compatibility_descriptor.PrintDescriptor();
        }
        
        if ( item_info_flag == 1 ) {
            Logger.d(String.format("\t num_of_items : 0x%x \n", num_of_items));
            
            for ( int i=0; i<item_infos.size(); i++ ) {
                ItemInfo item_info = item_infos.get(i);
                
                Logger.d(String.format("\t\t [%d] item_id : 0x%x \n", 
                        i, item_info.item_id));
                Logger.d(String.format("\t\t [%d] item_size : 0x%x \n", 
                        i, item_info.item_size));
                Logger.d(String.format("\t\t [%d] item_info_length : 0x%x \n", 
                        i, item_info.item_info_length));

                Logger.d(String.format("\t\t [%d] item_info_length : %s \n", 
                        i, new String(item_info.item_info_byte)));
            }
        }
        
        Logger.d(String.format("\t private_data_length : 0x%x \n", private_data_length));
        
        int j=1;
        Logger.p(String.format("%03d : ", j));
        for(int k=0; k<private_data_byte.length; k++)
        {
            Logger.p(String.format("%02x ", private_data_byte[k]));
            if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
        
        if ( text_info_flag == 1) {
            Logger.d(String.format("\t ISO_639_language_code : 0x%x \n", 
                    new String(ISO_639_language_code)));
            Logger.d(String.format("\t text_char : 0x%x \n", 
                    new String(text_char)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + 4 + 4 + 4 + 3 + 2;
        
        if ( compatibility_flag == 1 ) {
            descriptor_length += compatibility_descriptor.GetDescriptorLength();
        }
        
        if ( item_info_flag == 1 ) {
            descriptor_length += 2;
            
            for ( int i=0; i<item_infos.size(); i++ ) {
                ItemInfo item_info = item_infos.get(i);
                descriptor_length += (4 + 4 + 1 + item_info.item_info_length);
            }
        }
        
        descriptor_length += (1 + private_data_byte.length);
        
        if ( text_info_flag == 1) {
            descriptor_length += (3 + 1 + text_char.length);
        }
    }
}
