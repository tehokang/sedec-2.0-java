package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * @brief SimpleApplicationLocationDescriptor
 * @note Verified
 */
public class MH_SimpleApplicationLocationDescriptor extends Descriptor {
    private byte[] initial_path_bytes;

    public MH_SimpleApplicationLocationDescriptor(BitReadWriter brw) {
        super(brw);

        if ( 0 < descriptor_length ) {
            initial_path_bytes = new byte[descriptor_length];
            for ( int i=0; i<descriptor_length; i++ ) {
                initial_path_bytes[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public byte[] getInitialPathBytes() {
        return initial_path_bytes;
    }

    public void setInitialPathBytes(byte[] value) {
        initial_path_bytes = null;
        System.arraycopy(value, 0, initial_path_bytes, 0, value.length);
    }


    @Override
    protected void updateDescriptorLength() {
        descriptor_length = initial_path_bytes.length;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);

        if ( 0 < descriptor_length ) {
            for ( int i=0; i<descriptor_length; i++ ) {
                brw.writeOnBuffer(initial_path_bytes[i], 8);
            }
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t initial_path_bytes : %s \n",
                new String(initial_path_bytes)));
        Logger.d("\n");
    }


}
