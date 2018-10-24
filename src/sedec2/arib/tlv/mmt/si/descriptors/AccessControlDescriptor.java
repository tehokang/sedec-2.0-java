package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class AccessControlDescriptor extends Descriptor {
    protected int CA_system_ID;
    protected MMTGeneralLocationInfo MMT_general_location_info;
    protected byte[] private_data;
    

    public AccessControlDescriptor(BitReadWriter brw) {
        super(brw);
        
        CA_system_ID = brw.readOnBuffer(16);
        
        for ( int i=descriptor_length; i>0; ) {
            
            MMT_general_location_info = new MMTGeneralLocationInfo(brw);
            i-=MMT_general_location_info.getLength();
            
            private_data = new byte[i];
            for ( int j=0; j<private_data.length; j++) {
                private_data[j] = (byte) brw.readOnBuffer(8);
                i-=1;
            }
        }
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
