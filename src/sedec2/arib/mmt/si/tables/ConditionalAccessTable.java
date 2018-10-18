package sedec2.arib.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.mmt.si.DescriptorFactory;
import sedec2.arib.mmt.si.descriptors.Descriptor;

public class ConditionalAccessTable extends Table {
    List<Descriptor> descriptors = new ArrayList<>();
    
    public ConditionalAccessTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public List<Descriptor> GetDescriptors() {
        return descriptors;
    }
    
    @Override
    protected void __decode_table_body__() {
        
        for ( int i=length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
        }
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }
    }
}
