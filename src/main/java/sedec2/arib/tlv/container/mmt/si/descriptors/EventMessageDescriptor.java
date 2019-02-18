package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class EventMessageDescriptor extends Descriptor {
    protected int event_msg_group_id;
    protected byte time_mode;
    protected long event_msg_UTC;
    protected long event_msg_NPT;
    protected long event_msg_relativeTime;
    protected byte event_msg_type;
    protected int event_msg_id;
    protected byte[] private_data_byte;

    public EventMessageDescriptor(BitReadWriter brw) {
        super();

        descriptor_tag = brw.readOnBuffer(16);
        descriptor_length = brw.readOnBuffer(16);

        event_msg_group_id = brw.readOnBuffer(12);
        brw.skipOnBuffer(4);
        time_mode = (byte) brw.readOnBuffer(8);

        if ( time_mode == 0 ) {
            brw.skipOnBuffer(64);
        } else if ( time_mode == 0x01 || time_mode == 0x05 ) {
            event_msg_UTC = brw.readLongOnBuffer(64);
        } else if ( time_mode == 0x02 ) {
            event_msg_NPT = brw.readLongOnBuffer(64);
        } else if ( time_mode == 0x03 ) {
            event_msg_relativeTime = brw.readLongOnBuffer(64);
        }

        event_msg_type = (byte) brw.readOnBuffer(8);
        event_msg_id = brw.readOnBuffer(16);

        private_data_byte = new byte[descriptor_length - 3 - 8 - 1 - 2];
        for ( int i=0; i<private_data_byte.length; i++ ) {
            private_data_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getEventMsgGroupId() {
        return event_msg_group_id;
    }

    public byte getTimeMode() {
        return time_mode;
    }

    public long getEventMsgUTC() {
        return event_msg_UTC;
    }

    public long getEventMsgNPT() {
        return event_msg_NPT;
    }

    public long getEventMsgRelativeTime() {
        return event_msg_relativeTime;
    }

    public byte getEventMsgType() {
        return event_msg_type;
    }

    public int getEventMsgId() {
        return event_msg_id;
    }

    public byte[] getPrivateData() {
        return private_data_byte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t event_msg_group_id : 0x%x \n", event_msg_group_id));
        Logger.d(String.format("\t time_mode : 0x0%x \n", time_mode));
        Logger.d(String.format("\t event_msg_UTC : 0x%x \n", event_msg_UTC));
        Logger.d(String.format("\t event_msg_NPT : 0x%x \n", event_msg_NPT));
        Logger.d(String.format("\t event_msg_relativeTime : 0x%x \n", event_msg_relativeTime));
        Logger.d(String.format("\t event_msg_type : 0x%x \n", event_msg_type));
        Logger.d(String.format("\t event_msg_id : 0x%x \n", event_msg_id));
        Logger.d(String.format("\t private_data_byte : \n"));

        BinaryLogger.print(private_data_byte);
    }

    @Override
    public int getDescriptorLength() {
        updateDescriptorLength();
        return descriptor_length + 4;
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 14 + private_data_byte.length;
    }
}
