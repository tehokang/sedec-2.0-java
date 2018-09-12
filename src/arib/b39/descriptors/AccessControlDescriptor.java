package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class AccessControlDescriptor extends Descriptor {
    protected int CA_system_ID;
    protected MMTGeneralLocationInfo MMT_general_location_info;
    protected byte[] private_data;
    

    public AccessControlDescriptor(BitReadWriter brw) {
        super(brw);
        
        CA_system_ID = brw.ReadOnBuffer(16);
        
        for ( int i=descriptor_length; i>0; ) {
            
            MMT_general_location_info = new MMTGeneralLocationInfo(brw);
            i-=MMT_general_location_info.GetLength();
            
            private_data = new byte[i];
            for ( int j=0; j<private_data.length; j++) {
                private_data[j] = (byte) brw.ReadOnBuffer(8);
                i-=1;
            }
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t CA_system_ID : 0x%x \n", CA_system_ID));
        
        MMT_general_location_info.PrintInfo();
        
        int j=1;
        Logger.d("private_data : \n");
        Logger.p(String.format("%03d : ", j));
        for(int i=0; i<private_data.length; i++)
        {
            Logger.p(String.format("%02x ", private_data[i]));
            if(i%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
        
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + MMT_general_location_info.GetLength() + private_data.length;
    }

}
