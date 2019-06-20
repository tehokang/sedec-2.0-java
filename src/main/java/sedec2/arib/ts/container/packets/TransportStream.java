package sedec2.arib.ts.container.packets;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class TransportStream extends BitReadWriter {
    protected byte sync_byte;
    protected byte transport_error_indicator;
    protected byte payload_unit_start_indicator;
    protected byte transport_priority;
    protected int PID;
    protected byte transport_scrambling_control;
    protected byte adaptation_field_control;
    protected byte continuity_counter;
    protected byte pointer_field;
    protected AdaptationField adaptation_field = null;
    protected byte[] data_byte = {0x00, };

    public TransportStream(byte[] buffer) {
        super(buffer);

        sync_byte = (byte) readOnBuffer(8);
        transport_error_indicator = (byte) readOnBuffer(1);
        payload_unit_start_indicator = (byte) readOnBuffer(1);
        transport_priority = (byte) readOnBuffer(1);
        PID = readOnBuffer(13);
        transport_scrambling_control = (byte) readOnBuffer(2);
        adaptation_field_control = (byte) readOnBuffer(2);
        continuity_counter = (byte) readOnBuffer(4);

        int adaptation_field_length = 0;
        if ( adaptation_field_control == 0b10 || adaptation_field_control == 0b11 ) {
            adaptation_field = new AdaptationField(this);
            adaptation_field_length = adaptation_field.getAdaptationFieldLength() + 1;
        }

        if ( adaptation_field_control == 0b01 || adaptation_field_control == 0b11 ) {
            data_byte = new byte[188 - 4 - adaptation_field_length];
            for ( int i=0; i<data_byte.length; i++) {
                data_byte[i] = (byte) readOnBuffer(8);
            }
        }
    }

    public byte getTransportErrorIndicator() {
        return transport_error_indicator;
    }

    public byte getPayloadUnitStartIndicator() {
        return payload_unit_start_indicator;
    }

    public byte getTransportPriority() {
        return transport_priority;
    }

    public int getPID() {
        return PID;
    }

    public byte getTransportScramblingControl() {
        return transport_scrambling_control;
    }

    public byte getAdaptationFieldControl() {
        return adaptation_field_control;
    }

    public byte getContinuityCounter() {
        return continuity_counter;
    }

    public byte getPointerField() {
        if ( data_byte != null ) {
            return data_byte[0];
        }
        return 0;
    }

    public byte[] getDataByte() {
        return data_byte;
    }

    public void print() {
        Logger.d(String.format("======= Transport-Stream ======= (%s)\n", getClass().getName()));
        Logger.d(String.format("sync_byte : 0x%x \n", sync_byte));
        Logger.d(String.format("transport_error_indicator : 0x%x \n",
                transport_error_indicator));
        Logger.d(String.format("payload_unit_start_indicator : 0x%x \n",
                payload_unit_start_indicator));
        Logger.d(String.format("transport_priority : 0x%x \n",
                transport_priority));
        Logger.d(String.format("PID : 0x%x \n",  PID));
        Logger.d(String.format("transport_scrambling_control : 0x%x \n",
                transport_scrambling_control));
        Logger.d(String.format("adaptation_field_control : 0x%x \n",
                adaptation_field_control));
        Logger.d(String.format("continuity_counter : 0x%x \n", continuity_counter));

        if ( payload_unit_start_indicator == 1) {
            Logger.d(String.format("pointer_field : 0x%x \n", getPointerField()));
        }

        if ( adaptation_field != null ) adaptation_field.print();

        if ( data_byte != null ) BinaryLogger.print(data_byte);
    }
}
