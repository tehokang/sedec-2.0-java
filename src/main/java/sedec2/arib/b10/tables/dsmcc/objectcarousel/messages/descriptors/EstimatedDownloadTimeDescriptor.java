package sedec2.arib.b10.tables.dsmcc.objectcarousel.messages.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

public class EstimatedDownloadTimeDescriptor extends Descriptor {
    protected int est_download_time;

    public EstimatedDownloadTimeDescriptor(BitReadWriter brw) {
        super(brw);

        est_download_time = brw.readOnBuffer(32);
    }

    public int getEstimatedDownloadTime() {
        return est_download_time;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t est_download_time : 0x%x \n", est_download_time));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4;
    }
}
