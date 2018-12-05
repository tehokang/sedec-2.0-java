package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class AssociationTagDescriptor extends Descriptor {
    protected int association_tag;
    protected int Use;
    protected byte selector_byte_length;
    protected byte[] selector_byte;
    protected byte[] private_data_byte;

    public AssociationTagDescriptor(BitReadWriter brw) {
        super(brw);

        association_tag = brw.readOnBuffer(16);
        Use = brw.readOnBuffer(16);
        selector_byte_length = (byte) brw.readOnBuffer(8);
        selector_byte = new byte[selector_byte_length];
        for ( int i=0; i<selector_byte.length; i++ ) {
            selector_byte[i] = (byte) brw.readOnBuffer(8);
        }

        private_data_byte = new byte[descriptor_length-5-selector_byte.length];
        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] = (byte) brw.readOnBuffer(8);
        }

    }

    public int getAssociationTag() {
        return association_tag;
    }

    public int getUse() {
        return Use;
    }

    public byte[] getSelectorByte() {
        return selector_byte;
    }

    public byte[] getPrivateDataByte() {
        return private_data_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t association_tag : 0x%x \n", association_tag));
        Logger.d(String.format("\t Use : 0x%x \n", Use));
        Logger.d(String.format("\t selector_byte_length : 0x%x \n",  selector_byte.length));
        BinaryLogger.print(selector_byte);
        Logger.d(String.format("\t private_data_byte length : 0x%x \n", private_data_byte.length));
        BinaryLogger.print(private_data_byte);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = (5 + selector_byte.length + private_data_byte.length);
    }
}
