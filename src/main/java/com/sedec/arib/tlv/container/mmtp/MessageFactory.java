package com.sedec.arib.tlv.container.mmtp;

import com.sedec.arib.tlv.container.mmtp.messages.CAMessage;
import com.sedec.arib.tlv.container.mmtp.messages.DataTransmissionMessage;
import com.sedec.arib.tlv.container.mmtp.messages.M2SectionMessage;
import com.sedec.arib.tlv.container.mmtp.messages.M2ShortSectionMessage;
import com.sedec.arib.tlv.container.mmtp.messages.Message;
import com.sedec.arib.tlv.container.mmtp.messages.PAMessage;

/**
 * Class to describe 7.2 Message of ARIB B60 like below
 *
 * <ul>
 * <li> {@link PAMessage} which contains MPT, PLT, LCT of MMT-SI.
 * <li> {@link CAMessage} which contains CAT of MMT-SI.
 * <li> {@link M2SectionMessage} which contains ECM, EMM, DCM, DMM,
 * MH-EIT, MH-CDT, MH-BIT, MH-SDTT, MH-SDT, MH-AIT of MMT-SI.
 * <li> {@link M2ShortSectionMessage} which contains MH-TOT of MMT-SI.
 * <li> {@link DataTransmissionMessage} which contains DDMT, DAMT, DCCT of MMT-SI.
 * </ul>
 */
public class MessageFactory {
    public final static int PACKAGE_ACCESS_MESSAGE = 0x0000;
    public final static int PA_MESSAGE = PACKAGE_ACCESS_MESSAGE;
    public final static int M2_SECTION_MESSAGE = 0x8000;
    public final static int M2S_MESSAGE = M2_SECTION_MESSAGE;
    public final static int CONDITIONAL_ACCESS_MESSAGE = 0x8001;
    public final static int CA_MESSAGE = CONDITIONAL_ACCESS_MESSAGE;
    public final static int M2_SHORT_SECTION_MESSAGE = 0x8002;
    public final static int M2SS_MESSAGE = M2_SHORT_SECTION_MESSAGE;
    public final static int DATA_TRANSMISSION_MESSAGE = 0x8003;
    public final static int DT_MESSAGE = DATA_TRANSMISSION_MESSAGE;

    /**
     * Creates Message from byte buffer as message_byte of MMTP_payload.
     * @param buffer message_byte of signaling message of MMTP_payload
     * @return Message to describe PA, CA or more Message.
     */
    public static Message createMessage(byte[] buffer) {
        int message_id = ((buffer[0] & 0xff) << 8) | (buffer[1] & 0xff);

        switch ( message_id ) {
            case PACKAGE_ACCESS_MESSAGE:
                return new PAMessage(buffer);
            case CONDITIONAL_ACCESS_MESSAGE:
                return new CAMessage(buffer);
            case M2_SECTION_MESSAGE:
                return new M2SectionMessage(buffer);
            case M2_SHORT_SECTION_MESSAGE:
                return new M2ShortSectionMessage(buffer);
            case DATA_TRANSMISSION_MESSAGE:
                return new DataTransmissionMessage(buffer);
            default:
                return null;
        }
    }

    private MessageFactory() {

    }
}
