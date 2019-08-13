package com.sedec.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.tlv.container.mmt.si.info.MMTGeneralLocationInfo;
import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class ApplicationServiceDescriptor extends Descriptor {
    protected byte application_format;
    protected byte document_resolution;
    protected byte default_AIT_flag;
    protected byte DT_message_flag;
    protected byte EMT_num;

    protected MMTGeneralLocationInfo AIT_location_info;
    protected MMTGeneralLocationInfo DT_message_location_info;
    protected List<EMT> emts = new ArrayList<>();
    protected byte[] private_data;

    public class EMT {
        public byte EMT_tag;
        public MMTGeneralLocationInfo EMT_location_info;
    }

    public ApplicationServiceDescriptor(BitReadWriter brw) {
        super(brw);

        application_format = (byte) brw.readOnBuffer(4);
        brw.skipOnBuffer(4);
        document_resolution = (byte) brw.readOnBuffer(4);
        brw.skipOnBuffer(4);
        default_AIT_flag = (byte) brw.readOnBuffer(1);
        DT_message_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(2);
        EMT_num = (byte) brw.readOnBuffer(4);

        AIT_location_info = new MMTGeneralLocationInfo(brw);

        if ( DT_message_flag == 1 ) {
            DT_message_location_info = new MMTGeneralLocationInfo(brw);
        }

        for ( int i=0; i<EMT_num; i++ ) {
            EMT emt = new EMT();
            emt.EMT_tag =  (byte) brw.readOnBuffer(8);
            emt.EMT_location_info = new MMTGeneralLocationInfo(brw);
            emts.add(emt);
        }

        int private_data_length = descriptor_length - 3;
        private_data_length -= AIT_location_info.getLength();
        private_data_length -= (DT_message_flag == 1 ? DT_message_location_info.getLength():0);

        for ( int i=0; i<emts.size(); i++ ) {
            MMTGeneralLocationInfo EMT_location_info = emts.get(i).EMT_location_info;
            private_data_length -= (1 + EMT_location_info.getLength());
        }
        private_data = new byte[private_data_length];
        for ( int i=0; i<private_data_length; i++ ) {
            private_data[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte getApplicationFormat() {
        return application_format;
    }

    public byte getDocumentResolution() {
        return document_resolution;
    }

    public byte getDefaultAITFlag() {
        return default_AIT_flag;
    }

    public byte getDTMessageFlag() {
        return DT_message_flag;
    }

    public byte getEMTNum() {
        return EMT_num;
    }

    public MMTGeneralLocationInfo getAITLocationInfo() {
        return AIT_location_info;
    }

    public MMTGeneralLocationInfo getDTMessageLocationInfo() {
        return DT_message_location_info;
    }

    public List<EMT> getEMTs() {
        return emts;
    }

    public byte[] getPrivateData() {
        return private_data;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t application_format_map : 0x%x \n", application_format));
        Logger.d(String.format("\t default_AIT_flag : 0x%x \n", default_AIT_flag));
        Logger.d(String.format("\t DT_message_flag : 0x%x \n", DT_message_flag));
        Logger.d(String.format("\t EMT_num : 0x%x \n", EMT_num));

        AIT_location_info.print();

        if ( DT_message_flag == 1 ) {
            DT_message_location_info.print();
        }

        for ( int i=0; i<emts.size(); i++ ) {
            Logger.d(String.format("\t [%d] EMT_tag : 0x%x \n", i, emts.get(i).EMT_tag));
            Logger.d(String.format("\t [%d] EMT_location_info : \n", i));
            emts.get(i).EMT_location_info.print();
        }
    }

    @Override
    protected void updateDescriptorLength() {
       descriptor_length = 3;
       descriptor_length += AIT_location_info.getLength();

       if ( DT_message_flag == 1 ) {
           descriptor_length += DT_message_location_info.getLength();
       }

       for ( int i=0; i<emts.size(); i++ ) {
           descriptor_length += (1 + emts.get(i).EMT_location_info.getLength());
       }

       descriptor_length += private_data.length;
    }
}
