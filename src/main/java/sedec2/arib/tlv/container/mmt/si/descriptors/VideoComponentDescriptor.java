package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class VideoComponentDescriptor extends Descriptor {
    protected byte video_resolution;
    protected byte video_aspect_ratio;
    protected byte video_scan_flag;
    protected byte video_frame_rate;
    protected int component_tag;
    protected byte video_transfer_characteristics;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte[] text_char;

    public VideoComponentDescriptor(BitReadWriter brw) {
        super(brw);

        video_resolution = (byte) brw.readOnBuffer(4);
        video_aspect_ratio = (byte) brw.readOnBuffer(4);
        video_scan_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(2);
        video_frame_rate = (byte) brw.readOnBuffer(5);
        component_tag = brw.readOnBuffer(16);
        video_transfer_characteristics = (byte) brw.readOnBuffer(4);
        brw.skipOnBuffer(4);

        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

        text_char = new byte[descriptor_length-8];
        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte getVideoResolution() {
        return video_resolution;
    }

    public byte getVideoAspectRatio() {
        return video_aspect_ratio;
    }

    public byte getVideoScanFlag() {
        return video_scan_flag;
    }

    public byte getVideoFrameRate() {
        return video_frame_rate;
    }

    public int getComponentTag() {
        return component_tag;
    }

    public byte getVideoTransferCharacteristics() {
        return video_transfer_characteristics;
    }

    public byte[] getISO639LanguageCode() {
        return ISO_639_language_code ;
    }

    public byte[] getTextChar() {
        return text_char;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t video_resolution : 0x%x \n", video_resolution));
        Logger.d(String.format("\t video_aspect_ratio : 0x%x \n", video_aspect_ratio));
        Logger.d(String.format("\t video_scan_flag : 0x%x \n", video_scan_flag));
        Logger.d(String.format("\t video_frame_rate : 0x%x \n", video_frame_rate));
        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));
        Logger.d(String.format("\t video_transfer_characteristics : 0x%x \n",
                video_transfer_characteristics));
        Logger.d(String.format("\t ISO_639_language_code : %s \n",
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t text_char.length : 0x%x \n",  text_char.length));
        Logger.d(String.format("\t text_char : %s \n",  new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 8 + text_char.length;
    }

}
