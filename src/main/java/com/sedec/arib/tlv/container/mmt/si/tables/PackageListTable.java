package com.sedec.arib.tlv.container.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.tlv.container.mmt.si.DescriptorFactory;
import com.sedec.arib.tlv.container.mmt.si.descriptors.Descriptor;
import com.sedec.arib.tlv.container.mmt.si.info.MMTGeneralLocationInfo;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class PackageListTable extends Table {
    protected byte num_of_package;
    protected List<Package> packages = new ArrayList<>();
    protected byte num_of_ip_delivery;
    protected List<Delivery> deliveries = new ArrayList<>();

    public class Package {
        public byte MMT_package_id_length;
        public byte [] MMT_package_id_byte;
        public MMTGeneralLocationInfo MMT_general_location_info;
    }

    public class Delivery {
        public int transport_file_id;
        public byte location_type;
        public IPv4 ipv4 = new IPv4();
        public IPv6 ipv6 = new IPv6();
        public byte URL_length;
        public byte[] URL_byte;
        public int descriptor_loop_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }

    public class IPv4 {
        public byte[] src_addr = new byte[4];
        public byte[] dst_addr = new byte[4];
        public int dst_port;
    }

    public class IPv6 {
        public byte[] src_addr = new byte[16];
        public byte[] dst_addr = new byte[16];
        public int dst_port;
    }

    public PackageListTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public byte getNumberOfPackage() {
        return num_of_package;
    }

    public List<Package> getPackages() {
        return packages;
    }

    public byte getNumberOfDelivery() {
        return num_of_ip_delivery;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    @Override
    protected void __decode_table_body__() {
        num_of_package = (byte) readOnBuffer(8);

        for ( int i=0; i<num_of_package; i++ ) {
            Package pkg = new Package();
            pkg.MMT_package_id_length = (byte) readOnBuffer(8);
            pkg.MMT_package_id_byte = new byte[pkg.MMT_package_id_length];

            for ( int j=0; j<pkg.MMT_package_id_length; j++ ) {
                pkg.MMT_package_id_byte[j] = (byte) readOnBuffer(8);
            }

            pkg.MMT_general_location_info = new MMTGeneralLocationInfo(this);
            packages.add(pkg);
        }

        num_of_ip_delivery = (byte) readOnBuffer(8);

        for ( int i=0; i<num_of_ip_delivery; i++ ) {
            Delivery delivery = new Delivery();
            delivery.transport_file_id = readOnBuffer(32);
            delivery.location_type = (byte) readOnBuffer(8);

            switch ( delivery.location_type ) {
                case 0x01:
                    {
                        for ( int j=0; j<4; j++ )
                            delivery.ipv4.src_addr[j] = (byte) readOnBuffer(8);
                        for ( int j=0; j<4; j++ )
                            delivery.ipv4.dst_addr[j] = (byte) readOnBuffer(8);
                        delivery.ipv4.dst_port = readOnBuffer(16);
                    }
                    break;
                case 0x02:
                    {
                        for ( int j=0; j<16; j++ )
                            delivery.ipv6.src_addr[j] = (byte) readOnBuffer(8);
                        for ( int j=0; j<16; j++ )
                            delivery.ipv6.dst_addr[j] = (byte) readOnBuffer(8);
                        delivery.ipv6.dst_port = readOnBuffer(16);
                    }
                    break;
                case 0x05:
                    delivery.URL_length = (byte) readOnBuffer(8);
                    delivery.URL_byte = new byte[delivery.URL_length];

                    for ( int j=0; j<delivery.URL_length; j++ ) {
                        delivery.URL_byte[j] = (byte) readOnBuffer(8);
                    }
                    break;
                default:
                    break;
            }

            delivery.descriptor_loop_length = readOnBuffer(16);
            for ( int j=delivery.descriptor_loop_length; j>0; ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                j-=desc.getDescriptorLength();
                delivery.descriptors.add(desc);
            }
            deliveries.add(delivery);
        }
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("num_of_package : 0x%x \n", num_of_package));

        for ( int i=0; i<packages.size(); i++ ) {
            Package pkg = packages.get(i);
            Logger.d(String.format("[%d] MMT_package_id_length : 0x%x \n",
                    i, pkg.MMT_package_id_length));
            Logger.d(String.format("[%d] MMT_package_id_byte :\n", i));
            BinaryLogger.debug(pkg.MMT_package_id_byte);
            pkg.MMT_general_location_info.print();
        }

        Logger.d(String.format("num_of_ip_delivery : 0x%x \n", num_of_ip_delivery));

        for ( int i=0; i<deliveries.size(); i++ ) {
            Delivery delivery = deliveries.get(i);
            Logger.d(String.format("[%d] transport_file_id : 0x%x \n",
                    i, delivery.transport_file_id));
            Logger.d(String.format("[%d] location_type : 0x%x \n",
                    i, delivery.location_type));

            switch ( delivery.location_type ) {
                case 0x01:
                    {
                        Logger.d(String.format("[%d] ipv4.src_addr : %d.%d.%d.%d \n",
                                i, delivery.ipv4.src_addr[0], delivery.ipv4.src_addr[1],
                                delivery.ipv4.src_addr[2], delivery.ipv4.src_addr[3]));

                        Logger.d(String.format("[%d] ipv4.dst_addr : %d.%d.%d.%d \n",
                                i, delivery.ipv4.dst_addr[0], delivery.ipv4.dst_addr[1],
                                delivery.ipv4.dst_addr[2], delivery.ipv4.dst_addr[3]));
                        Logger.d(String.format("[%d] dst_port : 0x%x \n",
                                i, delivery.ipv4.dst_port));
                    }
                    break;
                case 0x02:
                    {
                        Logger.d(String.format("\t [%d] ipv6.src_address_128 : "
                                + "%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x \n",
                                i, delivery.ipv6.src_addr[0], delivery.ipv6.src_addr[1],
                                delivery.ipv6.src_addr[2], delivery.ipv6.src_addr[3],
                                delivery.ipv6.src_addr[4], delivery.ipv6.src_addr[5],
                                delivery.ipv6.src_addr[6], delivery.ipv6.src_addr[7],
                                delivery.ipv6.src_addr[8], delivery.ipv6.src_addr[9],
                                delivery.ipv6.src_addr[10], delivery.ipv6.src_addr[11],
                                delivery.ipv6.src_addr[12], delivery.ipv6.src_addr[12],
                                delivery.ipv6.src_addr[14], delivery.ipv6.src_addr[15]));

                        Logger.d(String.format("\t [%d] ipv6.src_address_128 : "
                                + "%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x \n",
                                i, delivery.ipv6.dst_addr[0], delivery.ipv6.dst_addr[1],
                                delivery.ipv6.dst_addr[2], delivery.ipv6.dst_addr[3],
                                delivery.ipv6.dst_addr[4], delivery.ipv6.dst_addr[5],
                                delivery.ipv6.dst_addr[6], delivery.ipv6.dst_addr[7],
                                delivery.ipv6.dst_addr[8], delivery.ipv6.dst_addr[9],
                                delivery.ipv6.dst_addr[10], delivery.ipv6.dst_addr[11],
                                delivery.ipv6.dst_addr[12], delivery.ipv6.dst_addr[12],
                                delivery.ipv6.dst_addr[14], delivery.ipv6.dst_addr[15]));
                        Logger.d(String.format("[%d] dst_port : 0x%x \n",
                                i, delivery.ipv6.dst_port));
                    }
                    break;
                case 0x05:
                    Logger.d(String.format("[%d] URL_length : 0x%x \n",
                            i, delivery.URL_length));
                    Logger.d(String.format("[%d] URL_byte : %s \n",
                            i, new String(delivery.URL_byte)));
                    break;
                default:
                    break;
            }

            Logger.d(String.format("[%d] descriptor_loop_length : 0x%x \n",
                    delivery.descriptor_loop_length));

            for ( int j=delivery.descriptors.size(); j>0; ) {
                delivery.descriptors.get(j).print();
            }
        }
    }

}
