package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_AudioComponentDescriptor extends Descriptor {
    protected byte stream_content;
    protected byte component_type;
    protected byte component_tag;
    protected byte stream_type;
    protected byte simulcast_group_tag;
    protected byte ES_multi_lingual_flag;
    protected byte main_component_tag;
    protected byte quality_indicator;
    protected byte sampling_rate;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte[] ISO_639_language_code_2 = new byte[3];
    protected byte[] text_char;
    
    public MH_AudioComponentDescriptor(BitReadWriter brw) {
        super(brw);
        
        brw.SkipOnBuffer(4);
        stream_content = (byte) brw.ReadOnBuffer(4);
        component_type = (byte) brw.ReadOnBuffer(8);
        component_tag = (byte) brw.ReadOnBuffer(16);
        stream_type = (byte) brw.ReadOnBuffer(8);
        simulcast_group_tag = (byte) brw.ReadOnBuffer(8);
        ES_multi_lingual_flag = (byte) brw.ReadOnBuffer(1);
        main_component_tag = (byte) brw.ReadOnBuffer(1);
        quality_indicator = (byte) brw.ReadOnBuffer(2);
        sampling_rate = (byte) brw.ReadOnBuffer(3);
        brw.SkipOnBuffer(1);
        
        ISO_639_language_code[0] = (byte) brw.ReadOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.ReadOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.ReadOnBuffer(8);
        
        if ( ES_multi_lingual_flag == 1 ) {
            ISO_639_language_code_2[0] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code_2[1] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code_2[2] = (byte) brw.ReadOnBuffer(8);
        }
        
        text_char = new byte[(descriptor_length - 10 - (ES_multi_lingual_flag==1? 3 : 0))];
        for ( int i = 0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t stream_content : 0x%x \n", stream_content));
        Logger.d(String.format("\t component_type : 0x%x \n", component_type));
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
        Logger.d(String.format("\t stream_type : 0x%x \n", stream_type));
        Logger.d(String.format("\t simulcast_group_tag : 0x%x \n", simulcast_group_tag));
        Logger.d(String.format("\t ES_multi_lingual_flag : 0x%x \n", ES_multi_lingual_flag));
        Logger.d(String.format("\t main_component_tag : 0x%x \n", main_component_tag));
        Logger.d(String.format("\t quality_indicator : 0x%x \n", quality_indicator));
        Logger.d(String.format("\t sampling_rate : 0x%x \n", sampling_rate));
        Logger.d(String.format("\t ISO_639_language_code : %s \n", 
                new String(ISO_639_language_code)));
        if ( ES_multi_lingual_flag == 1 ) {
            Logger.d(String.format("\t ISO_639_language_code_2 : %s \n", 
                    new String(ISO_639_language_code_2)));
        }
        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 10 + ((ES_multi_lingual_flag==1? 3 : 0)) + text_char.length;
    }
}
