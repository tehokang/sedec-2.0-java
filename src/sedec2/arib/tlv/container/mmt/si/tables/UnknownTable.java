package sedec2.arib.tlv.container.mmt.si.tables;

public class UnknownTable extends Table {

    public UnknownTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    @Override
    protected void __decode_table_body__() {
        skipOnBuffer(length*8);
    }
}
