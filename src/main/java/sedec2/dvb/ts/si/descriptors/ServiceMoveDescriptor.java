package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ServiceMoveDescriptor extends Descriptor {
    protected int new_original_network_id;
    protected int new_transport_stream_id;
    protected int new_service_id;

    public ServiceMoveDescriptor(BitReadWriter brw) {
        super(brw);

        new_original_network_id = brw.readOnBuffer(16);
        new_transport_stream_id = brw.readOnBuffer(16);
        new_service_id  = brw.readOnBuffer(16);
    }

    public int getNewOriginalNetworkId() {
        return new_original_network_id;
    }

    public int getTransportStreamId() {
        return new_transport_stream_id;
    }

    public int getNewServiceId() {
        return new_service_id;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t new_original_network_id : 0x%x \n",
                new_original_network_id));
        Logger.d(String.format("\t new_transport_stream_id : 0x%x \n",
                new_transport_stream_id));
        Logger.d(String.format("\t new_service_id : 0x%x \n", new_service_id));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 6;
    }
}
