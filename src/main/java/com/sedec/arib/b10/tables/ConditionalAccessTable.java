package com.sedec.arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.b10.DescriptorFactory;
import com.sedec.arib.b10.descriptors.Descriptor;
import com.sedec.base.Table;
import com.sedec.util.Logger;

public class ConditionalAccessTable extends Table {
    private byte version_number;
    private byte current_next_indicator;
    private byte section_number;
    private byte last_section_number;
    private List<Descriptor> descriptors = new ArrayList<>();

    public ConditionalAccessTable(byte[] buffer) {
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

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    protected void __decode_table_body__() {
        skipOnBuffer(18);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);

        for ( int i=(section_length-5-4); i>0; ) {
            Descriptor desc = DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));

        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }
        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                        (checksum_CRC32 >> 24) & 0xff,
                        (checksum_CRC32 >> 16) & 0xff,
                        (checksum_CRC32 >> 8) & 0xff,
                        checksum_CRC32 & 0xff));
        Logger.d("====================================== \n\n");
    }


}
