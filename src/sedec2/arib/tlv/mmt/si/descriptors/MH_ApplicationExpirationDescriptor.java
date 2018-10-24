package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ApplicationExpirationDescriptor extends Descriptor {
    protected long expiration_date_and_time;
    
    public MH_ApplicationExpirationDescriptor(BitReadWriter brw) {
        super(brw);
        
        expiration_date_and_time = brw.readOnBuffer(40);
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t expiration_date_and_time : 0x%x \n", 
                expiration_date_and_time));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5;
    }
}
