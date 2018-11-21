package sedec2.arib.b10.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class VideoDecodeControlDescriptor extends Descriptor {
    protected byte still_picture_flag;
    protected byte sequence_end_code_flag;
    protected byte video_encode_format;

    public VideoDecodeControlDescriptor(BitReadWriter brw) {
        super(brw);

        still_picture_flag = (byte) brw.readOnBuffer(1);
        sequence_end_code_flag = (byte) brw.readOnBuffer(1);
        video_encode_format = (byte) brw.readOnBuffer(4);
        brw.skipOnBuffer(2);
    }

    public byte getStillPictureFlag() {
        return still_picture_flag;
    }

    public byte getSequenceEndCodeFlag() {
        return sequence_end_code_flag;
    }

    public byte getVideoEncodeFormat() {
        return video_encode_format;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t still_picture_flag : 0x%x \n", still_picture_flag));
        Logger.d(String.format("\t sequence_end_code_flag : 0x%x \n", sequence_end_code_flag));
        Logger.d(String.format("\t video_encode_format : 0x%x \n", video_encode_format));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }

}
