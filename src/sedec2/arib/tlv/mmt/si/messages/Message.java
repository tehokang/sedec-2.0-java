package sedec2.arib.tlv.mmt.si.messages;

import java.util.Arrays;

import sedec2.util.Logger;

public abstract class Message extends sedec2.base.BitReadWriter {   
    protected int message_id;
    protected int version;
    protected int length;
    
    public Message(byte[] buffer) {
        super(buffer);
    }
    
    public int getMessageLength() {
        return length + 7;
    }
    
    public int getMessageId() {
        return message_id;
    }
    
    public int getVersion() {
        return version;
    }
    
    public int getLength() {
        return length;
    }
    
    public void encode() {
        __encode_update_message_length__();
        __encode_preprare_message__();

        __encode_prepare_buffer__();
        __encode_write_message_header__();
        __encode_write_message_body__();
    }
    
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
