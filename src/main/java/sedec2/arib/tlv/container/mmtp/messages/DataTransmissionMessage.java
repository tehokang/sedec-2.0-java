package sedec2.arib.tlv.container.mmtp.messages;

import sedec2.arib.tlv.container.mmt.si.TableFactory;

/**
 * Class to describe as Data Transmission Message of Table 10-5 of ARIB B60
 * which contains DDMT, DAMT, DCCT of MMT-SI.
 */
public class DataTransmissionMessage extends Message {
    /**
     * Constructor to decode Data Transmission Message of Table 10-5.
     * @param buffer message_byte of MMTP payload
     */
    public DataTransmissionMessage(byte[] buffer) {
        super(buffer);

        message_id = readOnBuffer(16);
        version = readOnBuffer(8);
        length = readOnBuffer(32);

        __decode_message_body__();
    }

    @Override
    public int getMessageLength() {
        return length + 7;
    }

    @Override
    protected void __decode_message_body__() {
        tables.add( TableFactory.createTable(getCurrentBuffer()) );
    }

    @Override
    public void print() {
        super.print();

        for ( int i=0; i<tables.size(); i++ ) {
            tables.get(i).print();
        }
    }
}
