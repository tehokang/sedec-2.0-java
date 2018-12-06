package sedec2.dvb.ts.dsmcc.datacarousel;

import sedec2.base.BitReadWriter;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.DownloadCancel;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.DownloadDataBlock;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.DownloadInfoIndication;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.DownloadServerInitiate;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.Message;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.UnknownMessage;

public class MessageFactory {
    public static final int DOWNLOAD_INFO_REQUEST = 0x1001;
    public static final int DOWNLOAD_INFO_RESPONSE = 0x1002;
    public static final int DOWNLOAD_INFO_INDICATION = DOWNLOAD_INFO_RESPONSE;
    public static final int DOWNLOAD_DATA_BLOCK = 0x1003;
    public static final int DOWNLOAD_DATA_REQUEST = 0x1004;
    public static final int DOWNLOAD_CANCEL = 0x1005;
    public static final int DOWNLOAD_SERVER_INITIATE = 0x1006;

    public static Message createMessage(BitReadWriter brw) {
        int message_id = (((brw.getCurrentBuffer()[2] & 0xff) << 8) |
                        (brw.getCurrentBuffer()[3] & 0xff));

        switch ( message_id ) {
            case DOWNLOAD_SERVER_INITIATE:
                return new DownloadServerInitiate(brw);
            case DOWNLOAD_INFO_INDICATION:
                return new DownloadInfoIndication(brw);
            case DOWNLOAD_CANCEL:
                return new DownloadCancel(brw);
            case DOWNLOAD_DATA_BLOCK:
                return new DownloadDataBlock(brw);
            default:
                return new UnknownMessage(brw);
        }
    }

    private MessageFactory() {
        /**
         * DO NOTHING
         */
    }
}
