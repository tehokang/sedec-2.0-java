package sedec2.arib.tlv.container.mmtp.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sedec2.base.Table;
import sedec2.util.Logger;

/**
 * Abstraction class to decode header of message which every messages has to inherited from this.
 * <ul>
 * <li> {@link PAMessage} which contains MPT, PLT, LCT of MMT-SI.
 * <li> {@link CAMessage} which contains CAT of MMT-SI.
 * <li> {@link M2SectionMessage} which contains ECM, EMM, DCM, DMM,
 * MH-EIT, MH-CDT, MH-BIT, MH-SDTT, MH-SDT, MH-AIT of MMT-SI.
 * <li> {@link M2ShortSectionMessage} which contains MH-TOT of MMT-SI.
 * <li> {@link DataTransmissionMessage} which contains DDMT, DAMT, DCCT of MMT-SI.
 * </ul>
 */
public abstract class Message extends sedec2.base.BitReadWriter {
    protected int message_id;
    protected int version;
    protected int length;

    protected List<Table> tables = new ArrayList<>();

    /**
     * Constructor to decode header of Message
     * @param buffer message_byte of MMTP payload
     */
    public Message(byte[] buffer) {
        super(buffer);
    }

    /**
     * Gets length of Message like PA, CA, DataTransmission, M2, M2Short only for encoding
     * @return length of Message updated
     */
    public abstract int getMessageLength();

    /**
     * Gets message_id of Message.
     * @return message_id as 16 bits
     */
    public int getMessageId() {
        return message_id;
    }

    /**
     * Get version of Message.
     * @return version as 8 bits
     */
    public int getVersion() {
        return version;
    }

    /**
     * Gets length of Message
     * @return length of Message
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets list of tables which Message contains
     * @return tables as list of tables
     */
    public List<Table> getTables() {
        return tables;
    }

    public void encode() {
        __encode_update_message_length__();
        __encode_preprare_message__();

        __encode_prepare_buffer__();
        __encode_write_message_header__();
        __encode_write_message_body__();
    }

    /**
     * Prints all of properties it has.
     * Child has to override print to show properties it has.
     */
    public void print() {
        Logger.d(String.format("======= Message Header ======= (%s)\n", getClass().getName()));
        Logger.d(String.format("message_id : 0x%x \n", message_id));
        Logger.d(String.format("version : 0x%x \n", version));
        Logger.d(String.format("length : 0x%x (%d) \n", length, length));
        Logger.d("------------------------------ \n");
    }

    protected abstract void __decode_message_body__();

    /**
     * @note internal functions to encode
     */
    protected void __encode_update_message_length__() {};
    protected void __encode_preprare_message__() {};
    protected void __encode_prepare_buffer__() {
        if(m_buffer != null) {
            m_buffer = null;
        }
        m_buffer = new byte[length + 3];

        Arrays.fill(m_buffer, (byte)0xff);
    }

    protected void __encode_write_message_header__() {
        writeOnBuffer( message_id, 16 );
        writeOnBuffer( version, 8);
        writeOnBuffer( length, 32);
    }

    protected void __encode_write_message_body__() {};
}
