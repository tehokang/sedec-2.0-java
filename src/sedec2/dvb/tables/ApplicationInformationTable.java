package sedec2.dvb.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Table;
import sedec2.dvb.DescriptorFactory;
import sedec2.dvb.descriptors.Application;
import sedec2.dvb.descriptors.Descriptor;
import sedec2.util.Logger;

/**
 * @brief TS 102-812 AIT
 */
public class ApplicationInformationTable extends Table {

    protected int test_application_flag;
    protected int application_type;
    protected int version_number;
    protected int current_next_indicator;
    protected int section_number;
    protected int last_section_number;
    protected int common_descriptors_length;
    protected List<Descriptor> common_descriptors = new ArrayList<>();
    protected int application_loop_length;
    protected List<Application> applications = new ArrayList<>();

    public ApplicationInformationTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public int getTestApplicationFlag() {
        return test_application_flag;
    }

    public int getApplicationType() {
        return application_type;
    }

    public int getVersionNumber() {
        return version_number;
    }

    public int getCurrentNextIndicator() {
        return current_next_indicator;
    }

    public int getSectionNumber() {
        return section_number;
    }

    public int getLastSectionNumber() {
        return last_section_number;
    }

    public int getCommonDescriptorLength() {
        return common_descriptors_length;
    }

    public List<Descriptor> getCommonDescriptors() {
        return common_descriptors;
    }

    public int getApplicationLoopLength() {
        return application_loop_length;
    }

    public List<Application> getApplications() {
        return applications;
    }

    @Override
    protected void __decode_table_body__() {
        test_application_flag = readOnBuffer(1);
        application_type = readOnBuffer(15);
        skipOnBuffer(2);
        version_number = readOnBuffer(5);
        current_next_indicator = readOnBuffer(1);
        section_number = readOnBuffer(8);
        last_section_number = readOnBuffer(8);
        skipOnBuffer(4);

        common_descriptors_length = readOnBuffer(12);
        for ( int i=common_descriptors_length;i>0; ) {
            Descriptor desc = DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            common_descriptors.add(desc);
        }

        skipOnBuffer(4);

        application_loop_length = readOnBuffer(12);
        for ( int i=application_loop_length;i>0; ) {
            Application app = new Application(this);
            i-=app.getApplicationLength();
            applications.add(app);
        }
        super.checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("test_application_flag : 0x%x \n", test_application_flag));
        Logger.d(String.format("application_type : 0x%x \n", application_type));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("common_descriptors_length : 0x%x \n", common_descriptors_length));

        for ( int i=0; i<common_descriptors.size(); i++ ) {
            common_descriptors.get(i).print();
        }

        Logger.d(String.format("application_loop_length : 0x%x \n", application_loop_length));
        Logger.d("\n");
        for ( int i=0; i<applications.size(); i++ ) {
            Logger.d("====== [" + i + "] Application list ======\n");
            applications.get(i).printApplication();
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
