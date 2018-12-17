package sedec2.arib.tlv.container.mmt.si.tables.dsmcc.datacarousel.messages.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

public class TypeDescriptor extends Descriptor {
    protected byte[] text_char;

    public TypeDescriptor(BitReadWriter brw) {
        super(brw);

        text_char = new byte[descriptor_length];
        for ( int i=0; i<text_char.length; i++ ) {
            text_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte[] getTextChar() {
        return text_char;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t text_char : %s \n", new String(text_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = text_char.length;
    }
}
