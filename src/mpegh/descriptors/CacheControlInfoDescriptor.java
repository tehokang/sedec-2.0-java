package mpegh.descriptors;

import base.BitReadWriter;
import util.Logger;

public class CacheControlInfoDescriptor extends Descriptor {
    private int application_size;
    private byte cache_priority;
    private byte package_flag;
    private byte application_version;
    private int expire_date;
    
    public CacheControlInfoDescriptor(BitReadWriter brw) {
        super(brw);
        
        application_size = brw.ReadOnBuffer(16);
        cache_priority = (byte) brw.ReadOnBuffer(8);
        package_flag = (byte) brw.ReadOnBuffer(1);
        application_version = (byte) brw.ReadOnBuffer(7);
        expire_date = brw.ReadOnBuffer(16);
    }

    public void SetApplicationSize(int value) {
        application_size = value;
    }
    
    public void SetCachePriority(byte value) {
        cache_priority = value;
    }
    
    public void SetPackageFlag(byte value) {
        package_flag = value;
    }
    
    public void SetApplicationVersion(byte value) {
        application_version = value;
    }
    
    public void SetExpireDate(int value) {
        expire_date = value;
    }
    
    public int GetApplicationSize() {
        return application_size;
    }
    
    public byte GetCachePriority() {
        return cache_priority;
    }
    
    public byte GetPackageFlag() {
        return package_flag;
    }
    
    public byte GetApplicationVersion() {
        return application_version;
    }
    
    public int GetExpireDate() {
        return expire_date;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptor_("CacheControlInfoDescriptor");
        
        Logger.d(String.format("\tapplication_size : 0x%x \n", application_size));
        Logger.d(String.format("\tcache_priority : 0x%x \n", cache_priority));
        Logger.d(String.format("\tpackage_flag : 0x%x \n", package_flag));
        Logger.d(String.format("\tapplication_version : 0x%x \n", application_version));
        Logger.d(String.format("\texpire_date : 0x%x \n", expire_date));

    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 6;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(application_size, 16);
        brw.WriteOnBuffer(cache_priority, 8);
        brw.WriteOnBuffer(package_flag, 1);
        brw.WriteOnBuffer(application_version, 7);
        brw.WriteOnBuffer(expire_date, 16);
    }
}
