package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class MPEGH_BroadcasterNameDescriptor extends Descriptor {
    protected byte[] broadcaster_name;
    
    public MPEGH_BroadcasterNameDescriptor(BitReadWriter brw) {
        super(brw);
        
        broadcaster_name = new byte[descriptor_length];
        for ( int i=0; i<descriptor_length; i++ ) {
            broadcaster_name[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    public String GetBroadcasterName() {
        return new String(broadcaster_name);
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t char : %s \n", new String(broadcaster_name)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = broadcaster_name.length;
    }
}
