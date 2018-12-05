package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class PDCDescriptor extends Descriptor {
    protected int programme_identification_label;

    public PDCDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(4);
        programme_identification_label = brw.readOnBuffer(20);
    }

    public int getProgrammIdentificationLabel() {
        return programme_identification_label;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t programme_identification_label : 0x%x \n",
                programme_identification_label));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3;
    }
}
