package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_LogoTransmissionDescriptor extends Descriptor {
    protected byte logo_transmission_type;
    protected int logo_id;
    protected int logo_version;
    protected int download_data_id;
    protected byte[] logo_char;
    protected byte[] reserved_future_use;
    
    public MH_LogoTransmissionDescriptor(BitReadWriter brw) {
        super(brw);
        
        logo_transmission_type = (byte) brw.readOnBuffer(8);
        
        switch ( logo_transmission_type ) {
            case 0x01:
                brw.skipOnBuffer(7);
                logo_id = brw.readOnBuffer(9);
                brw.skipOnBuffer(4);;
                logo_version = brw.readOnBuffer(12);
                download_data_id = brw.readOnBuffer(16);
                break;
            case 0x02:
                brw.skipOnBuffer(7);
                logo_id = brw.readOnBuffer(9);
                break;
            case 0x03:
                {
                    logo_char = new byte[descriptor_length-1];
                    for ( int i=0; i<logo_char.length; i++ ) {
                        logo_char[i] = (byte) brw.readOnBuffer(8);
                    }
                }
                break;
            default:
                {
                    reserved_future_use = new byte[descriptor_length-1];
                    for ( int i=0; i<descriptor_length-1; i++) {
                        brw.skipOnBuffer(8);
                    }
                }
                break;
        }
    }
    
    public byte getLogoTransmissionType() {
        return logo_transmission_type;
    }
    
    public int getLogoId() {
        return logo_id;
    }
    
    public int getLogoVersion() {
        return logo_version;
    }
    
    public int getDownloadDataId() {
        return download_data_id;
    }
    
    public byte[] getLogoChar() {
        return logo_char;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t logo_transmission_type : 0x%x \n", logo_transmission_type));
        
        switch ( logo_transmission_type ) {
            case 0x01:
                Logger.d(String.format("\t logo_id : 0x%x \n", logo_id));
                Logger.d(String.format("\t logo_version : 0x%x \n", logo_version));
                Logger.d(String.format("\t download_data_id : 0x%x \n", download_data_id));
                break;
            case 0x02:
                Logger.d(String.format("\t logo_id : 0x%x \n", logo_id));
                break;
            case 0x03:
                Logger.d(String.format("\t logo_transmission_type : %s \n", new String(logo_char)));
                break;
            default:
                break;
        }
    }

    @Override
    protected void updateDescriptorLength() {
        switch ( logo_transmission_type ) {
            case 0x01:
                descriptor_length = 1 + 6;
                break;
            case 0x02:
                descriptor_length = 1 + 2;
                break;
            case 0x03:
                descriptor_length = 1 + logo_char.length;
                break;
            default:
                descriptor_length = 1 + reserved_future_use.length;
                break;
        }
    }
}
