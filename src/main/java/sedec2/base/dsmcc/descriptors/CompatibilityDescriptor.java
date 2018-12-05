package sedec2.base.dsmcc.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.base.dsmcc.DescriptorFactory;
import sedec2.util.Logger;

public class CompatibilityDescriptor {
    protected int compatibilityDescriptorLength;
    protected int descriptorCount;
    protected List<Descriptor> descriptors = new ArrayList<>();

    public CompatibilityDescriptor(BitReadWriter brw) {
        compatibilityDescriptorLength = brw.readOnBuffer(16);
        descriptorCount = brw.readOnBuffer(16);

        for ( int i=0; i<descriptorCount; i++ ) {
            Descriptor desc = DescriptorFactory.createDescriptor(brw);
            descriptors.add(desc);
        }
    }

    public void print() {
        Logger.d(String.format("\t compatibilityDescriptorLength : 0x%d \n",
                compatibilityDescriptorLength));
        Logger.d(String.format("\t descriptorCount : 0x%d \n", descriptorCount));
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }
    }

    public int getLength() {
        int descriptor_length = 4;
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptor_length += descriptors.get(i).getDescriptorLength();
        }
        return descriptor_length;
    }
}
