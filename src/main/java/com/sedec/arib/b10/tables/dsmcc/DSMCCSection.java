package com.sedec.arib.b10.tables.dsmcc;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.Descriptor;
import com.sedec.base.Table;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

/**
 * DSMCCSection describes Table 9-2 of ISO 13818-6.
 * According to table_id, the kinds of table can be defined
 * <ul>
 * <li> 0x3A DSM-CC Sections containing multi-protocol encapsulated data
 * <li> 0x3B DSM-CC Sections containing U-N Messages, except Download Data Messages (DSI, DII)
 * <li> 0x3C DSM-CC Sections containing Download Data Messages (DDB)
 * <li> 0x3D DSM-CC Sections containing Stream Descriptors
 * <li> 0x3E DSM-CC Sections containing private data
 * </ul>
 */
public class DSMCCSection extends Table {
    protected int table_id_extension;
    protected byte version_number;
    protected byte current_next_indicator;
    protected byte section_number;
    protected byte last_section_number;
    protected List<Descriptor> DSMCC_descriptor_list = new ArrayList<>();
    protected Message message = null;
    protected byte[] message_byte;

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

    public byte[] getMessageBytes() {
        return message_byte;
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
            message_byte = new byte[section_length-9];
            for ( int i=0; i<message_byte.length; i++ ) {
                message_byte[i] = (byte) readOnBuffer(8);
            }
        } else if ( table_id == 0x3c ) {
            /**
             * downloadDataMessage() DDB
             */
            message_byte = new byte[section_length-9];
            for ( int i=0; i<message_byte.length; i++ ) {
                message_byte[i] = (byte) readOnBuffer(8);
            }
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

    public void updateToDataCarousel() {
        message = com.sedec.arib.b10.tables.dsmcc.datacarousel.MessageFactory.createMessage(message_byte);
    }

    public void updateToObjectCarousel() {
        message = com.sedec.arib.b10.tables.dsmcc.objectcarousel.MessageFactory.createMessage(message_byte);
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("table_id_extension : 0x%x \n", table_id_extension));
        Logger.d(String.format("version_number : 0x%x \n", version_number));
        Logger.d(String.format("current_next_indicator : 0x%x \n", current_next_indicator));
        Logger.d(String.format("section_number : 0x%x \n", section_number));
        Logger.d(String.format("last_section_number : 0x%x \n", last_section_number));
        Logger.d(String.format("DSMCC_descriptor_list.size : 0x%x \n",
                DSMCC_descriptor_list.size()));
        for ( int i=0; i<DSMCC_descriptor_list.size(); i++ ) {
            DSMCC_descriptor_list.get(i).print();
        }

        if ( message != null ) message.print();

        if ( private_data_byte != null ) {
            Logger.d(String.format("private_data_byte.length : 0x%x \n", private_data_byte.length));
            BinaryLogger.print(private_data_byte);
        }

        Logger.d(String.format("checksum_CRC32 : 0x%02x%02x%02x%02x \n",
                (checksum_CRC32 >> 24) & 0xff,
                (checksum_CRC32 >> 16) & 0xff,
                (checksum_CRC32 >> 8) & 0xff,
                checksum_CRC32 & 0xff));
    }
}
