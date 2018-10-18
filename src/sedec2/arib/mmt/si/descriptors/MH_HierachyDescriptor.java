package sedec2.arib.mmt.si.descriptors;

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
        
        brw.SkipOnBuffer(1);
        
        temporal_scalability_flag = (byte) brw.ReadOnBuffer(1);
        spatial_scalability_flag = (byte) brw.ReadOnBuffer(1);
        quality_scalability_flag = (byte) brw.ReadOnBuffer(1);
        hierachy_type = (byte) brw.ReadOnBuffer(4);
        
        brw.SkipOnBuffer(2);
        
        hierachy_layer_index = (byte) brw.ReadOnBuffer(6);
        tref_present_flag = (byte) brw.ReadOnBuffer(1);
        
        brw.SkipOnBuffer(1);
        
        hierachy_embedded_layer_index = (byte) brw.ReadOnBuffer(6);
        
        brw.SkipOnBuffer(2);
        
        hierachy_channel = (byte) brw.ReadOnBuffer(6);
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("temporal_scalability_flag : 0x%x \n",  
                temporal_scalability_flag));
        Logger.d(String.format("spatial_scalability_flag : 0x%x \n", 
                spatial_scalability_flag));
        Logger.d(String.format("quality_scalability_flag : 0x%x \n", 
                quality_scalability_flag));
        Logger.d(String.format("hierachy_type : 0x%x \n", hierachy_type));
        Logger.d(String.format("hierachy_layer_index : 0x%x \n", hierachy_layer_index));
        Logger.d(String.format("tref_present_flag : 0x%x \n", tref_present_flag));
        Logger.d(String.format("hierachy_embedded_layer_index : 0x%x \n", hierachy_layer_index));
        Logger.d(String.format("hierachy_channel : 0x%x \n", hierachy_channel));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4;
    }
}
