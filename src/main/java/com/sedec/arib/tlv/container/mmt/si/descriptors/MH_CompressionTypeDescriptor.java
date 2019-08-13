package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MH_CompressionTypeDescriptor extends Descriptor {
    private int compression_type;
    private int original_size;

    public MH_CompressionTypeDescriptor(BitReadWriter brw) {
        super(brw);

        compression_type = brw.readOnBuffer(8);
        original_size = brw.readOnBuffer(32);
    }

    public void setCompressionType(int value) {
        compression_type = value;
    }

    public void setOriginalSize(int value) {
        original_size = value;
    }

    public int getCompressionType() {
        return compression_type;
    }

    public int getOriginalSize() {
        return original_size;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t compression_type : 0x%x \n",  compression_type));
        Logger.d(String.format("\t original_size : 0x%x (%d) \n", original_size, original_size));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        if ( 0 < descriptor_length ) {
            brw.writeOnBuffer(compression_type, 8);
            brw.writeOnBuffer(original_size, 32);
        }
    }

}
