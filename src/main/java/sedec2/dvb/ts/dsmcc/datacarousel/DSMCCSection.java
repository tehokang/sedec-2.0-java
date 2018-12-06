package sedec2.dvb.ts.dsmcc.datacarousel;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.Descriptor;
import sedec2.base.Table;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.Message;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DSMCCSection extends Table {
    protected int table_id_extension;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected List<Descriptor> DSMCC_descriptor_list = new ArrayList<>();
    protected Message message = null;

    protected byte[] private_data_byte;

    public DSMCCSection(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public int getTableIdExtension() {
        return table_id_extension;
    }

    public byte getVersionNumber() {
        return version_number;
    }

    public byte getCurrentNextIndicator() {
        return current_next_indicator;
    }

    public byte getSectionNumber() {
        return section_number;
    }

    public byte getLastSectionNumber() {
        return last_section_number;
    }

    public List<Descriptor> getDSMCCDescriptorList() {
        return DSMCC_descriptor_list;
    }

    public Message getMessage() {
        return message;
    }

    public byte[] getPrivateDataByte() {
        return private_data_byte;
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
            skipOnBuffer((section_length-9)*8);
        } else if ( table_id == 0x3b ) {
            /**
             * userNetworkMessage() DSI, DII, DC
             */
            message = MessageFactory.createMessage(this);

        } else if ( table_id == 0x3c ) {
            /**
             * downloadDataMessage() DDB
             */
            message = MessageFactory.createMessage(this);
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

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("table_id_extension : 0x%x \n", table_id_extension));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        for ( int i=0; i<DSMCC_descriptor_list.size(); i++ ) {
            DSMCC_descriptor_list.get(i).print();
        }

        if ( message != null ) message.print();

        if ( private_data_byte != null ) {
            Logger.d(String.format("private_data_byte.length : 0x%x \n", private_data_byte.length));
            BinaryLogger.print(private_data_byte);
        }
    }
}
