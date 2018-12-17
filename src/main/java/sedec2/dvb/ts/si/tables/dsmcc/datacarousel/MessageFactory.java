package sedec2.dvb.ts.si.tables.dsmcc.datacarousel;

import sedec2.dvb.ts.si.tables.dsmcc.Message;
import sedec2.dvb.ts.si.tables.dsmcc.UnknownMessage;
import sedec2.dvb.ts.si.tables.dsmcc.datacarousel.messages.DownloadCancel;
import sedec2.dvb.ts.si.tables.dsmcc.datacarousel.messages.DownloadDataBlock;
import sedec2.dvb.ts.si.tables.dsmcc.datacarousel.messages.DownloadInfoIndication;
import sedec2.dvb.ts.si.tables.dsmcc.datacarousel.messages.DownloadServerInitiate;

public class MessageFactory {
    public static final int DOWNLOAD_INFO_INDICATION = 0x1002;
    public static final int DOWNLOAD_DATA_BLOCK = 0x1003;
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
