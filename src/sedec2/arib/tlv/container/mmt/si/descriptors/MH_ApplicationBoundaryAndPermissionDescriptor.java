package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ApplicationBoundaryAndPermissionDescriptor extends Descriptor {
    private byte permission_bitmap_count;
    private int[] permission_bitmap = new int[256];
    private byte managed_URL_count;
    private byte[] managed_URL_length = new byte[256];
    private byte[][] managed_URL_byte = new byte[256][256];
    
    public MH_ApplicationBoundaryAndPermissionDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            permission_bitmap_count = (byte) brw.readOnBuffer(8);
            
            for ( int i=0; i<permission_bitmap_count; i++ ) {
                permission_bitmap[i] = brw.readOnBuffer(16);
            }
            
            managed_URL_count = (byte) brw.readOnBuffer(8);
            for ( int i=0;i<managed_URL_count; i++ ) {
                managed_URL_length[i] = (byte) brw.readOnBuffer(8);
                
                for ( int j=0; i<managed_URL_length[i]; j++ ) {
                    managed_URL_byte[i][j] = (byte) brw.readOnBuffer(8);
                }
            }
        }
    }

    public byte getPermissionBitmapCount() {
        return permission_bitmap_count;
    }
    
    public int[] getPermissionBitmap() {
        return permission_bitmap;
    }
    
    public byte getManagedUrlCount() {
        return managed_URL_count;
    }
    
    public byte[] getManagedUrlLength() {
        return managed_URL_length;
    }
    
    public byte[][] getManagedUrlBytes() {
        return managed_URL_byte;
    }
    
    @Override
    public void print() {
        super._print_();
        
        if ( 0 < descriptor_length ) {
            Logger.d(String.format("\t permission_bitmap_count : 0x%x \n", permission_bitmap_count));
            
            for ( int i=0; i<permission_bitmap_count; i++ ) {
                Logger.d(String.format("\t permission_bitmap[%d] : 0x%x \n", i, permission_bitmap[i]));
            }
            
            Logger.d(String.format("\t managed_URL_count : 0x%x \n", managed_URL_count));
            
            for ( int i=0;i<managed_URL_count; i++ ) {
                Logger.d(String.format("\t managed_URL_count : 0x%x \n", managed_URL_length[i])); 
                Logger.d(String.format("\t managed_URL_byte : %s \n", new String(managed_URL_byte[i])));
            }
        }  
        
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + (permission_bitmap_count * 2) + 1;
        
        for ( int i=0; i<managed_URL_count; i++ ) {
            descriptor_length += 1 + managed_URL_length[i];
        }
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            brw.writeOnBuffer(permission_bitmap_count, 8);
            
            for ( int i=0; i<permission_bitmap_count; i++ ) {
                brw.writeOnBuffer(permission_bitmap[i], 16);
            }
            
            brw.writeOnBuffer(managed_URL_count, 8);
            for ( int i=0;i<managed_URL_count; i++ ) {
                brw.writeOnBuffer(managed_URL_length[i], 8);
                
                for ( int j=0; i<managed_URL_length[i]; j++ ) {
                    brw.writeOnBuffer(managed_URL_byte[i][j], 8);
                }
            }
        }
    }

}
