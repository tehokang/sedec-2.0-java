package arib.b39.descriptors;

import base.BitReadWriter;

public abstract class Descriptor extends base.Descriptor {

    public Descriptor() {
        /** NOTHING TO DO */
    }
    
    public Descriptor(BitReadWriter brw) {
        descriptor_tag = brw.ReadOnBuffer(16);
        descriptor_length = brw.ReadOnBuffer(8);
    }

    @Override
    public int GetDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 3;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        brw.WriteOnBuffer(descriptor_tag, 16);
        brw.WriteOnBuffer(descriptor_length, 8);
    }
}
