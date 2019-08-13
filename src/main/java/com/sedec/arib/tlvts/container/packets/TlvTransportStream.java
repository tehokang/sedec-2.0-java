package com.sedec.arib.tlvts.container.packets;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

/**
 * Class represents  Table-1 of ITU-T J.288 Encapsulation of TLV for cable transmission systems
 */
public class TlvTransportStream extends BitReadWriter {
    protected byte sync_byte;
    protected byte transport_error_indicator;
    protected byte TLV_start_indicator;
    protected int PID;
    protected byte[] fragmented_tlv_packet;

    public TlvTransportStream(byte[] buffer) {
        super(buffer);

        sync_byte = (byte) readOnBuffer(8);
        transport_error_indicator = (byte) readOnBuffer(1);
        TLV_start_indicator = (byte) readOnBuffer(1);
        skipOnBuffer(1);
        PID = readOnBuffer(13);

        fragmented_tlv_packet = new byte[188-3];
        for ( int i=0; i<fragmented_tlv_packet.length; i++ ) {
            fragmented_tlv_packet[i] = (byte) readOnBuffer(8);
        }
    }

    public byte getSyncByte() {
        return sync_byte;
    }

    public byte getTransportErrorIndicator() {
        return transport_error_indicator;
    }

    public byte getTLVStartIndicator() {
        return TLV_start_indicator;
    }

    public int getPID() {
        return PID;
    }

    public byte[] getFragmentedTlvPacket() {
        return fragmented_tlv_packet;
    }

    public byte getPointerField() {
        if ( fragmented_tlv_packet != null ) {
            return fragmented_tlv_packet[0];
        }
        return 0;
    }

    public void print() {
        Logger.d(String.format("======= Transport-Stream (Fragmented TLV) ======= (%s)\n",
                getClass().getName()));
        Logger.d(String.format("sync_byte : 0x%x \n", sync_byte));
        Logger.d(String.format("transport_error_indicator : 0x%x \n",
                transport_error_indicator));
        Logger.d(String.format("TLV_start_indicator : 0x%x \n", TLV_start_indicator));
        Logger.d(String.format("PID : 0x%x \n", PID));

        if ( fragmented_tlv_packet != null ) BinaryLogger.print(fragmented_tlv_packet);
    }
}
