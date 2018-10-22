package sedec2.arib.tlv.mmt.si.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.tlv.mmt.si.DescriptorFactory;
import sedec2.arib.tlv.mmt.si.descriptors.Descriptor;
import sedec2.arib.tlv.mmt.si.descriptors.MMTGeneralLocationInfo;
import sedec2.util.Logger;

public class MMT_PackageTable extends Table {
    protected byte MPT_mode;
    protected byte MMT_package_id_length;
    protected byte[] MMT_package_id_byte;
    protected int MPT_descriptor_length;
    protected List<Descriptor> descriptors = new ArrayList<>();
    protected byte number_of_assets;
    protected List<Asset> assets = new ArrayList<>();
    
    class Asset {
        public byte identifier_type;
        public int asset_id_scheme;
        public byte asset_id_length;
        public byte[] asset_id_byte;
        public int asset_type;
        public byte asset_clock_relation_flag;
        public byte location_count;
        
        public List<MMTGeneralLocationInfo> infos = new ArrayList<>();
        public int asset_descriptors_length;
        public List<Descriptor> descriptors = new ArrayList<>();
    }
    
    public MMT_PackageTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }
    
    public byte GetMPTMode() {
        return MPT_mode;
    }
    
    public byte GetMMTPackageIdLength() {
        return MMT_package_id_length;
    }
    
    public byte[] GetMMTPackageIdByte() {
        return MMT_package_id_byte;
    }
    
    public int GetMPTDescriptorLength() {
        return MPT_descriptor_length;
    }
    
    public List<Descriptor> GetDescriptors() {
        return descriptors;
    }
    
    public byte GetNumberOfAssets() {
        return number_of_assets;
    }
    
    public List<Asset> GetAssets() {
        return assets;
    }

    @Override
    protected void __decode_table_body__() {
        SkipOnBuffer(6);
        MPT_mode = (byte) ReadOnBuffer(2);
        MMT_package_id_length = (byte) ReadOnBuffer(8);
        MMT_package_id_byte = new byte[MMT_package_id_length];
        
        for ( int i=0; i<MMT_package_id_length; i++ ) {
            MMT_package_id_byte[i] = (byte) ReadOnBuffer(8);
        }
        
        MPT_descriptor_length = ReadOnBuffer(16);
        for ( int i=MPT_descriptor_length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
        
        number_of_assets = (byte) ReadOnBuffer(8);
        
        for ( int i=0; i<number_of_assets; i++ ) {
            Asset asset = new Asset();
            asset.identifier_type = (byte) ReadOnBuffer(8);
            asset.asset_id_scheme = ReadOnBuffer(32);
            asset.asset_id_length = (byte) ReadOnBuffer(8);
            asset.asset_id_byte = new byte[asset.asset_id_length];
            
            for ( int j=0; j<asset.asset_id_length; j++ ) {
                asset.asset_id_byte[j] = (byte) ReadOnBuffer(8);
            }
            asset.asset_type = ReadOnBuffer(32);
            SkipOnBuffer(7);
            asset.asset_clock_relation_flag = (byte) ReadOnBuffer(8);
            asset.location_count = (byte) ReadOnBuffer(8);
            
            for ( int j=0; j<asset.location_count; j++ ) {
                MMTGeneralLocationInfo info = new MMTGeneralLocationInfo(this);
                asset.infos.add(info);
            }
            
            asset.asset_descriptors_length = ReadOnBuffer(16);
            for ( int j=asset.asset_descriptors_length; j>0; ) {
                Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
                j-=desc.GetDescriptorLength();
                asset.descriptors.add(desc);
            }
            assets.add(asset);
        }
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("MPT_mode : 0x%x \n", MPT_mode));
        Logger.d(String.format("MMT_package_id_length : 0x%x \n", MMT_package_id_length));
        Logger.d(String.format("MMT_package_id_byte : %s \n", new String(MMT_package_id_byte)));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }
        
        Logger.d(String.format("number_of_assets : 0x%x \n", number_of_assets));
        for ( int i=0; i<assets.size(); i++ ) {
            Asset asset = assets.get(i);
            
            Logger.d(String.format("identifier_type : 0x%x \n", asset.identifier_type));
            Logger.d(String.format("asset_id_scheme : 0x%x \n", asset.asset_id_scheme));
            Logger.d(String.format("asset_id_length : 0x%x \n", asset.asset_id_length));
            Logger.d(String.format("asset_id_byte : %s \n", new String(asset.asset_id_byte)));
            Logger.d(String.format("asset_type : 0x%x \n",  asset.asset_type));
            Logger.d(String.format("asset_clock_relation_flag : 0x%x \n", 
                    asset.asset_clock_relation_flag));
            Logger.d(String.format("location_count : 0x%x \n", asset.location_count));
            for ( int j=0; j<asset.infos.size(); j++ ) {
                asset.infos.get(j).PrintInfo();
            }
            
            for ( int j=0; j<asset.descriptors.size(); j++ ) {
                asset.descriptors.get(j).PrintDescriptor();
            }
        }
    }

}
