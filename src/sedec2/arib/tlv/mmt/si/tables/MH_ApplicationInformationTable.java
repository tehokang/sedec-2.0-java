package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.descriptors.Application;
import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.Logger;

public class MH_ApplicationInformationTable extends Table {

    protected int application_type;
    protected int version_number;
    protected int current_next_indicator;
    protected int section_number;
    protected int last_section_number;
    protected int common_descriptors_length;
    protected List<Descriptor> common_descriptors = new ArrayList<>();
    protected int application_loop_length;
    protected List<Application> applications = new ArrayList<>();
    
    public MH_ApplicationInformationTable(byte[] buffer) {
        super(buffer);
       
        __decode_table_body__();
    }
    
    public int GetApplicationType() {
        return application_type;
    }
    
    public int GetVersionNumber() {
        return version_number;
    }
    
    public int GetCurrentNextIndicator() {
        return current_next_indicator;
    }
    
    public int GetSectionNumber() {
        return section_number;
    }
    
    public int GetLastSectionNumber() {
        return last_section_number;
    }
    
    public int GetCommonDescriptorLength() {
        return common_descriptors_length;
    }
    
    public List<Descriptor> GetCommonDescriptors() {
        return common_descriptors;
    }
    
    public int GetApplicationLoopLength() {
        return application_loop_length;
    }
    
    public List<Application> GetApplications() {
        return applications;
    }
    
    @Override
    protected void __decode_table_body__() {
        application_type = ReadOnBuffer(16);
        SkipOnBuffer(2);
        version_number = ReadOnBuffer(5);
        current_next_indicator = ReadOnBuffer(1);
        section_number = ReadOnBuffer(8);
        last_section_number = ReadOnBuffer(8);
        SkipOnBuffer(4);
        
        common_descriptors_length = ReadOnBuffer(12);
        for ( int i=common_descriptors_length;i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            common_descriptors.add(desc);
        }
        
        SkipOnBuffer(4);
        
        application_loop_length = ReadOnBuffer(12);
        for ( int i=application_loop_length;i>0; ) {
            Application app = new Application(this);
            i-=app.GetApplicationLength();
            applications.add(app);
        }
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("application_type : 0x%x \n", application_type));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("common_descriptors_length : 0x%x \n", common_descriptors_length));

        for ( int i=0; i<common_descriptors.size(); i++ ) {
            common_descriptors.get(i).PrintDescriptor();
        }
        
        Logger.d(String.format("application_loop_length : 0x%x \n", application_loop_length));
        Logger.d("\n");
        for ( int i=0; i<applications.size(); i++ ) {
            Logger.d("====== [" + i + "] Application list ======\n");
            applications.get(i).PrintApplication();
            Logger.d("--------------------------------------\n");
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%x%x%x%x \n", 
                ((checksum_CRC32 >> 24) & 0xff), 
                ((checksum_CRC32 >> 16) & 0xff), 
                ((checksum_CRC32 >> 8) & 0xff), 
                (checksum_CRC32 & 0xff)));
        Logger.d("====================================== \n\n");
    }
}
