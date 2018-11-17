package zexamples.arib;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

import sedec2.util.BinaryLogger;

/**
 * Application class as example to control informations of application which can be download
 */
public class SimpleApplication {
    public String application_root_path;
    public String base_directory_path;
    public List<SubDirectory> sub_directories = new ArrayList<>();

    public SimpleApplication(String rootpath) {
        application_root_path = rootpath;
    }
    
    public void done() {
        java.io.File root_dir = new java.io.File(application_root_path + "/" + base_directory_path);
        root_dir.mkdirs();
        
        for ( int i=0; i<sub_directories.size(); i++ ) {
            sub_directories.get(i).done(root_dir.toPath().toString());
        }
    }
    
    public void print() {
        System.out.print(String.format("base_directory_path : %s \n",
                base_directory_path));
        for ( int i=0; i<sub_directories.size(); i++ ) {
            SubDirectory sub_directory = sub_directories.get(i);
            sub_directory.print();
        }
    }
    
    public class SubDirectory {
        public String sub_directory_path;
        public int node_tag;
        public int mpu_sequence_number;
        public List<File> files = new ArrayList<>();
        
        public void done(String parent) {
            java.io.File sub_dir = new java.io.File(parent + "/" + sub_directory_path);
            sub_dir.mkdirs();
            
            for ( int i=0; i<files.size(); i++ ) {
                File file = files.get(i);
                file.done(sub_dir.toPath().toString());
            }
        }
        
        public void print() {
            System.out.print(String.format("\t sub_directory_path : %s \n", 
                    sub_directory_path));
            System.out.print(String.format("\t node_tag : 0x%x \n",  node_tag));
            System.out.print(String.format("\t mpu_sequence_number : 0x%x \n", 
                    mpu_sequence_number));
            
            for ( int i=0; i<files.size(); i++ ) {
                File file = files.get(i);
                file.print();
            }
        }
    }
    
    public class File {
        public int item_id;
        public byte item_version;
        public String file_name;
        public int item_size;
        public int original_size;
        public String mime_type;
        public byte compression_type;
        public byte[] buffer;
        public boolean write_completed;
        public boolean read_completed;
        
        public void done(String parent) {
            try {
                if ( read_completed == true && 
                        write_completed == false && buffer != null ) {
                    FileOutputStream outputStream = 
                            new FileOutputStream(new java.io.File(parent + "/" + file_name));
                    BufferedOutputStream bos = new BufferedOutputStream(outputStream);
                    
                    if ( compression_type != 0xff ) {
                        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                        InflaterInputStream iis = new InflaterInputStream(bais);
                        byte[] final_buffer = new byte[buffer.length];
                        int count = iis.read(final_buffer);
                        while ( count != -1 ) {
                            bos.write(final_buffer, 0, count);
                            count = iis.read(final_buffer);
                        }
                    } else {
                        bos.write(buffer); 
                    }
                    bos.close();
                    outputStream.close();
                    write_completed = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void print() {
            System.out.print(String.format("\t\t item_id : 0x%x \n",  item_id));
            System.out.print(String.format("\t\t item_version : 0x%x \n",  item_version));
            System.out.print(String.format("\t\t file_name : %s \n",  file_name));
            System.out.print(String.format("\t\t item_size : 0x%x (%d) \n",  
                    item_size, item_size));
            System.out.print(String.format("\t\t original_size : 0x%x (%d) \n", 
                    original_size, original_size));
            System.out.print(String.format("\t\t mime-type : %s \n",  mime_type));
            System.out.print(String.format("\t\t compression_type : 0x%x (%s)\n",  
                    compression_type, compression_type == 0xff ? "non-compress" : "compressed"));
            if ( buffer != null )
                BinaryLogger.print(buffer, 20);
        }
    }
}
