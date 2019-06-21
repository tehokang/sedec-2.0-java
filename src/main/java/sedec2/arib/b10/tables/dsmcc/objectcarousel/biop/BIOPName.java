package sedec2.arib.b10.tables.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class BIOPName {
    protected int nameComponents_count;
    protected List<Name> names = new ArrayList<>();
    protected int initialContext_length;
    protected byte[] initialContext_data_byte;

    public class Name {
        public byte id_length;
        public byte[] id_data_byte;
        public byte kind_length;
        public byte[] kind_data_byte;
    }

    public BIOPName(BitReadWriter brw) {
        nameComponents_count = (byte) brw.readOnBuffer(8);
        for ( int k=0; k<nameComponents_count; k++ ) {
            Name name = new Name();
            name.id_length = (byte) brw.readOnBuffer(8);
            name.id_data_byte = new byte[name.id_length];
            name.kind_length = (byte) brw.readOnBuffer(8);
            name.kind_data_byte = new byte[name.kind_length];
            for ( int m=0; m<name.kind_data_byte.length; m++ ) {
                name.kind_data_byte[m] = (byte) brw.readOnBuffer(8);
            }
            names.add(name);
        }
    }

    public int getLength() {
        int length = 1;
        for ( int i=0; i<names.size(); i++ ) {
            Name name = names.get(i);
            length += ( 2 + name.id_data_byte.length + name.kind_data_byte.length );
        }
        return length;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t nameComponents_count : 0x%x \n", nameComponents_count));
        for ( int i=0; i<names.size(); i++ ) {
            Name name = names.get(i);
            Logger.d(String.format("\t [%d] id_length : 0x%x \n", i, name.id_length));
            Logger.d(String.format("\t [%d] id_data_byte : \n", i));
            BinaryLogger.print(name.id_data_byte);
            Logger.d(String.format("\t [%d] kind_length : 0x%x \n", i, name.kind_length));
            Logger.d(String.format("\t [%d] kind_data_byte : \n", i));
            BinaryLogger.print(name.kind_data_byte);
        }
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
