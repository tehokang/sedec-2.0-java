package dvb.tables;

import java.util.List;

import dvb.DescriptorFactory;
import dvb.descriptors.Application;
import dvb.descriptors.ApplicationDescriptor;
import dvb.descriptors.Descriptor;
import dvb.descriptors.SimpleApplicationLocationDescriptor;
import dvb.descriptors.TransportProtocolDescriptor;

public class ApplicationInformationTableTranscoder extends ApplicationInformationTable {

    public ApplicationInformationTableTranscoder(byte[] buffer) {
        super(buffer);
    }
    
    @Override
    protected void __encode_preprare_table__() {
        common_descriptors_length = 0;
        for ( int i=0; i<common_descriptors.size(); i++ ) {
            common_descriptors_length += common_descriptors.get(i).GetDescriptorLength();
        }

        application_loop_length = 0;
        for ( int i=0; i<applications.size(); i++ ) {
            application_loop_length += applications.get(i).GetApplicationLength();
        }
    }
    
    @Override
    protected void __encode_write_table_body__() {
        WriteOnBuffer(test_application_flag, 1);
        WriteOnBuffer(application_type, 15);
        WriteOnBuffer(0x03, 2);
        WriteOnBuffer(version_number, 5);
        WriteOnBuffer(current_next_indicator, 1);
        WriteOnBuffer(section_number, 8);
        WriteOnBuffer(last_section_number, 8);
        WriteOnBuffer(0x0f, 4);
        WriteOnBuffer(common_descriptors_length, 12);

        for ( int i=0; i<common_descriptors.size(); i++ ) {
            common_descriptors.get(i).WriteDescriptor(this);
        }

        WriteOnBuffer(0x0f, 4);
        WriteOnBuffer(application_loop_length, 12);
        
        for ( int i=0; i<applications.size(); i++ ) {
            applications.get(i).WriteApplication(this);
        }
    }

    @Override
    protected void __encode_update_table_length__() {
        section_length = 0;
        for ( int i=0; i<common_descriptors.size(); i++ ) {
            section_length += common_descriptors.get(i).GetDescriptorLength();
        }
        for ( int i=0; i<applications.size(); i++ ) {
            section_length += applications.get(i).GetApplicationLength();
        }
        section_length += 13; /* 9 + crc(4) */
    }

    protected Descriptor __findDescriptor__(List<Descriptor> descriptors, int tag) {
        for ( int i=0; i<descriptors.size(); i++ ) {
            if ( tag == descriptors.get(i).GetDescriptorTag() ) {
                return descriptors.get(i);
            }
        }
        return null;
    }
    
    public void SetApplicationUrl(byte[] base_url, byte[] init_path) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.SIMPLE_APPLICATION_LOCATION_DESCRIPTOR);
            ((SimpleApplicationLocationDescriptor)desc).SetInitialPathBytes(init_path);
            
            desc = __findDescriptor__(app.GetDescriptors(), 
                    DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).SetBaseUrl(base_url);
        }
    }
    
    public void SetApplicationVersion(int major, int minor, int micro) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.APPLICATION_DESCRIPTOR);
            ((ApplicationDescriptor)desc).SetApplicationVersion(major, minor, micro);
        }
    }
    
    public void SetApplicationVisibility(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.APPLICATION_DESCRIPTOR);
            ((ApplicationDescriptor)desc).SetVisibility(value);
        }
    }
    
    public void SetApplicationId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            app.SetApplicationId(value);
        }
    }
    
    public void SetOrganizationId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            app.SetOrganizationId(value);
        }
    }
    
    public void SetRemoteConnection(byte value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).SetRemoteConnection(value);
        }
    }
    
    public void SetProtocolId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).SetProtocolId(value);
        }
    }
    
    public void SetOriginalNetworkId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).SetOriginalNetworkId(value);
        }
    }
    
    public void SetTransportStreamId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).SetTransportStreamId(value);
        }
    }
    
    public void SetServiceId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).SetServiceId(value);
        }
    }
    
    public void SetComponentTag(byte value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).SetComponentTag(value);
        }
    }
    
    public void SetTestApplicationFlag(int value) { 
        test_application_flag = value;
    }
    
    public void SetApplicationType(int value) { 
        application_type = value;
    }
    
    public void SetVersionNumber(int value) { 
        version_number = value;
    }
    
    public void SetCurrentNextIndicator(int value) {
        current_next_indicator = value;
    }
    
    public void SetSectionNumber(int value) { 
        section_number = value;
    }
    
    public void SetLastSectionNumber(int value) { 
        last_section_number = value;
    }
    
    public void SetTransportProtocolLabel(byte[] label) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc = 
                    __findDescriptor__(app.GetDescriptors(), 
                                DescriptorFactory.APPLICATION_DESCRIPTOR);
            ((ApplicationDescriptor)desc).SetTransportProtocolLabel(label);
            
            desc = __findDescriptor__(app.GetDescriptors(), 
                    DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).SetTransportProtocolLabel(
                    Byte.parseByte(label.toString()));
        }
    }
    
    public void SetCommonDescriptors(List<Descriptor> descriptors) {
        common_descriptors = descriptors;
    }
    
    public void SetApplications(List<Application> _applications) {
        applications = _applications;
    }
}
