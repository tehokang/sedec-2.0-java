package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class AdaptationFieldDataDescriptor extends Descriptor {
    protected byte adaptation_field_data_identifier;

    public AdaptationFieldDataDescriptor(BitReadWriter brw) {
        super(brw);

        adaptation_field_data_identifier = (byte) brw.readOnBuffer(8);
    }

    public byte getAdaptationFieldDataIndentifier() {
        return adaptation_field_data_identifier;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t adaptation_field_data_identifier : 0x%x \n",
                adaptation_field_data_identifier));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }
}
