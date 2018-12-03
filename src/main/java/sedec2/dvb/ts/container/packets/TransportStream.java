package sedec2.dvb.ts.container.packets;

import java.util.Arrays;

import sedec2.base.BitReadWriter;
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
    protected byte[] data_byte;

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

        if ( adaptation_field_control == 2 || adaptation_field_control == 3 ) {
            data_byte = new byte[184];
            for ( int i=0; i<184; i++) {
                data_byte[i] = (byte) readOnBuffer(8);
            }
        }

        if ( adaptation_field_control == 1 || adaptation_field_control == 3 ) {
            data_byte = new byte[184];
            for ( int i=0; i<184; i++) {
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

    public byte[] getDataByte() {
        return Arrays.copyOfRange(data_byte, 0, data_byte.length);
    }

    public void print() {
        Logger.d(String.format("======= TS Header ======= (%s)\n", getClass().getName()));
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
    }
}
