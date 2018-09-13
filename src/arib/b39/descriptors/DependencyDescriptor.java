package arib.b39.descriptors;

import java.util.ArrayList;
import java.util.List;

import base.BitReadWriter;
import util.Logger;

public class DependencyDescriptor extends Descriptor {
    protected byte num_of_dependencies;
    protected List<Asset> assets = new ArrayList<>();
    
    class Asset {
        public int asset_id_scheme;
        public byte asset_id_length;
        public byte[] asset_id_byte;
    }
    
    public DependencyDescriptor(BitReadWriter brw) {
        super();
        
        descriptor_tag = brw.ReadOnBuffer(16);
        descriptor_length = brw.ReadOnBuffer(16);
        
        num_of_dependencies = (byte) brw.ReadOnBuffer(8);
        
        for ( int i=0; i<num_of_dependencies; i++ ) {
            Asset asset = new Asset();
            asset.asset_id_scheme = brw.ReadOnBuffer(32);
            asset.asset_id_length = (byte) brw.ReadOnBuffer(8);
            
            asset.asset_id_byte = new byte[asset.asset_id_length];
            for ( int j=0; j<asset.asset_id_length; j++ ) {
                asset.asset_id_byte[j] = (byte) brw.ReadOnBuffer(8);
            }
            assets.add(asset);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t num_of_dependencies : 0x%x \n", num_of_dependencies));
        
        for ( int i=0; i<assets.size(); i++ ) {
            Asset asset = assets.get(i);
            Logger.d(String.format("\t [%d] asset_id_scheme : 0x%x \n", 
                    asset.asset_id_scheme));
            Logger.d(String.format("\t [%d] asset_id_length : 0x%x \n", 
                    asset.asset_id_length));
            Logger.d(String.format("\t [%d] asset_id_byte : %s \n", 
                    new String(asset.asset_id_byte)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
        for ( int i=0; i<assets.size(); i++ ) {
            descriptor_length += (4 + 1 + assets.get(i).asset_id_length);
        }
    }
}