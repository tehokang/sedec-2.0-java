package sedec2.arib.tlv.mmt.si.descriptors;

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
    
    public class ApplicationIdentifier {
        public int organization_id;
        public int application_id;
    }
    
    public class OverlayControlledArea {
        public byte overlay_controlled_area_tag;
        public int horizontal_pos;
        public int vertical_pos;
        public int horizontal_size;
        public int vertical_size;
    }
    
    public MH_ExternalApplicationControlDescriptor(BitReadWriter brw) {
        super(brw);
        
        specific_scope_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(7);
        
        if ( specific_scope_flag == 1) {
            target_application_class = brw.readOnBuffer(16);
            target_application_count = (byte) brw.readOnBuffer(8);
            
            for ( int i=0; i<target_application_count; i++ ) {
                ApplicationIdentifier app = new ApplicationIdentifier();
                app.organization_id = brw.readOnBuffer(16);
                app.application_id = brw.readOnBuffer(32);
                target_applications.add(app);
            }
        }
        
        permission_bitmap_count = (byte) brw.readOnBuffer(8);
        permission_bitmap = new int[permission_bitmap_count];
        
        for ( int i=0; i<permission_bitmap_count; i++ ) {
            permission_bitmap[i] = brw.readOnBuffer(16);
        }
        
        overlay_admission_polarity = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(3);
        overlay_controlled_area_count = (byte) brw.readOnBuffer(4);
        
        for ( int i=0; i<overlay_controlled_area_count; i++ ) {
            OverlayControlledArea area = new OverlayControlledArea();
            area.overlay_controlled_area_tag = (byte) brw.readOnBuffer(8);
            area.horizontal_pos = brw.readOnBuffer(16);
            area.vertical_pos = brw.readOnBuffer(16);
            area.horizontal_size = brw.readOnBuffer(16);
            area.vertical_size = brw.readOnBuffer(16);
            
            overlay_controlled_areas.add(area);
        }
        
        blocked_application_count = (byte) brw.readOnBuffer(8);
        for ( int i=0; i<blocked_application_count; i++ ) {
            ApplicationIdentifier app = new ApplicationIdentifier();
            app.organization_id = brw.readOnBuffer(16);
            app.application_id = brw.readOnBuffer(32);
            blocked_applications.add(app);
        }
    }
    
    public byte getSpecificScopeFlag() {
        return specific_scope_flag;
    }
    
    public int getTargetApplicationClass() {
        return target_application_class;
    }
    
    public byte getTargetApplicationCount() {
        return target_application_count;
    }
    
    public List<ApplicationIdentifier> getTargetApplications() {
        return target_applications;
    }
    
    public byte getPermissionBitmapCount() {
        return permission_bitmap_count;
    }
    
    public int[] getPermissionBitmap() {
        return permission_bitmap;
    }
    
    public byte getOverlayAdmissionPolarity() {
        return overlay_admission_polarity;
    }
    
    public byte getOverlayControlledAreaCount() {
        return overlay_controlled_area_count;
    }
    
    public List<OverlayControlledArea> getOverlayControlledAreas() {
        return overlay_controlled_areas;
    }
    
    public byte getBlockedApplicationCount() {
        return blocked_application_count;
    }
    
    public List<ApplicationIdentifier> getBlockedApplications() {
        return blocked_applications;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t specific_scope_flag : 0x%x \n", specific_scope_flag)); 
        
        if ( specific_scope_flag == 1) {
            Logger.d(String.format("\t target_application_class : 0x%x \n", 
                    target_application_class));
            Logger.d(String.format("\t target_application_count : 0x%x \n", 
                    target_application_count));
            
            for ( int i=0; i<target_applications.size(); i++ ) {
                ApplicationIdentifier app = target_applications.get(i);
                Logger.d(String.format("\t [%d] organization_id : 0x%x \n", app.organization_id));
                Logger.d(String.format("\t [%d] application_id : 0x%x \n", app.application_id));
            }
        }
        
        Logger.d(String.format("\t permission_bitmap_count : 0x%x \n", permission_bitmap_count));
        
        for ( int i=0; i<permission_bitmap.length; i++ ) {
            Logger.d(String.format("\t [%d] permission_bitmap : 0x%x \n", 
                    i, permission_bitmap[i]));
        }
        
        Logger.d(String.format("\t overlay_admission_polarity : 0x%x \n", 
                overlay_admission_polarity));
        Logger.d(String.format("\t overlay_controlled_area_count : 0x%x \n", 
                overlay_controlled_area_count));
        
        for ( int i=0; i<overlay_controlled_areas.size(); i++ ) {
            OverlayControlledArea area = overlay_controlled_areas.get(i);
            Logger.d(String.format("\t [%d] overlay_controlled_area_tag : 0x%x \n", 
                    i, area.overlay_controlled_area_tag));
            Logger.d(String.format("\t [%d] horizontal_pos : 0x%x \n", i, area.horizontal_pos));
            Logger.d(String.format("\t [%d] vertical_pos : 0x%x \n", i, area.vertical_pos));
            Logger.d(String.format("\t [%d] horizontal_size : 0x%x \n", i, area.horizontal_size));
            Logger.d(String.format("\t [%d] vertical_size : 0x%x \n", i, area.vertical_size));
        }

        Logger.d(String.format("\t blocked_application_count : 0x%x \n", 
                blocked_application_count));
        
        for ( int i=0; i<blocked_applications.size(); i++ ) {
            ApplicationIdentifier app = blocked_applications.get(i);
            Logger.d(String.format("\t [%d] organization_id : 0x%x \n", app.organization_id));
            Logger.d(String.format("\t [%d] application_id : 0x%x \n", app.application_id));
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
