package sedec2.arib.tlv.container.mmtp.messages;

import sedec2.arib.tlv.container.mmt.si.TableFactory;

public class M2SectionMessage extends Message {

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
