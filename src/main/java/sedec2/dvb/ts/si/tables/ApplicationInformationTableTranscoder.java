package sedec2.dvb.ts.si.tables;

import java.util.List;

import sedec2.dvb.ts.si.DescriptorFactory;
import sedec2.dvb.ts.si.descriptors.Application;
import sedec2.dvb.ts.si.descriptors.ApplicationDescriptor;
import sedec2.dvb.ts.si.descriptors.Descriptor;
import sedec2.dvb.ts.si.descriptors.SimpleApplicationLocationDescriptor;
import sedec2.dvb.ts.si.descriptors.TransportProtocolDescriptor;

public class ApplicationInformationTableTranscoder extends ApplicationInformationTable {

    public ApplicationInformationTableTranscoder(byte[] buffer) {
        super(buffer);
    }

    @Override
    protected void __encode_prepare_table__() {
        common_descriptors_length = 0;
        for ( int i=0; i<common_descriptors.size(); i++ ) {
            common_descriptors_length += common_descriptors.get(i).getDescriptorLength();
        }

        application_loop_length = 0;
        for ( int i=0; i<applications.size(); i++ ) {
            application_loop_length += applications.get(i).getApplicationLength();
        }
    }

    @Override
    protected void __encode_write_table_body__() {
        writeOnBuffer(test_application_flag, 1);
        writeOnBuffer(application_type, 15);
        writeOnBuffer(0x03, 2);
        writeOnBuffer(version_number, 5);
        writeOnBuffer(current_next_indicator, 1);
        writeOnBuffer(section_number, 8);
        writeOnBuffer(last_section_number, 8);
        writeOnBuffer(0x0f, 4);
        writeOnBuffer(common_descriptors_length, 12);

        for ( int i=0; i<common_descriptors.size(); i++ ) {
            common_descriptors.get(i).writeDescriptor(this);
        }

        writeOnBuffer(0x0f, 4);
        writeOnBuffer(application_loop_length, 12);

        for ( int i=0; i<applications.size(); i++ ) {
            applications.get(i).writeApplication(this);
        }
    }

    @Override
    protected void __encode_update_table_length__() {
        section_length = 0;
        for ( int i=0; i<common_descriptors.size(); i++ ) {
            section_length += common_descriptors.get(i).getDescriptorLength();
        }
        for ( int i=0; i<applications.size(); i++ ) {
            section_length += applications.get(i).getApplicationLength();
        }
        section_length += 13; /* 9 + crc(4) */
    }

    protected Descriptor __findDescriptor__(List<Descriptor> descriptors, int tag) {
        for ( int i=0; i<descriptors.size(); i++ ) {
            if ( tag == descriptors.get(i).getDescriptorTag() ) {
                return descriptors.get(i);
            }
        }
        return null;
    }

    public void setApplicationUrl(byte[] base_url, byte[] init_path) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.SIMPLE_APPLICATION_LOCATION_DESCRIPTOR);
            ((SimpleApplicationLocationDescriptor)desc).setInitialPathBytes(init_path);

            desc = __findDescriptor__(app.getDescriptors(),
                    DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).setBaseUrl(base_url);
        }
    }

    public void setApplicationVersion(int major, int minor, int micro) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.APPLICATION_DESCRIPTOR);
            ((ApplicationDescriptor)desc).setApplicationVersion(major, minor, micro);
        }
    }

    public void setApplicationVisibility(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.APPLICATION_DESCRIPTOR);
            ((ApplicationDescriptor)desc).setVisibility(value);
        }
    }

    public void setApplicationId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            app.setApplicationId(value);
        }
    }

    public void setOrganizationId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            app.setOrganizationId(value);
        }
    }

    public void setRemoteConnection(byte value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).setRemoteConnection(value);
        }
    }

    public void setProtocolId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).setProtocolId(value);
        }
    }

    public void setOriginalNetworkId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).setOriginalNetworkId(value);
        }
    }

    public void setTransportStreamId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).setTransportStreamId(value);
        }
    }

    public void setServiceId(int value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).setServiceId(value);
        }
    }

    public void setComponentTag(byte value) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).setComponentTag(value);
        }
    }

    public void setTestApplicationFlag(int value) {
        test_application_flag = value;
    }

    public void setApplicationType(int value) {
        application_type = value;
    }

    public void setVersionNumber(int value) {
        version_number = value;
    }

    public void setCurrentNextIndicator(int value) {
        current_next_indicator = value;
    }

    public void setSectionNumber(int value) {
        section_number = value;
    }

    public void setLastSectionNumber(int value) {
        last_section_number = value;
    }

    public void setTransportProtocolLabel(byte[] label) {
        for ( int i=0; i<applications.size(); i++ ) {
            Application app = applications.get(i);
            Descriptor desc =
                    __findDescriptor__(app.getDescriptors(),
                                DescriptorFactory.APPLICATION_DESCRIPTOR);
            ((ApplicationDescriptor)desc).setTransportProtocolLabel(label);

            desc = __findDescriptor__(app.getDescriptors(),
                    DescriptorFactory.TRANSPORT_PROTOCOL_DESCRIPTOR);
            ((TransportProtocolDescriptor)desc).setTransportProtocolLabel(
                    Byte.parseByte(label.toString()));
        }
    }

    public void setCommonDescriptors(List<Descriptor> descriptors) {
        common_descriptors = descriptors;
    }

    public void setApplications(List<Application> _applications) {
        applications = _applications;
    }
}
