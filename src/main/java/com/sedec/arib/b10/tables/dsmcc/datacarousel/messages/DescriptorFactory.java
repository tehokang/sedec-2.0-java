package com.sedec.arib.b10.tables.dsmcc.datacarousel.messages;

import com.sedec.arib.b10.descriptors.UnknownDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.CRC32Descriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.CachingPriorityDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.CompressedModuleDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.EstimatedDownloadTimeDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.GroupLinkDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.InfoDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.LocationDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.ModuleLinkDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.NameDescriptor;
import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.TypeDescriptor;
import com.sedec.base.BitReadWriter;
import com.sedec.base.Descriptor;

public class DescriptorFactory {
    public static final int TYPE_DESCRIPTOR = 0x01;
    public static final int NAME_DESCRIPTOR = 0x02;
    public static final int INFO_DESCRIPTOR = 0x03;
    public static final int MODULE_LINK_DESCRIPTOR = 0x04;
    public static final int CRC32_DESCRIPTOR = 0x05;
    public static final int LOCATION_DESCRIPTOR = 0x06;
    public static final int EST_DOWNLOAD_TIME_DESCRIPTOR = 0x07;
    public static final int GROUP_LINK_DESCRIPTOR = 0x08;
    public static final int COMPRESSED_MODULE_DESCRIPTOR = 0x09;
    public static final int CACHING_PRIORITY_DESCRIPTOR = 0x71;

    public static Descriptor createDescriptor(BitReadWriter brw) {
        int descriptor_tag = brw.getCurrentBuffer()[0] & 0x0000ff;

        switch ( descriptor_tag ) {
            case TYPE_DESCRIPTOR:
                return new TypeDescriptor(brw);
            case NAME_DESCRIPTOR:
                return new NameDescriptor(brw);
            case INFO_DESCRIPTOR:
                return new InfoDescriptor(brw);
            case MODULE_LINK_DESCRIPTOR:
                return new ModuleLinkDescriptor(brw);
            case CRC32_DESCRIPTOR:
                return new CRC32Descriptor(brw);
            case LOCATION_DESCRIPTOR:
                return new LocationDescriptor(brw);
            case EST_DOWNLOAD_TIME_DESCRIPTOR:
                return new EstimatedDownloadTimeDescriptor(brw);
            case GROUP_LINK_DESCRIPTOR:
                return new GroupLinkDescriptor(brw);
            case COMPRESSED_MODULE_DESCRIPTOR:
                return new CompressedModuleDescriptor(brw);
            case CACHING_PRIORITY_DESCRIPTOR:
                return new CachingPriorityDescriptor(brw);
            default:
                return new UnknownDescriptor(brw);
        }
    }

    private DescriptorFactory() {

    }
}
