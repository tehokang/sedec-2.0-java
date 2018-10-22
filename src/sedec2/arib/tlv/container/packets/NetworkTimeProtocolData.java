package sedec2.arib.tlv.container.packets;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class NetworkTimeProtocolData extends BitReadWriter {
    protected byte leap_indicator;
    protected byte version;
    protected byte mode;
    protected byte stratum;
    protected byte poll;
    protected byte precision;
    protected int root_delay;
    protected int root_dispersion;
    protected int reference_identification;
    protected long reference_timestamp;
    protected long origin_timestamp;
    protected long receive_timestamp;
    protected long transmit_timestamp;
    
    public NetworkTimeProtocolData(byte[] buffer) {
        super(buffer);
        
        leap_indicator = (byte) ReadOnBuffer(2);
        version = (byte) ReadOnBuffer(3);
        mode = (byte) ReadOnBuffer(3);
        stratum = (byte) ReadOnBuffer(8);
        poll = (byte) ReadOnBuffer(8);
        precision = (byte) ReadOnBuffer(8);
        
        root_delay = ReadOnBuffer(32);
        root_dispersion = ReadOnBuffer(32);
        reference_identification = ReadOnBuffer(32);
        
        reference_timestamp = ReadOnBuffer(64);
        origin_timestamp = ReadOnBuffer(64);
        receive_timestamp = ReadOnBuffer(64);
        transmit_timestamp = ReadOnBuffer(64);
    }

    public void Print() {
        Logger.d(String.format("=============== NTP =============== (%s)\n", getClass().getName()));
        Logger.d(String.format("leap_indicator : 0x%x \n", leap_indicator));
        Logger.d(String.format("version : 0x%x \n", version));
        Logger.d(String.format("mode : 0x%x \n", mode));
        Logger.d(String.format("stratum : 0x%x \n",  stratum));
        Logger.d(String.format("poll : 0x%x \n", poll));
        Logger.d(String.format("precision : 0x%x \n", precision));
        Logger.d(String.format("root_delay : 0x%x \n", root_delay));
        Logger.d(String.format("root_dispersion : 0x%x \n", root_dispersion));
        Logger.d(String.format("reference_identification : 0x%x \n", reference_identification));
        Logger.d(String.format("reference_timestamp : 0x%x \n", reference_timestamp));
        Logger.d(String.format("origin_timestamp : 0x%x \n", origin_timestamp));
        Logger.d(String.format("receive_timestamp : 0x%x \n", receive_timestamp));
        Logger.d(String.format("transmit_timestamp : 0x%x \n", transmit_timestamp));
        Logger.d(String.format("----------------------------------\n"));
    }
}
