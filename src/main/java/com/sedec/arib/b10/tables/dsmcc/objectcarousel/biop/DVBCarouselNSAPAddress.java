package com.sedec.arib.b10.tables.dsmcc.objectcarousel.biop;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class DVBCarouselNSAPAddress {
    protected byte AFI;
    protected byte Type;
    protected int carouselId;
    protected byte specifierType;
    protected int specifierData;
    protected int transport_stream_id;
    protected int original_network_id;
    protected int service_id;

    public DVBCarouselNSAPAddress(BitReadWriter brw) {
        AFI = (byte) brw.readOnBuffer(8);
        Type = (byte) brw.readOnBuffer(8);
        carouselId = brw.readOnBuffer(32);
        specifierType = (byte) brw.readOnBuffer(8);
        specifierData = brw.readOnBuffer(24);
        transport_stream_id = brw.readOnBuffer(16);
        original_network_id = brw.readOnBuffer(16);
        service_id = brw.readOnBuffer(16);
        brw.skipOnBuffer(32);
    }

    public byte getAFI() {
        return AFI;
    }

    public byte getType() {
        return Type;
    }

    public int getCarouselId() {
        return carouselId;
    }

    public byte getSpecifierType() {
        return specifierType;
    }

    public int getSpecifierData() {
        return specifierData;
    }

    public int getTransportStreamId() {
        return transport_stream_id;
    }

    public int getOriginalNetworkId() {
        return original_network_id;
    }

    public int getServiceId() {
        return service_id;
    }

    public int getLength() {
        return 20;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t AFI : 0x%x \n", AFI));
        Logger.d(String.format("\t Type : 0x%x \n", Type));
        Logger.d(String.format("\t carouselId : 0x%x \n", carouselId));
        Logger.d(String.format("\t specifierType : 0x%x \n", specifierType));
        Logger.d(String.format("\t specifierData : 0x%x \n", specifierData));
        Logger.d(String.format("\t transport_stream_id : 0x%x \n", transport_stream_id));
        Logger.d(String.format("\t original_network_id : 0x%x \n",  original_network_id));
        Logger.d(String.format("\t service_id : 0x%x \n", service_id));
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
