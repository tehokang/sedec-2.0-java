package sedec2.arib.tlv.container.mmtp.messages;

import sedec2.arib.tlv.container.mmt.si.TableFactory;

/**
 * Class to describe as M2Section Message of Table 7-2 of ARIB B60
 * which contains ECM, EMM, DCM, DMM, MH-EIT, MH-CDT, MH-BIT, MH-SDTT, MH-SDT, MH-AIT of MMT-SI.
 */
public class M2SectionMessage extends Message {
    /**
     * Constructor to decode M2Section Message of Table 7-2
     * @param buffer message_byte of MMTP payload
     */
    public M2SectionMessage(byte[] buffer) {
        super(buffer);

        message_id = readOnBuffer(16);
        version = readOnBuffer(8);
        length = readOnBuffer(16);

        __decode_message_body__();
    }

    @Override
    protected void __decode_message_body__() {
        tables.add( TableFactory.createTable(getCurrentBuffer()) );
    }

    @Override
    public int getMessageLength() {
        return length + 5;
    }

    @Override
    public void print() {
        super.print();

        for ( int i=0; i<tables.size(); i++ ) {
            tables.get(i).print();
        }
    }
}
