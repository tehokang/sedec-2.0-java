package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class CAIdentifierDescriptor extends Descriptor {
    protected int[] CA_system_id;

    public CAIdentifierDescriptor(BitReadWriter brw) {
        super(brw);

        CA_system_id = new int[descriptor_length/2];

        for ( int i=0; i<CA_system_id.length; i++ ) {
            CA_system_id[i] = brw.readOnBuffer(16);
        }
    }

    public int[] getCASystemId() {
        return CA_system_id;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<CA_system_id.length; i++ ) {
            Logger.d(String.format("\t [%d] CA_system_id : 0x%x \n", CA_system_id[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = CA_system_id.length * 2;
    }
}
