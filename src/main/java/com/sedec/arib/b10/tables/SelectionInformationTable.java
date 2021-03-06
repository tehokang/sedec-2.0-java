package com.sedec.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.b10.DescriptorFactory;
import com.sedec.arib.b10.descriptors.Descriptor;
import com.sedec.base.Table;
import com.sedec.util.Logger;

public class SelectionInformationTable extends Table {
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int transmission_info_loop_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    protected List<Service> services = new ArrayList<>();

    public class Service {
        public int service_id;
        public byte running_status;
        public int service_loop_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }

    public SelectionInformationTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public byte getVersionNumber() {
        return version_number;
    }

    public byte getCurrentNextIndicator() {
        return current_next_indicator;
    }

    public byte getSectionNumber() {
        return section_number;
    }

    public byte getLastSectionNumber() {
        return last_section_number;
    }

    public int getTransmissionInfoLoopLength() {
        return transmission_info_loop_length;
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    public List<Service> getServices() {
        return services;
    }

    @Override
    protected void __decode_table_body__() {
        skipOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        skipOnBuffer(4);
        transmission_info_loop_length = readOnBuffer(12);

        for ( int i=transmission_info_loop_length; i>0; ) {
            Descriptor desc = DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }

        for ( int i=(section_length - 7 - transmission_info_loop_length - 4); i>0; ) {
            Service service = new Service();
            service.service_id = readOnBuffer(16);
            skipOnBuffer(1);
            service.running_status = (byte) readOnBuffer(3);
            service.service_loop_length = readOnBuffer(12);

            for ( int j=service.service_loop_length; j>0; ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                j-=desc.getDescriptorLength();
                service.descriptors.add(desc);
            }
            services.add(service);
        }

        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("version_number : 0x%x \n",  version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n",
                current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("transmisstion_info_loop_length : 0x%x \n",
                transmission_info_loop_length));

        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }

        for ( int i=0; i<services.size(); i++ ) {
            Service service = services.get(i);
            Logger.d(String.format("\t [%d] service_id : 0x%x \n", i, service.service_id));
            Logger.d(String.format("\t [%d] running_status : 0x%x \n",
                    i, service.running_status));

            Logger.d(String.format("\t [%d] service_loop_length : 0x%x \n",
                    i, service.service_loop_length));

            for ( int j=0; j<service.descriptors.size(); j++ ) {
                service.descriptors.get(j).print();
            }
        }
    }
}
