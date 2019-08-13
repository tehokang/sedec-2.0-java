package com.sedec.arib.tlv.container.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.tlv.container.mmt.si.DescriptorFactory;
import com.sedec.arib.tlv.container.mmt.si.descriptors.Descriptor;
import com.sedec.base.Table;
import com.sedec.util.Logger;

public class EventMessageTable extends Table {
    protected byte data_event_id;
    protected int event_msg_group_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected List<Descriptor> descriptors = new ArrayList<>();

    public EventMessageTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public int getDataEventId() {
        return data_event_id;
    }

    public int getEventMsgGroupId() {
        return event_msg_group_id;
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
        data_event_id = (byte) readOnBuffer(4);
        event_msg_group_id = (byte) readOnBuffer(12);
        skipOnBuffer(2);
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

        Logger.d(String.format("data_event_id : 0x%x \n", data_event_id));
        Logger.d(String.format("event_msg_group_id : 0x%x \n", event_msg_group_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));

        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }
    }
}
