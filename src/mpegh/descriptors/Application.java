package mpegh.descriptors;

import java.util.ArrayList;
import java.util.List;

import base.BitReadWriter;
import mpegh.DescriptorFactory;
import util.Logger;

public class Application {

    private int organization_id;
    private int application_id;
    private int application_control_code;
    private int application_descriptors_loop_length;
    private List<Descriptor> application_descriptors = new ArrayList<Descriptor>();
    
    private int m_application_length;
    
    public Application(BitReadWriter brw) {
        organization_id = brw.ReadOnBuffer(32);
        application_id = brw.ReadOnBuffer(16);
        application_control_code = brw.ReadOnBuffer(8);
        brw.SkipOnBuffer(4);
        application_descriptors_loop_length = brw.ReadOnBuffer(12);
        System.out.println("application_descriptors_loop_length : " + application_descriptors_loop_length);
        for ( int i=application_descriptors_loop_length; i>0 ; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(brw);
            desc.PrintDescriptor();
            i-=desc.GetDescriptorLength();
            System.out.println("i : " + i);
            application_descriptors.add(desc);
        }
    }
    
    public int GetApplicationLength() {
        updateDescriptorLength();
        return m_application_length;
    }
    
    public void PrintApplication() {
        Logger.d(String.format("organization_id : 0x%x \n", organization_id));
        Logger.d(String.format("application_id : 0x%x \n", application_id));
        Logger.d(String.format("application_control_code : 0x%x \n", 
                application_control_code));
        Logger.d(String.format("application_descriptors_loop_length : 0x%x \n", 
                application_descriptors_loop_length));

        for ( int i=0; i<application_descriptors.size(); i++ ) {
            application_descriptors.get(i).PrintDescriptor();
        }
    }
    
    public int GetOrganizationId() {
        return organization_id;
    }
    
    public void SetOrganizationId(int org_id) {
        organization_id = org_id;
    }
    
    public int GetApplicationId() {
        return application_id;
    }
    
    public void SetApplicationId(int app_id) {
        application_id = app_id;
    }
    
    public int GetApplicationControlCode() {
        return application_control_code;
    }
    
    public void SetApplicationControlCode(int control_code) {
        application_control_code = control_code;
    }
    
    public List<Descriptor>GetDescriptors() {
        return application_descriptors;
    }
    
    public void SetDescriptors(List<Descriptor>descriptors) {
        application_descriptors.clear();
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            application_descriptors.add(i, descriptors.get(i));
        }
    }
    
    public int GetApplicationDescriptorLength() {
        return application_descriptors_loop_length;
    }
    
    public void WriteApplication(BitReadWriter brw) {
        brw.WriteOnBuffer(organization_id, 32);
        brw.WriteOnBuffer(application_id, 16);
        brw.WriteOnBuffer(application_control_code, 8);
        brw.WriteOnBuffer(0x0f, 4);
        brw.WriteOnBuffer(application_descriptors_loop_length, 12);

        for ( int i=0; i<application_descriptors.size(); i++ ) {
            application_descriptors.get(i).WriteDescriptor(brw);
        }
    }
    
    private void updateDescriptorLength() {
        application_descriptors_loop_length = 0;
        for ( int i=0; i<application_descriptors.size(); i++ ) {
            Descriptor desc = application_descriptors.get(i);
            application_descriptors_loop_length+=desc.GetDescriptorLength();
        }
        m_application_length = (9 + application_descriptors_loop_length);
    }
}
