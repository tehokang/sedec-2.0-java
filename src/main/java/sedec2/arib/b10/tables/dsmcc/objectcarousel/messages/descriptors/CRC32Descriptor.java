package sedec2.arib.b10.tables.dsmcc.objectcarousel.messages.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

public class CRC32Descriptor extends Descriptor {
    protected int crc_32;

    public CRC32Descriptor(BitReadWriter brw) {
        super(brw);

        crc_32 = brw.readOnBuffer(32);
    }

    public int getCRC32() {
        return crc_32;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t crc_32 : 0x%x \n", crc_32));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4;
    }
}
