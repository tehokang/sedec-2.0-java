package sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel;

import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.Message;
import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.UnknownMessage;
import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel.messages.DownloadCancel;
import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel.messages.DownloadDataBlock;
import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel.messages.DownloadInfoIndication;
import sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel.messages.DownloadServerInitiate;

public class MessageFactory {
    public static final int DOWNLOAD_INFO_REQUEST = 0x1001;
    public static final int DOWNLOAD_INFO_RESPONSE = 0x1002;
    public static final int DOWNLOAD_INFO_INDICATION = DOWNLOAD_INFO_RESPONSE;
    public static final int DOWNLOAD_DATA_BLOCK = 0x1003;
    public static final int DOWNLOAD_DATA_REQUEST = 0x1004;
    public static final int DOWNLOAD_CANCEL = 0x1005;
    public static final int DOWNLOAD_SERVER_INITIATE = 0x1006;

    public static Message createMessage(byte[] buffer) {
        int message_id = (((buffer[2] & 0xff) << 8) | (buffer[3] & 0xff));

        switch ( message_id ) {
            case DOWNLOAD_SERVER_INITIATE:
                return new DownloadServerInitiate(buffer);
            case DOWNLOAD_INFO_INDICATION:
                return new DownloadInfoIndication(buffer);
            case DOWNLOAD_CANCEL:
                return new DownloadCancel(buffer);
            case DOWNLOAD_DATA_BLOCK:
                return new DownloadDataBlock(buffer);
            default:
                return new UnknownMessage(buffer);
        }
    }

    private MessageFactory() {
        /**
         * DO NOTHING
         */
    }
}
