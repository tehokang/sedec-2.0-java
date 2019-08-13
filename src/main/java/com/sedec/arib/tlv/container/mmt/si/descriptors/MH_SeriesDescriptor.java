package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_SeriesDescriptor extends Descriptor {
    protected int series_id;
    protected byte repeat_label;
    protected byte program_pattern;
    protected byte expire_date_valid_flag;
    protected int expire_date;
    protected int episode_number;
    protected int last_episode_number;
    protected byte[] series_name_char;

    public MH_SeriesDescriptor(BitReadWriter brw) {
        super(brw);

        series_id = brw.readOnBuffer(16);
        repeat_label = (byte) brw.readOnBuffer(4);
        program_pattern = (byte) brw.readOnBuffer(3);
        expire_date_valid_flag = (byte) brw.readOnBuffer(1);
        expire_date = brw.readOnBuffer(16);
        episode_number = brw.readOnBuffer(12);
        last_episode_number = brw.readOnBuffer(12);

        series_name_char = new byte[descriptor_length-8];
        for ( int i=0; i<series_name_char.length; i++ ) {
            series_name_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getSeriesId() {
        return series_id;
    }

    public byte getRepeatLabel() {
        return repeat_label;
    }

    public byte getProgramPattern() {
        return program_pattern;
    }

    public byte getExpireDateValidFlag() {
        return expire_date_valid_flag;
    }

    public int getExpireDate() {
        return expire_date;
    }

    public int getEpisodeNumber() {
        return episode_number;
    }

    public int getLastEpisodeNumber() {
        return last_episode_number;
    }

    public byte[] getSeriesNameChar() {
        return series_name_char;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t series_id : 0x%x \n", series_id));
        Logger.d(String.format("\t repeat_label : 0x%x \n", repeat_label));
        Logger.d(String.format("\t program_pattern : 0x%x \n", program_pattern));
        Logger.d(String.format("\t expire_date_valid_flag : 0x%x \n",
                expire_date_valid_flag));
        Logger.d(String.format("\t expire_date : 0x%x \n", expire_date));
        Logger.d(String.format("\t episode_number : 0x%x \n", episode_number));
        Logger.d(String.format("\t last_episode_number : 0x%x \n", last_episode_number));
        Logger.d(String.format("\t series_name_char : %s \n", new String(series_name_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 8 + series_name_char.length;
    }

}
