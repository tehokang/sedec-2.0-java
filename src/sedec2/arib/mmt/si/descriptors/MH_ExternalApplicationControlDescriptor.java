package sedec2.arib.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ExternalApplicationControlDescriptor extends Descriptor {
    protected byte specific_scope_flag;
    protected int target_application_class;
    protected byte target_application_count;
    protected List<ApplicationIdentifier> target_applications = new ArrayList<>();
    
    protected byte permission_bitmap_count;
    protected int[] permission_bitmap;
    protected byte overlay_admission_polarity;
    protected byte overlay_controlled_area_count;
    protected List<OverlayControlledArea> overlay_controlled_areas = new ArrayList<>();
    
    protected byte blocked_application_count;
    protected List<ApplicationIdentifier> blocked_applications = new ArrayList<>();
    
    class ApplicationIdentifier {
        public int organization_id;
        public int application_id;
    }
    
    class OverlayControlledArea {
        public byte overlay_controlled_area_tag;
        public int horizontal_pos;
        public int vertical_pos;
        public int horizontal_size;
        public int vertical_size;
    }
    
    public MH_ExternalApplicationControlDescriptor(BitReadWriter brw) {
        super(brw);
        
        specific_scope_flag = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(7);
        
        if ( specific_scope_flag == 1) {
            target_application_class = brw.ReadOnBuffer(16);
            target_application_count = (byte) brw.ReadOnBuffer(8);
            
            for ( int i=0; i<target_application_count; i++ ) {
                ApplicationIdentifier app = new ApplicationIdentifier();
                app.organization_id = brw.ReadOnBuffer(16);
                app.application_id = brw.ReadOnBuffer(32);
                target_applications.add(app);
            }
        }
        
        permission_bitmap_count = (byte) brw.ReadOnBuffer(8);
        permission_bitmap = new int[permission_bitmap_count];
        
        for ( int i=0; i<permission_bitmap_count; i++ ) {
            permission_bitmap[i] = brw.ReadOnBuffer(16);
        }
        
        overlay_admission_polarity = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(3);
        overlay_controlled_area_count = (byte) brw.ReadOnBuffer(4);
        
        for ( int i=0; i<overlay_controlled_area_count; i++ ) {
            OverlayControlledArea area = new OverlayControlledArea();
            area.overlay_controlled_area_tag = (byte) brw.ReadOnBuffer(8);
            area.horizontal_pos = brw.ReadOnBuffer(16);
            area.vertical_pos = brw.ReadOnBuffer(16);
            area.horizontal_size = brw.ReadOnBuffer(16);
            area.vertical_size = brw.ReadOnBuffer(16);
            
            overlay_controlled_areas.add(area);
        }
        
        blocked_application_count = (byte) brw.ReadOnBuffer(8);
        for ( int i=0; i<blocked_application_count; i++ ) {
            ApplicationIdentifier app = new ApplicationIdentifier();
            app.organization_id = brw.ReadOnBuffer(16);
            app.application_id = brw.ReadOnBuffer(32);
            blocked_applications.add(app);
        }
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("specific_scope_flag : 0x%x \n", specific_scope_flag)); 
        
        if ( specific_scope_flag == 1) {
            Logger.d(String.format("target_application_class : 0x%x \n", 
                    target_application_class));
            Logger.d(String.format("target_application_count : 0x%x \n", 
                    target_application_count));
            
            for ( int i=0; i<target_applications.size(); i++ ) {
                ApplicationIdentifier app = target_applications.get(i);
                Logger.d(String.format("[%d] organization_id : 0x%x \n", app.organization_id));
                Logger.d(String.format("[%d] application_id : 0x%x \n", app.application_id));
            }
        }
        
        Logger.d(String.format("permission_bitmap_count : 0x%x \n", permission_bitmap_count));
        
        for ( int i=0; i<permission_bitmap.length; i++ ) {
            Logger.d(String.format("[%d] permission_bitmap : 0x%x \n", 
                    i, permission_bitmap[i]));
        }
        
        Logger.d(String.format("overlay_admission_polarity : 0x%x \n", 
                overlay_admission_polarity));
        Logger.d(String.format("overlay_controlled_area_count : 0x%x \n", 
                overlay_controlled_area_count));
        
        for ( int i=0; i<overlay_controlled_areas.size(); i++ ) {
            OverlayControlledArea area = overlay_controlled_areas.get(i);
            Logger.d(String.format("[%d] overlay_controlled_area_tag : 0x%x \n", 
                    i, area.overlay_controlled_area_tag));
            Logger.d(String.format("[%d] horizontal_pos : 0x%x \n", i, area.horizontal_pos));
            Logger.d(String.format("[%d] vertical_pos : 0x%x \n", i, area.vertical_pos));
            Logger.d(String.format("[%d] horizontal_size : 0x%x \n", i, area.horizontal_size));
            Logger.d(String.format("[%d] vertical_size : 0x%x \n", i, area.vertical_size));
        }

        Logger.d(String.format("blocked_application_count : 0x%x \n", 
                blocked_application_count));
        
        for ( int i=0; i<blocked_applications.size(); i++ ) {
            ApplicationIdentifier app = blocked_applications.get(i);
            Logger.d(String.format("[%d] organization_id : 0x%x \n", app.organization_id));
            Logger.d(String.format("[%d] application_id : 0x%x \n", app.application_id));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
        
        if ( specific_scope_flag == 1 ) {
            descriptor_length += (3 + (target_applications.size()*6));
        }
        
        descriptor_length += (1 + (permission_bitmap.length*2));
        descriptor_length += 1;
        descriptor_length += (overlay_controlled_areas.size()*9);
        descriptor_length += (1 + blocked_applications.size()*6);
    }
}
