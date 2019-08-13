package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class AC3Descriptor extends Descriptor {
    protected byte component_type_flag;
    protected byte bsid_flag;
    protected byte mainid_flag;
    protected byte asvc_flag;
    protected byte component_type;
    protected byte bsid;
    protected byte mainid;
    protected byte asvc;
    protected byte[] additional_info_byte;

    public AC3Descriptor(BitReadWriter brw) {
        super(brw);

        component_type_flag = (byte) brw.readOnBuffer(1);
        bsid_flag = (byte) brw.readOnBuffer(1);
        mainid_flag = (byte) brw.readOnBuffer(1);
        asvc_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(4);

        int temp = 0;
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

        additional_info_byte = new byte[descriptor_length-1-temp];
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

    public byte getComponentType() {
        return component_type;
    }

    public byte getBsid() {
        return bsid;
    }

    public byte getMainId() {
        return mainid;
    }

    public byte getAsvc() {
        return asvc;
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

        if ( component_type_flag == 1 ) {
            Logger.d(String.format("\t component_type : 0x%x \n", component_type));
        }

        if ( bsid_flag == 1 ) {
            Logger.d(String.format("\t bsid : 0x%x \n", bsid));
        }

        if ( mainid_flag == 1 ) {
            Logger.d(String.format("\t mainid : 0x%x \n", mainid));
        }

        if ( asvc_flag == 1 ) {
            Logger.d(String.format("\t asvc : 0x%x \n", asvc));
        }

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

        if ( additional_info_byte != null ) {
            descriptor_length += additional_info_byte.length;
        }
    }
}
