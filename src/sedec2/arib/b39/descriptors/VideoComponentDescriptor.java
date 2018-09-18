package sedec2.arib.b39.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class VideoComponentDescriptor extends Descriptor {
    protected byte video_resolution;
    protected byte video_aspect_ratio;
    protected byte video_scan_flag;
    protected byte video_frame_rate;
    protected int component_tag;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte[] text_char;
    
    public VideoComponentDescriptor(BitReadWriter brw) {
        super(brw);
        
        video_resolution = (byte) brw.ReadOnBuffer(4);
        video_aspect_ratio = (byte) brw.ReadOnBuffer(4);
        video_scan_flag = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(2);
        video_frame_rate = (byte) brw.ReadOnBuffer(5);
        component_tag = brw.ReadOnBuffer(16);
        
        ISO_639_language_code[0] = (byte) brw.ReadOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.ReadOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.ReadOnBuffer(8);
        
        text_char = new byte[descriptor_length-7];
        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t video_resolution : 0x%x \n", video_resolution));
        Logger.d(String.format("\t video_aspect_ratio : 0x%x \n", video_aspect_ratio));
        Logger.d(String.format("\t video_scan_flag : 0x%x \n", video_scan_flag));
        Logger.d(String.format("\t video_frame_rate : 0x%x \n", video_frame_rate));
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
        Logger.d(String.format("\t ISO_639_language_code : 0x%x \n", 
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t text_char : 0x%x \n",  new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 7 + text_char.length;
    }

}
