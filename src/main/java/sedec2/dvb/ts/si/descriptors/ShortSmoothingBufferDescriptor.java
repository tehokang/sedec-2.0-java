package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ShortSmoothingBufferDescriptor extends Descriptor {
    protected byte sb_size;
    protected byte sb_leak_rate;
    protected byte[] DVB_reserved;

    public ShortSmoothingBufferDescriptor(BitReadWriter brw) {
        super(brw);

        sb_size = (byte) brw.readOnBuffer(2);
        sb_leak_rate = (byte) brw.readOnBuffer(6);

        DVB_reserved = new byte[descriptor_length-1];
        for ( int i=0; i<DVB_reserved.length; i++ ) {
            brw.skipOnBuffer(8);
        }
    }

    public byte getSbSize() {
        return sb_size;
    }

    public byte getSbLeakRate() {
        return sb_leak_rate;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t sb_size : 0x%x (%d) \n", sb_size, sb_size));
        Logger.d(String.format("\t sb_leak_rate : 0x%x \n", sb_leak_rate));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + DVB_reserved.length;
    }
}
