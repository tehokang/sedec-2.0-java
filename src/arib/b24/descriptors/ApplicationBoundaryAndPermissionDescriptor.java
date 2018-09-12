package arib.b24.descriptors;

import base.BitReadWriter;
import util.Logger;

public class ApplicationBoundaryAndPermissionDescriptor extends Descriptor {
    private byte permission_bitmap_count;
    private int[] permission_bitmap = new int[256];
    private byte managed_URL_count;
    private byte[] managed_URL_length = new byte[256];
    private byte[][] managed_URL_byte = new byte[256][256];
    
    public ApplicationBoundaryAndPermissionDescriptor(BitReadWriter brw) {
        super(brw);
        
        if ( 0 < descriptor_length ) {
            permission_bitmap_count = (byte) brw.ReadOnBuffer(8);
            
            for ( int i=0; i<permission_bitmap_count; i++ ) {
                permission_bitmap[i] = brw.ReadOnBuffer(16);
            }
            
            managed_URL_count = (byte) brw.ReadOnBuffer(8);
            for ( int i=0;i<managed_URL_count; i++ ) {
                managed_URL_length[i] = (byte) brw.ReadOnBuffer(8);
                
                for ( int j=0; i<managed_URL_length[i]; j++ ) {
                    managed_URL_byte[i][j] = (byte) brw.ReadOnBuffer(8);
                }
            }
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
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
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            brw.WriteOnBuffer(permission_bitmap_count, 8);
            
            for ( int i=0; i<permission_bitmap_count; i++ ) {
                brw.WriteOnBuffer(permission_bitmap[i], 16);
            }
            
            brw.WriteOnBuffer(managed_URL_count, 8);
            for ( int i=0;i<managed_URL_count; i++ ) {
                brw.WriteOnBuffer(managed_URL_length[i], 8);
                
                for ( int j=0; i<managed_URL_length[i]; j++ ) {
                    brw.WriteOnBuffer(managed_URL_byte[i][j], 8);
                }
            }
        }
    }
    
    

}
