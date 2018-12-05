package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class TimeShiftedEventDescriptor extends Descriptor {
    protected int reference_service_id;
    protected int reference_event_id;

    public TimeShiftedEventDescriptor(BitReadWriter brw) {
        super(brw);

        reference_service_id = brw.readOnBuffer(16);
        reference_event_id = brw.readOnBuffer(16);
    }

    public int getReferenceServiceId() {
        return reference_service_id;
    }

    public int getReferenceEventId() {
        return reference_event_id;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t reference_service_id : 0x%x \n", reference_service_id));
        Logger.d(String.format("\t reference_event_id : 0x%x \n", reference_event_id));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4;
    }
}
