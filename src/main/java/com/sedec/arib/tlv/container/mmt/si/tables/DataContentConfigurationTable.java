package com.sedec.arib.tlv.container.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.tlv.container.mmt.si.DescriptorFactory;
import com.sedec.arib.tlv.container.mmt.si.descriptors.Descriptor;
import com.sedec.util.Logger;

public class DataContentConfigurationTable extends com.sedec.base.Table {
    protected byte data_transmission_session_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected int content_id;
    protected byte content_version;
    protected int content_size;
    protected byte PU_info_flag;
    protected byte content_info_flag;

    protected byte number_of_PUs;
    protected List<PU> pus = new ArrayList<>();

    protected int number_of_nodes;
    protected int[] node_tag;
    protected byte content_descriptor_loop_length;
    protected List<Descriptor> content_descriptors = new ArrayList<>();

    public class PU {
        public byte PU_tag;
        public int PU_size;
        public byte number_of_member_nodes;
        public int[] node_tag;
        public byte PU_descriptor_loop_length;
        public List<Descriptor> pu_desctiptors = new ArrayList<>();
    }

    public DataContentConfigurationTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        data_transmission_session_id = (byte) readOnBuffer(8);
        skipOnBuffer(10);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);
        content_id = readOnBuffer(16);
        content_version = (byte) readOnBuffer(8);
        content_size = readOnBuffer(32);
        PU_info_flag = (byte) readOnBuffer(1);
        content_info_flag =(byte) readOnBuffer(1);
        skipOnBuffer(6);

        if ( PU_info_flag == 1 ) {
            number_of_PUs = (byte) readOnBuffer(8);

            for ( int i=0; i<number_of_PUs; i++ ) {
                PU pu = new PU();
                pu.PU_tag = (byte) readOnBuffer(8);
                pu.PU_size = readOnBuffer(32);
                pu.number_of_member_nodes = (byte) readOnBuffer(8);
                pu.node_tag = new int[pu.number_of_member_nodes];

                for ( int k=0; k<pu.number_of_member_nodes; k++ ) {
                    pu.node_tag[k] = readOnBuffer(16);
                }
                pu.PU_descriptor_loop_length = (byte) readOnBuffer(8);
                for ( int k=pu.PU_descriptor_loop_length; k>0; ) {
                    Descriptor desc = DescriptorFactory.createDescriptor(this);
                    pu.pu_desctiptors.add(desc);
                    k-=desc.getDescriptorLength();
                }
                pus.add(pu);
            }
        } else {
            number_of_nodes = (byte) readOnBuffer(16);
            node_tag = new int[number_of_nodes];
            for ( int i=0; i<number_of_nodes; i++ ) {
                node_tag[i] = readOnBuffer(16);
            }
        }

        if ( content_info_flag == 1 ) {
            content_descriptor_loop_length = (byte) readOnBuffer(8);
            for ( int i=content_descriptor_loop_length; i>0; ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                content_descriptors.add(desc);
                i-=desc.getDescriptorLength();
            }
        }
        checksum_CRC32 = readOnBuffer(32);
    }

    public byte getDataTransmissionSessionId() {
        return data_transmission_session_id;
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

    public int getContentId() {
        return content_id;
    }

    public byte getContentVersion() {
        return content_version;
    }

    public int getContentSize() {
        return content_size;
    }

    public byte getPUInfoFlag() {
        return PU_info_flag;
    }

    public byte getContentInfoFlag() {
        return content_info_flag;
    }

    public byte getNumberOfPUs() {
        return number_of_PUs;
    }

    public List<PU> getPUs() {
        return pus;
    }

    public int getNumberOfNodes() {
        return number_of_nodes;
    }

    public int[] getNodeTag() {
        return node_tag;
    }

    public List<Descriptor> getContentDescriptor() {
        return content_descriptors;
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("data_transmission_session_id : 0x%x \n",
                data_transmission_session_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("content_id : 0x%x \n", content_id));
        Logger.d(String.format("content_version : 0x%x \n", content_version));
        Logger.d(String.format("content_size : 0x%x (%d) \n", content_size, content_size));
        Logger.d(String.format("PU_info_flag : 0x%x \n", PU_info_flag));
        Logger.d(String.format("content_info_flag : 0x%x \n", content_info_flag));

        if ( PU_info_flag == 1 ) {
            Logger.d(String.format("number_of_PUs : 0x%x (%d) \n",
                    number_of_PUs, number_of_PUs));

            for ( int i=0; i<pus.size(); i++ ) {
                PU pu = pus.get(i);
                Logger.d(String.format("[%d] PU_tag : 0x%x \n", i, pu.PU_tag));
                Logger.d(String.format("[%d] PU_size : 0x%x (%d) \n",
                        i, pu.PU_size, pu.PU_size));
                Logger.d(String.format("[%d] number_of_member_nodes : 0x%x (%d) \n",
                        i, pu.number_of_member_nodes, pu.number_of_member_nodes));

                for ( int k=0; k<pu.node_tag.length; k++ ) {
                    Logger.d(String.format("\t [%d] node_tag : 0x%x \n",
                            k, pu.node_tag[k]));
                }
                Logger.d(String.format("[%d] PU_descriptor_loop_length : 0x%x (%d) \n",
                        i, pu.PU_descriptor_loop_length, pu.PU_descriptor_loop_length));
                for ( int k=0; k<pu.pu_desctiptors.size(); k++) {
                    pu.pu_desctiptors.get(k).print();
                }
            }
        } else {
            Logger.d(String.format("number_of_nodes : 0x%x (%d) \n", number_of_nodes));
            for ( int i=0; i<node_tag.length; i++ ) {
                Logger.d(String.format("[%d] node_tag : 0x%x \n", i, node_tag[i]));
            }
        }

        if ( content_info_flag == 1 ) {
            Logger.d(String.format("content_descriptor_loop_length : 0x%x (%d) \n",
                    content_descriptor_loop_length, content_descriptor_loop_length));
            for ( int i=0; i<content_descriptors.size(); i++) {
                content_descriptors.get(i).print();
            }
        }

        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
