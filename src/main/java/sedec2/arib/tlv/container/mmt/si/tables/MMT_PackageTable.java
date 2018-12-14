package sedec2.arib.tlv.container.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.container.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.container.mmt.si.descriptors.Descriptor;
import sedec2.arib.tlv.container.mmt.si.info.MMTGeneralLocationInfo;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MMT_PackageTable extends Table {
    protected byte MPT_mode;
    protected byte MMT_package_id_length;
    protected byte[] MMT_package_id_byte;
    protected int MPT_descriptor_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    protected byte number_of_assets;
    protected List<Asset> assets = new ArrayList<>();

    public class Asset {
        public byte identifier_type;
        public int asset_id_scheme;
        public byte asset_id_length;
        public byte[] asset_id_byte;
        public byte[] asset_type = new byte[4];
        public byte asset_clock_relation_flag;
        public byte location_count;

        public List<MMTGeneralLocationInfo> infos = new ArrayList<>();
        public int asset_descriptors_length;
        public List<Descriptor> descriptors = new ArrayList<>();

        public int getAssetId() {
           return ((asset_id_byte[0] & 0xff) << 8 | asset_id_byte[1]);
        }
    }

    public MMT_PackageTable(byte[] buffer) {
        super(buffer);

        __decode_table_body__();
    }

    public byte getMPTMode() {
        return MPT_mode;
    }

    public byte getMMTPackageIdLength() {
        return MMT_package_id_length;
    }

    public byte[] getMMTPackageIdByte() {
        return MMT_package_id_byte;
    }

    public int getMPTDescriptorLength() {
        return MPT_descriptor_length;
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    public byte getNumberOfAssets() {
        return number_of_assets;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    @Override
    protected void __decode_table_body__() {
        skipOnBuffer(6);
        MPT_mode = (byte) readOnBuffer(2);
        MMT_package_id_length = (byte) readOnBuffer(8);
        MMT_package_id_byte = new byte[MMT_package_id_length];

        for ( int i=0; i<MMT_package_id_byte.length; i++ ) {
            MMT_package_id_byte[i] = (byte) readOnBuffer(8);
        }

        MPT_descriptor_length = readOnBuffer(16);
        for ( int i=MPT_descriptor_length; i>0; ) {
            Descriptor desc = DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }

        number_of_assets = (byte) readOnBuffer(8);

        for ( int i=0; i<number_of_assets; i++ ) {
            Asset asset = new Asset();
            asset.identifier_type = (byte) readOnBuffer(8);
            asset.asset_id_scheme = readOnBuffer(32);
            asset.asset_id_length = (byte) readOnBuffer(8);
            asset.asset_id_byte = new byte[asset.asset_id_length];

            for ( int j=0; j<asset.asset_id_byte.length; j++ ) {
                asset.asset_id_byte[j] = (byte) readOnBuffer(8);
            }

            asset.asset_type[0] = (byte) readOnBuffer(8);
            asset.asset_type[1] = (byte) readOnBuffer(8);
            asset.asset_type[2] = (byte) readOnBuffer(8);
            asset.asset_type[3] = (byte) readOnBuffer(8);

            skipOnBuffer(7);
            asset.asset_clock_relation_flag = (byte) readOnBuffer(1);
            asset.location_count = (byte) readOnBuffer(8);

            for ( int j=0; j<asset.location_count; j++ ) {
                MMTGeneralLocationInfo info = new MMTGeneralLocationInfo(this);
                asset.infos.add(info);
            }

            asset.asset_descriptors_length = readOnBuffer(16);
            for ( int j=asset.asset_descriptors_length; j>0; ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                j-=desc.getDescriptorLength();
                asset.descriptors.add(desc);
            }
            assets.add(asset);
        }
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("MPT_mode : 0x%x \n", MPT_mode));
        Logger.d(String.format("MMT_package_id_length : 0x%x \n", MMT_package_id_length));
        Logger.d(String.format("MMT_package_id_byte : \n"));

        BinaryLogger.print(MMT_package_id_byte);

        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }

        Logger.d(String.format("number_of_assets : 0x%x \n\n", number_of_assets));
        for ( int i=0; i<assets.size(); i++ ) {
            Asset asset = assets.get(i);
            Logger.d(String.format("[%d] ----- Asset Information ----- \n", i));
            Logger.d(String.format("[%d] identifier_type : 0x%x \n", i, asset.identifier_type));
            Logger.d(String.format("[%d] asset_id_scheme : 0x%x \n", i, asset.asset_id_scheme));
            Logger.d(String.format("[%d] asset_id_length : 0x%x \n", i, asset.asset_id_length));
            Logger.d(String.format("[%d] asset_id_byte : \n", i));

            BinaryLogger.debug(asset.asset_id_byte);

            Logger.d(String.format("[%d] asset_type : %s \n",  i, new String(asset.asset_type)));
            Logger.d(String.format("[%d] asset_clock_relation_flag : 0x%x \n",
                    i, asset.asset_clock_relation_flag));
            Logger.d(String.format("[%d] location_count : 0x%x \n", i, asset.location_count));
            for ( int j=0; j<asset.infos.size(); j++ ) {
                asset.infos.get(j).print();
            }

            for ( int j=0; j<asset.descriptors.size(); j++ ) {
                asset.descriptors.get(j).print();
            }
            Logger.d("\n");
        }
    }

}
