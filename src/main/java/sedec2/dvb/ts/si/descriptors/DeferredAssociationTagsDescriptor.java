package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DeferredAssociationTagsDescriptor extends Descriptor {
    protected byte association_tags_loop_length;
    protected int[] association_tag;
    protected int transport_stream_id;
    protected int program_number;
    protected byte[] private_data_byte;

    public DeferredAssociationTagsDescriptor(BitReadWriter brw) {
        super(brw);

        association_tags_loop_length = (byte) brw.readOnBuffer(8);
        association_tag = new int[association_tags_loop_length/2];
        for ( int i=0; i<association_tag.length; i++ ) {
            association_tag[i] = brw.readOnBuffer(16);
        }
        transport_stream_id = brw.readOnBuffer(16);
        program_number = brw.readOnBuffer(16);

        private_data_byte = new byte[descriptor_length-1-association_tag.length*2-4];
        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] =(byte) brw.readOnBuffer(8);
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t association_tags_loop_length : 0x%x \n",
                association_tags_loop_length));
        for ( int i=0; i<association_tag.length; i++ ) {
            Logger.d(String.format("\t [%d] association_tag : 0x%x \n",
                    i, association_tag[i]));
        }
        Logger.d(String.format("\t transport_stream_id : 0x%x \n", transport_stream_id));
        Logger.d(String.format("\t program_number : 0x%x \n", program_number));
        Logger.d(String.format("\t private_data_byte.length : 0x%x \n",
                private_data_byte.length));
        BinaryLogger.print(private_data_byte);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + association_tag.length*2 + 4 + private_data_byte.length;
    }
}
