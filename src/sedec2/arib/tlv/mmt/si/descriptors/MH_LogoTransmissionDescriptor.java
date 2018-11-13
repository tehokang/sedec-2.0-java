package sedec2.arib.tlv.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_LogoTransmissionDescriptor extends Descriptor {
    protected byte logo_transmission_type;
    protected int logo_id;
    protected int logo_version;
    protected int download_data_id;
    protected List<Logo> logos = new ArrayList<>();
    protected byte[] logo_char;
    protected byte[] reserved_future_use;

    public class Logo {
        public byte logo_type;
        public byte start_section_number;
        public byte num_of_sections;
    }
    
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
                
                for ( int i=descriptor_length-1-6; i>0; ) {
                    Logo logo = new Logo();
                    logo.logo_type = (byte) brw.readOnBuffer(8);
                    logo.start_section_number = (byte) brw.readOnBuffer(8);
                    logo.num_of_sections = (byte) brw.readOnBuffer(8);
                    logos.add(logo);
                    i-=3;
                }
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
    
    public List<Logo> getLogos() {
        return logos;
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
                for ( int i=0; i<logos.size(); i++ ) {
                    Logo logo = logos.get(i);
                    Logger.d(String.format("\t [%d] logo_type : 0x%x \n", 
                            i, logo.logo_type));
                    Logger.d(String.format("\t [%d] start_section_number : 0x%x \n",
                            i, logo.start_section_number));
                    Logger.d(String.format("\t [%d] num_of_sections : 0x%x \n", 
                            i, logo.num_of_sections));
                }
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
                descriptor_length = 1 + 6 + (logos.size()*3);
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
