package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_CacheControlInfoDescriptor extends Descriptor {
    private int application_size;
    private byte cache_priority;
    private byte package_flag;
    private byte application_version;
    private int expire_date;
    
    public MH_CacheControlInfoDescriptor(BitReadWriter brw) {
        super(brw);
        
        application_size = brw.readOnBuffer(16);
        cache_priority = (byte) brw.readOnBuffer(8);
        package_flag = (byte) brw.readOnBuffer(1);
        application_version = (byte) brw.readOnBuffer(7);
        expire_date = brw.readOnBuffer(16);
    }

    public void setApplicationSize(int value) {
        application_size = value;
    }
    
    public void setCachePriority(byte value) {
        cache_priority = value;
    }
    
    public void setPackageFlag(byte value) {
        package_flag = value;
    }
    
    public void setApplicationVersion(byte value) {
        application_version = value;
    }
    
    public void setExpireDate(int value) {
        expire_date = value;
    }
    
    public int getApplicationSize() {
        return application_size;
    }
    
    public byte getCachePriority() {
        return cache_priority;
    }
    
    public byte getPackageFlag() {
        return package_flag;
    }
    
    public byte getApplicationVersion() {
        return application_version;
    }
    
    public int getExpireDate() {
        return expire_date;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t application_size : 0x%x \n", application_size));
        Logger.d(String.format("\t cache_priority : 0x%x \n", cache_priority));
        Logger.d(String.format("\t package_flag : 0x%x \n", package_flag));
        Logger.d(String.format("\t application_version : 0x%x \n", application_version));
        Logger.d(String.format("\t expire_date : 0x%x \n", expire_date));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 6;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);
        
        brw.writeOnBuffer(application_size, 16);
        brw.writeOnBuffer(cache_priority, 8);
        brw.writeOnBuffer(package_flag, 1);
        brw.writeOnBuffer(application_version, 7);
        brw.writeOnBuffer(expire_date, 16);
    }
}
