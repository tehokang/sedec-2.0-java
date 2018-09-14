package arib.b39.tables;

import java.util.ArrayList;
import java.util.List;

import arib.b39.DescriptorFactory;
import arib.b39.descriptors.Descriptor;

public class ConditionalAccessTable extends Table {
    List<Descriptor> descriptors = new ArrayList<>();
    
    public ConditionalAccessTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
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
