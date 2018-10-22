package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;

public abstract class Descriptor extends sedec2.base.Descriptor {

    public Descriptor() {
        /** 
         * @note NOTHING TO DO, THIS IS ONLY FOR LEGACY DESCRIPTOR
         * WHICH HAS DESCRIPTOR_TAG 8 bits and DESCRIPTOR_LENGTH 8 bits 
         * */
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
