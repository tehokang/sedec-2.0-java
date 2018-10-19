package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class EmergencyNewsDescriptor extends Descriptor {
    protected long transmit_timestamp;
    
    public EmergencyNewsDescriptor(BitReadWriter brw) {
        super(brw);
        
        transmit_timestamp = brw.ReadOnBuffer(64);
        brw.SkipOnBuffer(8);
    }
    
    public long GetTransmitTimestamp() {
        return transmit_timestamp;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("transmit_timestamp : 0x%x \n", transmit_timestamp));

    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 9;
    }
}
