package sedec2.base;

import sedec2.util.Logger;

public abstract class Descriptor {
    protected int descriptor_tag;
    protected int descriptor_length;
    
    public Descriptor() {
        /**
         * @warning NOTHING TO HERE SINCE CHILD WANTS TO OVERRIDE
         */
    }
    public Descriptor(BitReadWriter brw) {
        descriptor_tag = brw.readOnBuffer(8);
        descriptor_length = brw.readOnBuffer(8);
    }
    
    public int getDescriptorTag() {
        return descriptor_tag;
    }
    
    public int getDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 2;
    }
    
    public void writeDescriptor(BitReadWriter brw) {
        brw.writeOnBuffer(descriptor_tag, 8);
        brw.writeOnBuffer(descriptor_length, 8);
    }
    
    protected void _print_() {
        Logger.d("\n");
        Logger.d(String.format("\t descriptor_tag : 0x%x (%s) \n", 
                descriptor_tag, getClass().getName()));
        Logger.d(String.format("\t descriptor_length : 0x%x (%d) \n", 
                descriptor_length, descriptor_length));
    }
    
    public abstract void print();
    
    
    /**
     * @brief updateDescriptorLength should return length except for header of descriptor \n
     * like tag and length 
     */
    protected abstract void updateDescriptorLength();
}