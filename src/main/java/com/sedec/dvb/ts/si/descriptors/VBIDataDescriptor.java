package com.sedec.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class VBIDataDescriptor extends Descriptor {
    protected List<VBIData> vbi_datas = new ArrayList<>();

    public class VBIData {
        public byte data_service_id;
        public byte data_service_descriptor_length;
        public List<SubData> sub_datas = new ArrayList<>();
    }

    public class SubData {
        public byte field_parity;
        public byte line_offset;
    }

    public VBIDataDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            VBIData vbi = new VBIData();
            vbi.data_service_id = (byte) brw.readOnBuffer(8);
            vbi.data_service_descriptor_length = (byte) brw.readOnBuffer(8);

            i-=2;
            if ( vbi.data_service_id == 0x01 ||
                    vbi.data_service_id == 0x02 ||
                    vbi.data_service_id == 0x04 ||
                    vbi.data_service_id == 0x05 ||
                    vbi.data_service_id == 0x06 ||
                    vbi.data_service_id == 0x07 ) {
                for ( int j=vbi.data_service_descriptor_length; j>0; ) {
                    SubData sub_data = new SubData();
                    brw.skipOnBuffer(2);
                    sub_data.field_parity = (byte) brw.readOnBuffer(1);
                    sub_data.line_offset = (byte) brw.readOnBuffer(5);
                    vbi.sub_datas.add(sub_data);
                    i-=1;
                }
            } else {
                for ( int j=0; j<vbi.data_service_descriptor_length; j++ ) {
                    brw.skipOnBuffer(8);
                    i-=1;
                }
            }
            vbi_datas.add(vbi);
        }
    }

    public List<VBIData> getVBIDatas() {
        return vbi_datas;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<vbi_datas.size(); i++ ) {
            VBIData vbi = vbi_datas.get(i);
            Logger.d(String.format("\t [%d] data_service_id : 0x%x \n",
                    i, vbi.data_service_id));
            Logger.d(String.format("\t [%d] data_service_descriptor_length : 0x%x \n",
                    i, vbi.data_service_descriptor_length));
            for ( int j=0; j<vbi.sub_datas.size(); j++ ) {
                SubData sub_data = vbi.sub_datas.get(j);
                Logger.d(String.format("\t   [%d] field_parity : 0x%x \n",
                        j, sub_data.field_parity));
                Logger.d(String.format("\t   [%d] line_offset : 0x%x \n",
                        j, sub_data.line_offset));
            }
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 0;

        for ( int i=0; i<vbi_datas.size(); i++ ) {
            VBIData vbi = vbi_datas.get(i);
            descriptor_length += 2;
            for ( int j=0; j<vbi.sub_datas.size(); j++ ) {
                descriptor_length += 1;
            }
        }
    }
}
