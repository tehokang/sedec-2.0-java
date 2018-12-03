package sedec2.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.dvb.ts.si.DescriptorFactory;
import sedec2.util.Logger;

public class Application {

    private int organization_id;
    private int application_id;
    private int application_control_code;
    private int application_descriptors_loop_length;
    private List<Descriptor> application_descriptors = new ArrayList<Descriptor>();

    private int m_application_length;

    public Application(BitReadWriter brw) {
        organization_id = brw.readOnBuffer(32);
        application_id = brw.readOnBuffer(16);
        application_control_code = brw.readOnBuffer(8);
        brw.skipOnBuffer(4);
        application_descriptors_loop_length = brw.readOnBuffer(12);

        for ( int i=application_descriptors_loop_length; i>0 ;) {
            Descriptor desc = DescriptorFactory.createDescriptor(brw);
            i-=desc.getDescriptorLength();
            application_descriptors.add(desc);
        }
    }

    public int getApplicationLength() {
        updateDescriptorLength();
        return m_application_length;
    }

    public void printApplication() {
        Logger.d(String.format("organization_id : 0x%x \n", organization_id));
        Logger.d(String.format("application_id : 0x%x \n", application_id));
        Logger.d(String.format("application_control_code : 0x%x \n",
                application_control_code));
        Logger.d(String.format("application_descriptors_loop_length : 0x%x \n",
                application_descriptors_loop_length));

        for ( int i=0; i<application_descriptors.size(); i++ ) {
            application_descriptors.get(i).print();
        }
    }

    public int getOrganizationId() {
        return organization_id;
    }

    public void setOrganizationId(int org_id) {
        organization_id = org_id;
    }

    public int getApplicationId() {
        return application_id;
    }

    public void setApplicationId(int app_id) {
        application_id = app_id;
    }

    public int getApplicationControlCode() {
        return application_control_code;
    }

    public void setApplicationControlCode(int control_code) {
        application_control_code = control_code;
    }

    public List<Descriptor> getDescriptors() {
        return application_descriptors;
    }

    public void setDescriptors(List<Descriptor>descriptors) {
        application_descriptors.clear();

        for ( int i=0; i<descriptors.size(); i++ ) {
            application_descriptors.add(i, descriptors.get(i));
        }
    }

    public int getApplicationDescriptorLength() {
        return application_descriptors_loop_length;
    }

    public void writeApplication(BitReadWriter brw) {
        brw.writeOnBuffer(organization_id, 32);
        brw.writeOnBuffer(application_id, 16);
        brw.writeOnBuffer(application_control_code, 8);
        brw.writeOnBuffer(0x0f, 4);
        brw.writeOnBuffer(application_descriptors_loop_length, 12);

        for ( int i=0; i<application_descriptors.size(); i++ ) {
            application_descriptors.get(i).writeDescriptor(brw);
        }
    }

    private void updateDescriptorLength() {
        application_descriptors_loop_length = 0;
        for ( int i=0; i<application_descriptors.size(); i++ ) {
            Descriptor desc = application_descriptors.get(i);
            application_descriptors_loop_length+=desc.getDescriptorLength();
        }
        m_application_length = (9 + application_descriptors_loop_length);
    }
}
