package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_HierachyDescriptor extends Descriptor {
    protected byte temporal_scalability_flag;
    protected byte spatial_scalability_flag;
    protected byte quality_scalability_flag;
    protected byte hierachy_type;
    protected byte hierachy_layer_index;
    protected byte tref_present_flag;
    protected byte hierachy_embedded_layer_index;
    protected byte hierachy_channel;
    
    public MH_HierachyDescriptor(BitReadWriter brw) {
        super(brw);
        
        brw.skipOnBuffer(1);
        
        temporal_scalability_flag = (byte) brw.readOnBuffer(1);
        spatial_scalability_flag = (byte) brw.readOnBuffer(1);
        quality_scalability_flag = (byte) brw.readOnBuffer(1);
        hierachy_type = (byte) brw.readOnBuffer(4);
        
        brw.skipOnBuffer(2);
        
        hierachy_layer_index = (byte) brw.readOnBuffer(6);
        tref_present_flag = (byte) brw.readOnBuffer(1);
        
        brw.skipOnBuffer(1);
        
        hierachy_embedded_layer_index = (byte) brw.readOnBuffer(6);
        
        brw.skipOnBuffer(2);
        
        hierachy_channel = (byte) brw.readOnBuffer(6);
    }
    
    public byte getTemporalScalabilityFlag() {
        return temporal_scalability_flag;
    }
    
    public byte getSpatialScalabilityFlag() {
        return spatial_scalability_flag;
    }
    
    public byte getQualityScalabilityFlag() {
        return quality_scalability_flag;
    }
    
    public byte getHierachyType() {
        return hierachy_type;
    }
    
    public byte getHierachyLayerIndex() {
        return hierachy_layer_index;
    }
    
    public byte getTrefPresentFlag() {
        return tref_present_flag;
    }
    
    public byte getHierachyEmbeddedLayerIndex() {
        return hierachy_embedded_layer_index;
    }
    
    public byte getHierachyChannel() {
        return hierachy_channel;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t temporal_scalability_flag : 0x%x \n",  
                temporal_scalability_flag));
        Logger.d(String.format("\t spatial_scalability_flag : 0x%x \n", 
                spatial_scalability_flag));
        Logger.d(String.format("\t quality_scalability_flag : 0x%x \n", 
                quality_scalability_flag));
        Logger.d(String.format("\t hierachy_type : 0x%x \n", hierachy_type));
        Logger.d(String.format("\t hierachy_layer_index : 0x%x \n", hierachy_layer_index));
        Logger.d(String.format("\t tref_present_flag : 0x%x \n", tref_present_flag));
        Logger.d(String.format("\t hierachy_embedded_layer_index : 0x%x \n", hierachy_layer_index));
        Logger.d(String.format("\t hierachy_channel : 0x%x \n", hierachy_channel));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4;
    }
}
