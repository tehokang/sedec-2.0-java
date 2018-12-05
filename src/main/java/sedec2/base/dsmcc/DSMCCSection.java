package sedec2.base.dsmcc;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Descriptor;
import sedec2.base.Table;

public class DSMCCSection extends Table {
    protected int table_id_extension;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected List<Descriptor> DSMCC_descriptor_list = new ArrayList<>();

    protected byte[] private_data_byte;

    public DSMCCSection(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        table_id_extension = readOnBuffer(16);
        skipOnBuffer(2);
        version_number = (byte) readOnBuffer(5);
        current_next_indicator = (byte) readOnBuffer(1);
        section_number = (byte) readOnBuffer(8);
        last_section_number = (byte) readOnBuffer(8);

        if ( table_id == 0x3a ) {
            /**
             * LLCSNAP() not support
             */

        } else if ( table_id == 0x3b ) {
            /**
             * userNetworkMessage()
             */

        } else if ( table_id == 0x3c ) {
            /**
             * downloadDataMessage()
             */

        } else if ( table_id == 0x3d ) {
            /**
             * DSMCC_descriptor_list()
             */
            for ( int i=section_length-9; i>0; ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                i-=desc.getDescriptorLength();
                DSMCC_descriptor_list.add(desc);
            }
        } else if ( table_id == 0x3e ) {
            /**
             * private_data_byte
             */
            private_data_byte = new byte[section_length - 9];
            for ( int i=0; i<private_data_byte.length; i++ ) {
                private_data_byte[i] = (byte) readOnBuffer(8);
            }
        }

        checksum_CRC32 = readOnBuffer(32);

    }
}
