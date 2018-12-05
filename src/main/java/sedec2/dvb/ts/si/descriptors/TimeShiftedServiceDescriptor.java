package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class TimeShiftedServiceDescriptor extends Descriptor {
    protected int reference_service_id;

    public TimeShiftedServiceDescriptor(BitReadWriter brw) {
        super(brw);

        reference_service_id = brw.readOnBuffer(16);
    }

    public int getReferenceServiceId() {
        return reference_service_id;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t reference_service_id : 0x%x \n", reference_service_id));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
    }
}
