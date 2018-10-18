package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_AutostartPriorityDescriptor extends Descriptor {

    private byte autostart_priority;
    
    public MH_AutostartPriorityDescriptor(BitReadWriter brw) {
        super(brw);
        
        autostart_priority = (byte) brw.ReadOnBuffer(8);
    }

    public byte GetAutostartPriority() {
        return autostart_priority;
    }
    
    public void SetAutostartPriority(byte value) {
        autostart_priority = value;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t autostart_priority : 0x%x \n", autostart_priority));
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(autostart_priority, 8);
    }
}
