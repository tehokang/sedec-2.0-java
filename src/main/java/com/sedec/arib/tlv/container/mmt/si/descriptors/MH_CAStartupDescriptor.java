package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class MH_CAStartupDescriptor extends Descriptor {
    protected int CA_system_ID;
    protected int CA_program_ID;
    protected byte second_load_flag;
    protected byte load_indicator;
    protected byte exclusion_ID_num;
    protected int[] exclusion_CA_program_ID;
    protected byte load_security_info_len;
    protected byte[] load_security_info_byte;
    protected byte[] private_data_byte;

    public MH_CAStartupDescriptor(BitReadWriter brw) {
        super(brw);

        CA_system_ID = brw.readOnBuffer(16);
        brw.skipOnBuffer(3);
        CA_program_ID = (byte) brw.readOnBuffer(13);
        second_load_flag = (byte) brw.readOnBuffer(1);
        load_indicator = (byte) brw.readOnBuffer(7);

        if ( second_load_flag == '1' ) {
            brw.skipOnBuffer(3);
            CA_program_ID = brw.readOnBuffer(13);
            brw.skipOnBuffer(1);
            load_indicator = (byte) brw.readOnBuffer(7);
        }

        exclusion_ID_num = (byte) brw.readOnBuffer(8);
        exclusion_CA_program_ID = new int[exclusion_ID_num];

        for ( int i=0; i<exclusion_CA_program_ID.length; i++ ) {
            brw.skipOnBuffer(3);
            exclusion_CA_program_ID[i] = brw.readOnBuffer(13);
        }

        load_security_info_len = (byte) brw.readOnBuffer(8);
        load_security_info_byte = new byte[load_security_info_len];

        for ( int i=0; i<load_security_info_byte.length; i++ ) {
            load_security_info_byte[i] = (byte) brw.readOnBuffer(8);
        }

        private_data_byte = new byte[(descriptor_length - 5 - (second_load_flag=='1'?3:0)
                - 1 - (exclusion_ID_num*2) - 1 - load_security_info_len)];

        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getCASystemId() {
        return CA_system_ID;
    }

    public int getCAPRogramId() {
        return CA_program_ID;
    }

    public byte getSecondLoadFlag() {
        return second_load_flag;
    }

    public byte getLoadIndicator() {
        return load_indicator;
    }

    public byte getExclusionIdNum() {
        return exclusion_ID_num;
    }

    public int[] getExclusionCAProgramId() {
        return exclusion_CA_program_ID;
    }

    public byte getLoadSecurityInfoLen() {
        return load_security_info_len;
    }

    public byte[] getLoadSecurityInfoByte() {
        return load_security_info_byte;
    }

    public byte[] getPrivateDataByte() {
        return private_data_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t CA_system_ID : 0x%x \n", CA_system_ID));
        Logger.d(String.format("\t CA_program_ID : 0x%x \n", CA_program_ID));
        Logger.d(String.format("\t second_load_flag : 0x%x \n", second_load_flag));
        Logger.d(String.format("\t load_indicator : 0x%x \n", load_indicator));

        Logger.d(String.format("\t exclusion_ID_num : 0x%x \n", exclusion_ID_num));
        for ( int i=0; i<exclusion_CA_program_ID.length; i++ ) {
            Logger.d(String.format("\t [%d] exclusion_CA_program_ID : 0x%x \n",
                    i, exclusion_CA_program_ID[i]));
        }

        Logger.d(String.format("\t load_security_info_len : 0x%x \n", load_security_info_len));
        for ( int i=0; i<load_security_info_byte.length; i++ ) {
            Logger.d(String.format("\t [%d] load_security_info_byte : 0x%x \n",
                    i, load_security_info_byte[i]));
        }

        BinaryLogger.print(private_data_byte);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5;

        if ( second_load_flag  == '1' ) {
            descriptor_length += 3;
        }

        descriptor_length += ( 1 + (exclusion_ID_num*2));
        descriptor_length += ( 1 + (load_security_info_len));
        descriptor_length += private_data_byte.length;
    }
}
