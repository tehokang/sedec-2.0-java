package base;

import util.Logger;

public abstract class Descriptor {
    protected int descriptor_tag;
    protected int descriptor_length;
    
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
    
    protected void _PrintDescriptor_(String name) {
        Logger.d("\tdescriptor_tag : 0x" + Integer.toHexString(descriptor_tag) + " (" + name + ")" + "\n" );
        Logger.d("\tdescriptor_length : 0x" + Integer.toHexString(descriptor_length) + "\n");
    }
    
    public abstract void PrintDescriptor();
    
    protected abstract void updateDescriptorLength();
}