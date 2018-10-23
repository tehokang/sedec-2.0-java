package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MH_DownloadProtectionDescriptor extends Descriptor {
    protected byte DL_system_ID;
    protected MMTGeneralLocationInfo info;
    protected byte encrypt_protocol_number;
    protected byte[] encrypt_info;
    
    public MH_DownloadProtectionDescriptor(BitReadWriter brw) {
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
        
        Logger.d("\t encrypt_info : \n");
        BinaryLogger.Print(encrypt_info);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + info.GetLength() + 1 + encrypt_info.length;
    }

}
