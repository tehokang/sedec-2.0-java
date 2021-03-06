package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.arib.tlv.container.mmt.si.info.MMTGeneralLocationInfo;
import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class AccessControlDescriptor extends Descriptor {
    protected int CA_system_ID;
    protected MMTGeneralLocationInfo MMT_general_location_info;
    protected byte[] private_data;

    public AccessControlDescriptor(BitReadWriter brw) {
        super(brw);

        CA_system_ID = brw.readOnBuffer(16);
        MMT_general_location_info = new MMTGeneralLocationInfo(brw);
        private_data =
                new byte[descriptor_length - 2 - MMT_general_location_info.getLength()];
        for ( int j=0; j<private_data.length; j++) {
            private_data[j] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getCASystemId() {
        return CA_system_ID;
    }

    public MMTGeneralLocationInfo getMMTGeneralLocationInfo() {
        return MMT_general_location_info;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t CA_system_ID : 0x%x \n", CA_system_ID));
        MMT_general_location_info.print();
        Logger.d("\t private_data : \n");
        BinaryLogger.print(private_data);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + MMT_general_location_info.getLength() + private_data.length;
    }

}
