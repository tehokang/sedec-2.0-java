package sedec2.arib.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_PlaybackApplicationDescriptor extends Descriptor {
    protected byte application_profiles_length;
    protected List<ApplicationProfile> application_profiles = new ArrayList<>();
    protected byte service_bound_flag;
    protected byte visibility;
    protected byte application_priority;
    protected byte[] transport_protocol_label;
    
    class ApplicationProfile {
        public int application_profile;
        public byte version_major;
        public byte version_minor;
        public byte version_micro;
    }
    
    public MH_PlaybackApplicationDescriptor(BitReadWriter brw) {
        super(brw);
        
        application_profiles_length = (byte) brw.ReadOnBuffer(8);
        for ( int i=0; i<application_profiles_length; i++ ) {
            ApplicationProfile app_profile = new ApplicationProfile();
            app_profile.application_profile = brw.ReadOnBuffer(16);
            app_profile.version_major = (byte) brw.ReadOnBuffer(8);
            app_profile.version_minor = (byte) brw.ReadOnBuffer(8);
            app_profile.version_micro = (byte) brw.ReadOnBuffer(8);
            application_profiles.add(app_profile);
        }
        
        service_bound_flag = (byte) brw.ReadOnBuffer(1);
        visibility = (byte) brw.ReadOnBuffer(2);
        brw.SkipOnBuffer(5);
        application_priority = (byte) brw.ReadOnBuffer(8);
        
        transport_protocol_label = 
                new byte[descriptor_length-3-application_profiles.size()*5];
        for ( int i=0; i<transport_protocol_label.length; i++ ) {
            transport_protocol_label[i] = (byte) brw.ReadOnBuffer(8);
        }
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t application_profiles_length : 0x%x \n", 
                application_profiles_length));
        for ( int i=0; i<application_profiles.size(); i++ ) {
            ApplicationProfile app_profile = application_profiles.get(i);
            Logger.d(String.format("\t [%d] application_profile : 0x%x \n", 
                    i, app_profile.application_profile));
            Logger.d(String.format("\t [%d] version_major : 0x%x \n", 
                    i, app_profile.version_major));
            Logger.d(String.format("\t [%d] version_minor : 0x%x \n", 
                    i, app_profile.version_minor));
            Logger.d(String.format("\t [%d] version_micro : 0x%x \n", 
                    i, app_profile.version_micro));
        }
        
        Logger.d(String.format("\t service_bound_flag : 0x%x \n",  service_bound_flag));
        Logger.d(String.format("\t vilsibility : 0x%x \n", visibility));
        Logger.d(String.format("\t application_priority : 0x%x \n", application_priority));
        Logger.d(String.format("\t transport_protocol_label : %s \n", 
                new String(transport_protocol_label)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 
                3 + (application_profiles.size()*5) + transport_protocol_label.length;
    }

}
