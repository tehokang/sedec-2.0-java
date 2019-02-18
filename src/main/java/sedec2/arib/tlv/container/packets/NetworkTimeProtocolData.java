package sedec2.arib.tlv.container.packets;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * Class to parse Network Time Protocol Data.
 * NTP refers to Table 3-1 of chapter 3 of ARIB B60
 */
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

    /**
     * Constructor to decode Network Time Protocol Data
     * @param buffer NTP raw buffer inside of TLV
     */
    public NetworkTimeProtocolData(byte[] buffer) {
        super(buffer);

        leap_indicator = (byte) readOnBuffer(2);
        version = (byte) readOnBuffer(3);
        mode = (byte) readOnBuffer(3);
        stratum = (byte) readOnBuffer(8);
        poll = (byte) readOnBuffer(8);
        precision = (byte) readOnBuffer(8);

        root_delay = readOnBuffer(32);
        root_dispersion = readOnBuffer(32);
        reference_identification = readOnBuffer(32);

        reference_timestamp = readLongOnBuffer(64);
        origin_timestamp = readLongOnBuffer(64);
        receive_timestamp = readLongOnBuffer(64);
        transmit_timestamp = readLongOnBuffer(64);
    }

    public void print() {
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
