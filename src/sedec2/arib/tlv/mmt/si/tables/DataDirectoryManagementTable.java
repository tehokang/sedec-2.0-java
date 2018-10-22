package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Table;
import sedec2.util.Logger;

public class DataDirectoryManagementTable extends Table {
    protected byte num_of_base_directory;
    protected List<BaseDirectory> base_directories = new ArrayList<>();
    
    class BaseDirectory {
        public byte base_directory_path_length;
        public byte[] base_directory_path_byte;
        public int num_of_directory_nodes;
        public List<DirectoryNode> directory_nodes = new ArrayList<>();
    }
    
    class DirectoryNode {
        public int node_tag;
        public byte directory_node_version;
        public byte directory_node_path_length;
        public byte[] directory_node_path_byte;
        public int num_of_files;
        public List<File> files = new ArrayList<>();
    }
    
    class File {
        public int node_tag;
        public byte file_name_length;
        public byte[] file_name_byte;
    }
    
    public DataDirectoryManagementTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public byte GetNumOfBaseDirectory() {
        return num_of_base_directory;
    }
    
    public List<BaseDirectory> GetBaseDirectories() {
        return base_directories;
    }
    
    @Override
    protected void __decode_table_body__() {
        num_of_base_directory = (byte) ReadOnBuffer(8);
        
        for ( int i=0; i<num_of_base_directory; i++ ) {
            BaseDirectory base_directory = new BaseDirectory();
            base_directory.base_directory_path_length = (byte) ReadOnBuffer(8);
            base_directory.base_directory_path_byte = 
                    new byte[base_directory.base_directory_path_length];
            
            for ( int j=0; j<base_directory.base_directory_path_length; j++ ) {
                base_directory.base_directory_path_byte[j] = (byte) ReadOnBuffer(8);
            }
            
            base_directory.num_of_directory_nodes = ReadOnBuffer(16);
            
            for ( int j=0; j<base_directory.num_of_directory_nodes; j++ ) {
                DirectoryNode node = new DirectoryNode();
                node.node_tag = ReadOnBuffer(16);
                node.directory_node_version = (byte) ReadOnBuffer(8);
                node.directory_node_path_length = (byte) ReadOnBuffer(8);
                node.directory_node_path_byte = new byte[node.directory_node_path_length];
                
                for ( int k=0; k<node.directory_node_path_length; k++ ) {
                    node.directory_node_path_byte[k] = (byte) ReadOnBuffer(8);
                }
                
                node.num_of_files = ReadOnBuffer(16);
                
                for ( int k=0; k<node.num_of_files; k++ ) {
                    File file = new File();
                    file.node_tag = ReadOnBuffer(16);
                    file.file_name_length = (byte) ReadOnBuffer(8);
                    
                    file.file_name_byte = new byte[file.file_name_length];
                    for ( int m=0; m<file.file_name_length; m++ ) {
                        file.file_name_byte[m] = (byte) ReadOnBuffer(8);
                    }
                    node.files.add(file);
                }
                base_directory.directory_nodes.add(node);
            }
            base_directories.add(base_directory);
        }
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("num_of_base_directory : 0x%x \n", num_of_base_directory));
        
        
        for ( int i=0; i<base_directories.size(); i++ ) {
            BaseDirectory base_directory = base_directories.get(i);
            Logger.d(String.format("[%d] base_directory_path_length : 0x%x \n", 
                    i, base_directory.base_directory_path_length));
            Logger.d(String.format("[%d] base_directory_pat_byte : %s \n", 
                    i, new String(base_directory.base_directory_path_byte)));
            Logger.d(String.format("[%d] num_of_directory_nodes : 0x%x \n", 
                    i, base_directory.num_of_directory_nodes));
            
            for ( int j=0; j<base_directory.directory_nodes.size(); j++ ) {
                DirectoryNode node = base_directory.directory_nodes.get(j);
                Logger.d(String.format("\t [%d] node_tag : 0x%x \n", j, node.node_tag));
                Logger.d(String.format("\t [%d] directory_node_version : 0x%x \n", 
                        j, node.directory_node_version));
                Logger.d(String.format("\t [%d] directory_node_path_length : 0x%x \n", 
                        j, node.directory_node_path_length));
                Logger.d(String.format("\t [%d] directory_node_path_byte : %s \n", 
                        j, new String(node.directory_node_path_byte)));
                
                Logger.d(String.format("\t [%d] num_of_files : 0x%x \n", 
                        j, node.num_of_files));
                
                for ( int k=0; k<node.files.size(); k++ ) {
                    File file = node.files.get(k);
                    Logger.d(String.format("\t\t [%d] node_tag : 0x%x \n", k, file.node_tag));
                    Logger.d(String.format("\t\t [%d] file_name_length : 0x%x \n",
                            k, file.file_name_length));
                    Logger.d(String.format("\t\t [%d] file_name_byte : %s \n", 
                            k, new String(file.file_name_byte)));
                }
            }
        }
    }
}
