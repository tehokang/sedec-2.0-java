package com.sedec.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.b10.DescriptorFactory;
import com.sedec.arib.b10.descriptors.Descriptor;
import com.sedec.base.Table;
import com.sedec.util.Logger;

/**
 * ARIB-B10 LDT
 */
public class LinkedDescriptionTable extends Table {
    protected int original_service_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int transport_stream_id;
    protected int original_network_id;
    protected List<LinkedDescription> linked_descriptions = new ArrayList<>();

    public class LinkedDescription {
        public int description_id;
        public int descriptors_loop_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }

    public LinkedDescriptionTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public int getOriginalServiceId() {
        return original_service_id;
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

    public int getTransportStreamId() {
        return transport_stream_id;
    }

    public int getOriginalNetworkId() {
        return original_network_id;
    }

    public List<LinkedDescription> getLinkedDescriptions() {
        return linked_descriptions;
    }

    @Override
    protected void __decode_table_body__() {
        original_service_id = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        transport_stream_id = readOnBuffer(16);
        original_network_id = readOnBuffer(16);

        for ( int i=(section_length-9-4); i>0; ) {
            LinkedDescription linked_description = new LinkedDescription();
            linked_description.description_id = readOnBuffer(16);
            skipOnBuffer(12);
            linked_description.descriptors_loop_length = readOnBuffer(12);

            for ( int j=linked_description.descriptors_loop_length; j>0; ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                j-=desc.getDescriptorLength();
                linked_description.descriptors.add(desc);
            }
            i-=(5 + linked_description.descriptors_loop_length);
            linked_descriptions.add(linked_description);
        }

        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("original_service_id : 0x%x \n", original_service_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("transport_stream_id : 0x%x \n", transport_stream_id));
        Logger.d(String.format("original_network_id : 0x%x \n", original_network_id));

        for ( int i=0; i<linked_descriptions.size(); i++ ) {
            LinkedDescription linked_description = linked_descriptions.get(i);
            Logger.d(String.format("\t [%d] description_id : 0x%x \n",
                    i, linked_description.description_id));
            Logger.d(String.format("\t [%d] descriptors_loop_length : 0x%x \n",
                    i, linked_description.descriptors_loop_length));

            for ( int j=0; j<linked_description.descriptors.size(); j++ ) {
                linked_description.descriptors.get(j).print();
            }
        }

        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
