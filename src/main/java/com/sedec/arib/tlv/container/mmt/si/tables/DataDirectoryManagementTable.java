package com.sedec.arib.tlv.container.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.Table;
import com.sedec.util.Logger;

public class DataDirectoryManagementTable extends Table {
    protected byte data_transmission_session_id;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected byte base_directory_path_length;
    protected byte[] base_directory_path_byte;

    protected byte num_of_directory_nodes;
    protected List<DirectoryNode> directory_nodes = new ArrayList<>();

    public class DirectoryNode {
        public int node_tag;
        public byte directory_node_version;
        public byte directory_node_path_length;
        public byte[] directory_node_path_byte;
        public int num_of_files;
        public List<FileNode> files = new ArrayList<>();
    }

    public class FileNode {
        public int node_tag;
        public byte file_name_length;
        public byte[] file_name_byte;
    }

    public DataDirectoryManagementTable(byte[] buffer) {
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
        base_directory_path_length = (byte) readOnBuffer(8);
        base_directory_path_byte = new byte[base_directory_path_length];

        for ( int i=0; i<base_directory_path_byte.length; i++ ) {
            base_directory_path_byte[i] = (byte) readOnBuffer(8);
        }

        num_of_directory_nodes = (byte) readOnBuffer(8);

        for ( int i=0; i<num_of_directory_nodes; i++ ) {
            DirectoryNode directory_node = new DirectoryNode();
            directory_node.node_tag = readOnBuffer(16);
            directory_node.directory_node_version = (byte) readOnBuffer(8);
            directory_node.directory_node_path_length = (byte) readOnBuffer(8);
            directory_node.directory_node_path_byte =
                    new byte[directory_node.directory_node_path_length];

            for ( int k=0; k<directory_node.directory_node_path_byte.length; k++ ) {
                directory_node.directory_node_path_byte[k] = (byte) readOnBuffer(8);
            }

            directory_node.num_of_files = readOnBuffer(16);

            for ( int k=0; k<directory_node.num_of_files; k++ ) {
                FileNode file = new FileNode();
                file.node_tag = readOnBuffer(16);
                file.file_name_length = (byte) readOnBuffer(8);

                file.file_name_byte = new byte[file.file_name_length];
                for ( int m=0; m<file.file_name_length; m++ ) {
                    file.file_name_byte[m] = (byte) readOnBuffer(8);
                }
                directory_node.files.add(file);
            }
            directory_nodes.add(directory_node);
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

    public byte getBaseDirectoryPathLength() {
        return base_directory_path_length;
    }

    public byte[] getBaseDirectoryPath() {
        return base_directory_path_byte;
    }

    public byte getNumOfDirectoryNodes() {
        return num_of_directory_nodes;
    }

    public List<DirectoryNode> getDirectoryNodes() {
        return directory_nodes;
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("data_transmission_session_id : 0x%x \n",
                data_transmission_session_id));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n",
                current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("base_directory_path_length : 0x%x (%d) \n",
                base_directory_path_length, base_directory_path_length));
        Logger.d(String.format("base_directory_path_byte : %s \n",
                new String(base_directory_path_byte)));

        Logger.d(String.format("num_of_directory_nodes : 0x%x (%d) \n",
                num_of_directory_nodes, num_of_directory_nodes));

        for ( int i=0; i<directory_nodes.size(); i++ ) {
            DirectoryNode directory_node = directory_nodes.get(i);
            Logger.d(String.format("[%d] node_tag : 0x%x \n", i, directory_node.node_tag));
            Logger.d(String.format("[%d] directory_node_version : 0x%x \n",
                    i, directory_node.directory_node_version));
            Logger.d(String.format("[%d] directory_node_path_length : 0x%x \n",
                    i, directory_node.directory_node_path_length));
            Logger.d(String.format("[%d] directory_node_path : %s \n",
                    i, new String(directory_node.directory_node_path_byte)));

            Logger.d(String.format("[%d] num_of_files : 0x%x (%d) \n",
                    i, directory_node.num_of_files, directory_node.num_of_files ));

            for ( int k=0; k<directory_node.files.size(); k++ ) {
                FileNode file = directory_node.files.get(i);
                Logger.d(String.format("\t [%d] node_tag : 0x%x \n", k, file.node_tag));
                Logger.d(String.format("\t [%d] file_name_length : 0x%x (%d) \n",
                        k, file.file_name_length, file.file_name_length));
                Logger.d(String.format("\t [%d] file_name_byte : %s \n",
                        k, new String(file.file_name_byte)));
            }
        }

        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
