package hybridcast.descriptors;

import java.util.ArrayList;
import java.util.List;

import base.BitReadWriter;
import util.Logger;

/**
 * @brief ApplicationDescriptor
 * @note Verified
 */
public class ApplicationDescriptor extends Descriptor {
    private int application_profiles_length;
    private List<ApplicationProfile> application_profiles = new ArrayList<ApplicationProfile>();
    private int service_bound_flag;
    private int visibility;
    private int application_priority;
    private byte[] transport_protocol_label;
    private int transport_protocol_label_length;
    
    public class ApplicationProfile {
        public int application_profile;
        public int version_major;
        public int version_minor;
        public int version_micro;
    };
    
    public ApplicationDescriptor(BitReadWriter brw) {
        super(brw);
        
        application_profiles_length = brw.ReadOnBuffer(8);
        for ( int i=application_profiles_length; i>0; i-=5) {
            ApplicationProfile application_profile = new ApplicationProfile();
            
            application_profile.application_profile = brw.ReadOnBuffer(16);
            application_profile.version_major = brw.ReadOnBuffer(8);
            application_profile.version_minor = brw.ReadOnBuffer(8);
            application_profile.version_micro = brw.ReadOnBuffer(8);
            
            application_profiles.add(application_profile);
        }
        
        service_bound_flag = brw.ReadOnBuffer(1);
        visibility = brw.ReadOnBuffer(2);
        brw.SkipOnBuffer(5);
        application_priority = brw.ReadOnBuffer(8);
        
        transport_protocol_label_length = 
                descriptor_length - 1 - (application_profiles_length > 0 ? 5:0) - 2;
        
        if ( 0 < transport_protocol_label_length ) {
            transport_protocol_label = new byte[transport_protocol_label_length];
            for ( int i=0; i<transport_protocol_label_length; i++ ) {
                transport_protocol_label[i] = (byte) brw.ReadOnBuffer(8);
            }
        }
    }
    
    public void SetApplicationVersion(int major, int minor, int micro)
    {
        for ( int i=application_profiles_length; i>0; i-=5) {
            application_profiles.get(i).version_major = major;
            application_profiles.get(i).version_minor = minor;
            application_profiles.get(i).version_micro = micro;
        }
    }
    
    public void SetVisibility(int value) {
        visibility = value;
    }
    
    public void SetApplicationPriority(int value ) {
        application_priority = value;
    }
    
    public void SetTransportProtocolLabel(byte[] value) {
        transport_protocol_label_length = value.length;
        System.arraycopy(value, 0, transport_protocol_label, 0, value.length);
    }
    
    public int GetApplicationVersionMajor(int application_index) {
        return application_profiles.get(application_index).version_major;
    }
    
    public int GetApplicationVersionMinor(int application_index) {
        return application_profiles.get(application_index).version_minor;
    }
    
    public int GetApplicationVersionMicro(int application_index) {
        return application_profiles.get(application_index).version_micro;
    }
    
    public int GetServiceBoundFlag() {
        return service_bound_flag;
    }
    
    public int GetVisility() {
        return visibility;
    }
    
    public int GetApplicationPriority() {
        return application_priority;
    }
    
    public byte[] GetTransportProtocolLabel() {
        return transport_protocol_label;
    }
    
    public int GetTransportProtocolLabelLength() {
        return transport_protocol_label_length;
    }
    
    @Override
    public void PrintDescriptor() {
        Logger.d("\n");
        super._PrintDescriptor_("ApplicationDescriptor");

        Logger.d(String.format("\tapplication_profiles_length : 0x%x \n", application_profiles_length));
        for ( int i=0 ; i<application_profiles.size(); i++ ) {
            Logger.d(String.format("\tapplication_profile : 0x%x \n", application_profiles.get(i).application_profile));
            Logger.d(String.format("\tversion_major : 0x%x \n", application_profiles.get(i).version_major));
            Logger.d(String.format("\tversion_minor : 0x%x \n", application_profiles.get(i).version_minor));
            Logger.d(String.format("\tversion_micro : 0x%x \n", application_profiles.get(i).version_micro));
        }
        Logger.d(String.format("\tservice_bound_flag : 0x%x \n", service_bound_flag));
        Logger.d(String.format("\tvisibility : 0x%x \n", visibility));
        Logger.d(String.format("\tapplication_priority : 0x%x \n", application_priority));
        for ( int i=0;i<transport_protocol_label_length;i++ ) {
            Logger.d(String.format("\ttransport_protocol_label[%d] : 0x%x \n", i, transport_protocol_label[i]));
        }
        Logger.d("\n");
    }
    
    @Override
    protected void updateDescriptorLength() {
        descriptor_length =
                1 + (application_profiles_length>0 ? 5:0) + 2 + transport_protocol_label_length;
        
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(application_profiles_length, 8);
        for(int i=application_profiles_length;i>0;i-=5)
        {
            brw.WriteOnBuffer(application_profiles.get(i).application_profile, 16);
            brw.WriteOnBuffer(application_profiles.get(i).version_major, 8);
            brw.WriteOnBuffer(application_profiles.get(i).version_minor, 8);
            brw.WriteOnBuffer(application_profiles.get(i).version_micro, 8);
        }
        brw.WriteOnBuffer(service_bound_flag, 1);
        brw.WriteOnBuffer(visibility, 2);
        brw.WriteOnBuffer(0x1f, 5);
        brw.WriteOnBuffer(application_priority, 8);

        for(int i=0;i<transport_protocol_label_length;i++)
        {
            brw.WriteOnBuffer(transport_protocol_label[i], 8);
        }
    }
}
