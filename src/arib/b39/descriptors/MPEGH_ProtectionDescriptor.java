package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class MPEGH_ProtectionDescriptor extends Descriptor {
    protected byte DL_system_ID;
    protected MMTGeneralLocationInfo info;
    protected byte encrypt_protocol_number;
    protected byte[] encrypt_info;
    
    public MPEGH_ProtectionDescriptor(BitReadWriter brw) {
        super(brw);
        
        DL_system_ID = (byte) brw.ReadOnBuffer(8);
        info = new MMTGeneralLocationInfo(brw);
        encrypt_protocol_number = (byte) brw.ReadOnBuffer(8);
        
        encrypt_info = new byte[encrypt_protocol_number];
        for ( int i=0; i<encrypt_protocol_number; i++ ) {
            encrypt_info[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t DL_system_ID : 0x%x \n", DL_system_ID));
        
        info.PrintInfo();
        
        Logger.d(String.format("\t encrypt_protocol_number : 0x%x \n", 
                encrypt_protocol_number));
        
        int j=1;
        Logger.d("encrypt_info : \n");
        Logger.p(String.format("%03d : ", j));
        for(int k=0; k<encrypt_info.length; k++)
        {
            Logger.p(String.format("%02x ", encrypt_info[k]));
            if(k%10 == 9) Logger.p(String.format("\n%03d : ", (++j)));
        }
        
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + info.GetLength() + 1 + encrypt_info.length;
    }

}
