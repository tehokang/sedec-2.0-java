package sedec2.arib.tlv;

import sedec2.arib.tlv.container.packets.NetworkTimeProtocolData;
import sedec2.base.Table;

public interface ITLVExtractorListener {
    public void onReceivedTable(Table table);
    public void onUpdatedNtp(NetworkTimeProtocolData ntp);
}
