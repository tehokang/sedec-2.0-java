package base;

import util.Logger;

public abstract class Descriptor {
    protected int descriptor_tag;
    protected int descriptor_length;
    
    public Descriptor() {
        /**
         * @warning NOTHING TO HERE SINCE CHILD WANTS TO OVERRIDE
         */
    }
    public Descriptor(BitReadWriter brw) {
        descriptor_tag = brw.ReadOnBuffer(8);
        descriptor_length = brw.ReadOnBuffer(8);
    }
    
    public int GetDescriptorTag() {
        return descriptor_tag;
    }
    
    public int GetDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 2;
    }
    
    public void WriteDescriptor(BitReadWriter brw) {
        brw.WriteOnBuffer(descriptor_tag, 8);
        brw.WriteOnBuffer(descriptor_length, 8);
    }
    
    protected void _PrintDescriptorHeader_() {
        Logger.d(String.format("\t descriptor_tag : 0x%x (%s) \n", 
                descriptor_tag, getClass().getName()));
        Logger.d(String.format("\t descriptor_length : 0x%x \n", descriptor_length));
    }
    
    public abstract void PrintDescriptor();
    
    
    /**
     * @brief updateDescriptorLength should return length except for header of descriptor \n
     * like tag and length 
     */
    protected abstract void updateDescriptorLength();
}