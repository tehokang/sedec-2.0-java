package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class EnhancedAC3Descriptor extends Descriptor {
    protected byte component_type_flag;
    protected byte bsid_flag;
    protected byte mainid_flag;
    protected byte asvc_flag;
    protected byte mixinfoexists;
    protected byte substream1_flag;
    protected byte substream2_flag;
    protected byte substream3_flag;
    protected byte component_type;
    protected byte bsid;
    protected byte mainid;
    protected byte asvc;
    protected byte substream1;
    protected byte substream2;
    protected byte substream3;
    protected byte[] additional_info_byte;

    public EnhancedAC3Descriptor(BitReadWriter brw) {
        super(brw);

        component_type_flag = (byte) brw.readOnBuffer(1);
        bsid_flag = (byte) brw.readOnBuffer(1);
        mainid_flag = (byte) brw.readOnBuffer(1);
        asvc_flag = (byte) brw.readOnBuffer(1);
        mixinfoexists = (byte) brw.readOnBuffer(1);
        substream1_flag = (byte) brw.readOnBuffer(1);
        substream2_flag = (byte) brw.readOnBuffer(1);
        substream3_flag = (byte) brw.readOnBuffer(1);

        int temp = 1;
        if ( component_type_flag == 1 ) {
            component_type = (byte) brw.readOnBuffer(8);
            temp += 1;
        }

        if ( bsid_flag == 1 ) {
            bsid = (byte) brw.readOnBuffer(8);
            temp += 1;
        }

        if ( mainid_flag == 1 ) {
            mainid = (byte) brw.readOnBuffer(8);
            temp += 1;
        }

        if ( asvc_flag == 1 ) {
            asvc = (byte) brw.readOnBuffer(8);
            temp += 1;
        }

        if ( substream1_flag == 1 ) {
            substream1 = (byte) brw.readOnBuffer(8);
            temp += 1;
        }

        if ( substream2_flag == 1 ) {
            substream2 = (byte) brw.readOnBuffer(8);
            temp += 1;
        }

        if ( substream3_flag == 1 ) {
            substream3 = (byte) brw.readOnBuffer(8);
            temp += 1;
        }

        additional_info_byte = new byte[descriptor_length - temp];
        for ( int i=0; i<additional_info_byte.length; i++ ) {
            additional_info_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte getComponentTypeFlag() {
        return component_type_flag;
    }

    public byte getBsidFlag() {
        return bsid_flag;
    }

    public byte getMainIdFlag() {
        return mainid_flag;
    }

    public byte getAsvcFlag() {
        return asvc_flag;
    }

    public byte getMixInfoExists() {
        return mixinfoexists;
    }

    public byte getSubStream1Flag() {
        return substream1_flag;
    }

    public byte getSubStream2Flag() {
        return substream2_flag;
    }

    public byte getSubStream3Flag() {
        return substream3_flag;
    }

    public byte getComponentType() {
        return component_type;
    }

    public byte getBsId() {
        return bsid;
    }

    public byte getMainId() {
        return mainid;
    }

    public byte getAsvc() {
        return asvc;
    }

    public byte getSubStream1() {
        return substream1;
    }

    public byte getSubStream2() {
        return substream2;
    }

    public byte getSubStream3() {
        return substream3;
    }

    public byte[] getAdditionalInfoByte() {
        return additional_info_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t component_type_flag : 0x%x \n", component_type_flag));
        Logger.d(String.format("\t bsid_flag : 0x%x \n", bsid_flag));
        Logger.d(String.format("\t mainid_flag : 0x%x \n", mainid_flag));
        Logger.d(String.format("\t asvc_flag : 0x%x \n", asvc_flag));
        Logger.d(String.format("\t mixinfoexists : 0x%x \n", mixinfoexists));
        Logger.d(String.format("\t substream1_flag : 0x%x \n", substream1_flag));
        Logger.d(String.format("\t substream2_flag : 0x%x \n", substream2_flag));
        Logger.d(String.format("\t substream3_flag : 0x%x \n", substream3_flag));

        Logger.d(String.format("\t additional_info_byte : \n"));
        BinaryLogger.print(additional_info_byte);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;

        if ( component_type_flag == 1 ) {
            descriptor_length += 1;
        }

        if ( bsid_flag == 1 ) {
            descriptor_length += 1;
        }

        if ( mainid_flag == 1 ) {
            descriptor_length += 1;
        }

        if ( asvc_flag == 1 ) {
            descriptor_length += 1;
        }

        if ( substream1_flag == 1 ) {
            descriptor_length += 1;
        }

        if ( substream2_flag == 1 ) {
            descriptor_length += 1;
        }

        if ( substream3_flag == 1 ) {
            descriptor_length += 1;
        }

        descriptor_length += additional_info_byte.length;
    }
}
