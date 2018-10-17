package sedec2.arib.b39.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.b39.DescriptorFactory;
import sedec2.arib.b39.descriptors.Descriptor;
import sedec2.util.Logger;

/**
 * @brief ARIB-b60 LCT
 */
public class LayoutConfigurationTable extends Table {
    protected byte version;
    protected int length;
    protected byte number_of_loop;
    protected List<Layout> layouts = new ArrayList<>();
    protected List<Descriptor> descriptors = new ArrayList<>();
    
    class Layout {
        public byte layout_number;
        public byte device_id;
        public byte number_of_region;
        List<Region> regions = new ArrayList<>();
    }
    
    class Region {
        public byte region_number;
        public byte left_top_pos_x;
        public byte left_top_pos_y;
        public byte right_down_post_x;
        public byte right_down_pos_y;
        public byte layer_order;
    }
    
    public LayoutConfigurationTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public byte GetVersion() {
        return version;
    }
    
    public int GetLength() {
        return length;
    }
    
    public byte GetNumberOfLoop() {
        return number_of_loop;
    }
    
    public List<Layout> GetLayouts() {
        return layouts;
    }
    
    public List<Descriptor> GetDescriptors() {
        return descriptors;
    }
    
    @Override
    protected void __decode_table_body__() {
        number_of_loop = (byte) ReadOnBuffer(8);
        
        int read_bytes = 0;
        for ( int i=0; i<number_of_loop; i++) {
            Layout layout = new Layout();
            layout.layout_number = (byte) ReadOnBuffer(8);
            layout.device_id = (byte) ReadOnBuffer(8);
            layout.number_of_region = (byte) ReadOnBuffer(8);
            read_bytes += 3;
            
            for ( int j=0; j<layout.number_of_region; j++ ) {
                Region region = new Region();
                region.region_number = (byte) ReadOnBuffer(8);
                region.left_top_pos_x = (byte) ReadOnBuffer(8);
                region.left_top_pos_y = (byte) ReadOnBuffer(8);
                region.right_down_post_x = (byte) ReadOnBuffer(8);
                region.right_down_pos_y = (byte) ReadOnBuffer(8);
                region.layer_order = (byte) ReadOnBuffer(8);
                layout.regions.add(region);
                
                read_bytes += 6;
            }
            layouts.add(layout);
        }
        
        for ( int i=length-1-read_bytes; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("number_of_loop : 0x%x \n", number_of_loop));
        for ( int i=0; i<layouts.size(); i++ ) {
            Layout layout = layouts.get(i);
            Logger.d(String.format("[%d] layout_number : 0x%x \n", 
                    i, layout.layout_number));
            Logger.d(String.format("[%d] device_id : 0x%x \n", i, layout.device_id));
            Logger.d(String.format("[%d] number_of_region : 0x%x \n", 
                    i, layout.number_of_region));
            
            for ( int j=0; j<layout.regions.size(); j++ ) {
                Region region = layout.regions.get(j);
                Logger.d(String.format("\t [%d] region_number : 0x%x \n", 
                        j, region.region_number));
                Logger.d(String.format("\t [%d] left_top_pos_x : 0x%x \n", 
                        j, region.left_top_pos_x));
                Logger.d(String.format("\t [%d] left_top_pos_y : 0x%x \n", 
                        j, region.left_top_pos_y));
                Logger.d(String.format("\t [%d] right_down_post_x : 0x%x \n", 
                        j, region.right_down_post_x));
                Logger.d(String.format("\t [%d] right_down_post_y : 0x%x \n", 
                        j, region.right_down_pos_y));
                Logger.d(String.format("\t [%d] layer_order : 0x%x \n", 
                        j, region.layer_order));
            }
        }
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }
    }
}
