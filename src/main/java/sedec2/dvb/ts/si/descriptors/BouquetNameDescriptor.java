package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class BouquetNameDescriptor extends Descriptor {
    protected byte[] characters;

    public BouquetNameDescriptor(BitReadWriter brw) {
        super(brw);

        characters = new byte[descriptor_length];
        for ( int i=0; i<characters.length; i++ ) {
            characters[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte[] getChar() {
        return characters;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t char : %s \n",
                new String(characters)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = characters.length;
    }

}
