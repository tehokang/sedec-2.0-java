package sedec2.arib.tlv.container.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.container.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.container.mmt.si.descriptors.Descriptor;

public class ConditionalAccessTable extends Table {
    List<Descriptor> descriptors = new ArrayList<>();

    public ConditionalAccessTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    @Override
    protected void __decode_table_body__() {

        for ( int i=length; i>0; ) {
            Descriptor desc = DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
        }
    }

    @Override
    public void print() {
        super.print();

        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }
    }
}
