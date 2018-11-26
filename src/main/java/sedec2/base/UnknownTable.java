package sedec2.base;

public class UnknownTable extends Table {

    public UnknownTable(byte[] buffer) {
        super(buffer);

        is_unknown_table = true;
        
        skipOnBuffer(section_length*8);
    }

    @Override
    protected void __decode_table_body__() {

    }
}
