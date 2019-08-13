package com.sedec.arib.tlv.si;

import com.sedec.arib.tlv.si.descriptors.ChannelBondingCableDeliverySystemDescriptor;
import com.sedec.arib.tlv.si.descriptors.Descriptor;
import com.sedec.arib.tlv.si.descriptors.NetworkNameDescriptor;
import com.sedec.arib.tlv.si.descriptors.SateliteDeliverySystemDescriptor;
import com.sedec.arib.tlv.si.descriptors.ServiceListDescriptor;
import com.sedec.arib.tlv.si.descriptors.SystemManagementDescriptor;
import com.sedec.arib.tlv.si.descriptors.UnknownDescriptor;
import com.sedec.base.BitReadWriter;

/**
 * Factory to obtain a kind of descriptors of TLV-SI like below.
 * <ul>
 * <li> {@link ChannelBondingCableDeliverySystemDescriptor}
 * <li> {@link NetworkNameDescriptor}
 * <li> {@link SateliteDeliverySystemDescriptor}
 * <li> {@link ServiceListDescriptor}
 * <li> {@link SystemManagementDescriptor}
 * </ul>
 */
public class DescriptorFactory {
    public final static int NETWORK_NAME_DESCRIPTOR = 0x40;
    public final static int SERVICE_LIST_DESCRIPTOR = 0x41;
    public final static int SATELITE_DELIVERY_SYSTEM_DESCRIPTOR = 0x43;

    public final static int CHANNEL_BONDING_CABLE_DELIVERY_SYSTEM_DESCRIPTOR = 0xf3;
    public final static int SYSTEM_MANAGEMENT_DESCRIPTOR = 0xfe;

    public final static int UNKNOWN_DESCRIPTOR = 0xff;

    public static Descriptor createDescriptor(BitReadWriter brw) {
        int descriptor_tag = brw.getCurrentBuffer()[0] & 0x0000ff;

        switch ( descriptor_tag ) {
            case SYSTEM_MANAGEMENT_DESCRIPTOR:
                return new SystemManagementDescriptor(brw);
            case SATELITE_DELIVERY_SYSTEM_DESCRIPTOR:
                return new SateliteDeliverySystemDescriptor(brw);
            case NETWORK_NAME_DESCRIPTOR:
                return new NetworkNameDescriptor(brw);
            case SERVICE_LIST_DESCRIPTOR:
                return new ServiceListDescriptor(brw);
            case CHANNEL_BONDING_CABLE_DELIVERY_SYSTEM_DESCRIPTOR:
                return new ChannelBondingCableDeliverySystemDescriptor(brw);
            case UNKNOWN_DESCRIPTOR:
            default:
                return new UnknownDescriptor(brw);
        }
    }

    private DescriptorFactory() {

    }
}
