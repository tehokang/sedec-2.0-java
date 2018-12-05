package sedec2.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class NVODReferenceDescriptor extends Descriptor {
    protected List<Reference> references = new ArrayList<>();

    public class Reference {
        public int transport_stream_id;
        public int original_network_id;
        public int service_id;
    }

    public NVODReferenceDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            Reference ref = new Reference();
            ref.transport_stream_id = brw.readOnBuffer(16);
            ref.original_network_id = brw.readOnBuffer(16);
            ref.service_id = brw.readOnBuffer(16);

            references.add(ref);
            i-=6;
        }
    }

    public List<Reference> getReferences() {
        return references;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<references.size(); i++ ) {
            Reference ref = references.get(i);
            Logger.d(String.format("\t [%d] transport_stream_id : 0x%x \n",
                    i, ref.transport_stream_id));
            Logger.d(String.format("\t [%d] original_network_id : 0x%x \n",
                    i, ref.original_network_id));
            Logger.d(String.format("\t [%d] service_id : 0x%x \n",
                    i, ref.service_id));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = references.size()*6;
    }
}
