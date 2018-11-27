package sedec2.dvb.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ConnectionRequirementDescriptor extends Descriptor {
    private boolean IP_connection_requirement_flag;

    public ConnectionRequirementDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(7);
        IP_connection_requirement_flag = brw.readOnBuffer(1) == 1 ? true : false;

        for ( int i=0; i<descriptor_length-1; i++ ) {
            brw.skipOnBuffer(8);
        }
    }

    public boolean getIPConnectionRequirementFlag() {
        return IP_connection_requirement_flag;
    }

    public void setIPConnectionRequirementFlag(boolean value) {
        IP_connection_requirement_flag = value;
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        brw.writeOnBuffer(0x7f, 7);
        brw.writeOnBuffer(IP_connection_requirement_flag == true ? 1 : 0, 1);
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t IP_connection_requirement_flag : %d \n", IP_connection_requirement_flag));
    }

}
