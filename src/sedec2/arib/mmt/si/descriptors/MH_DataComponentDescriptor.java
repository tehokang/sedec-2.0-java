package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_DataComponentDescriptor extends Descriptor {
    protected int data_component_id;
    protected byte[] additional_data_component_info;
    
    public MH_DataComponentDescriptor(BitReadWriter brw) {
        super(brw);
        
        data_component_id = brw.ReadOnBuffer(16);
        
        additional_data_component_info = new byte[descriptor_length-2];
        for ( int i=0; i<additional_data_component_info.length; i++ ) {
            additional_data_component_info[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t data_component_id : 0x%x \n", data_component_id));
        Logger.d(String.format("\t addtional_data_component_info : %s \n", 
                new String(additional_data_component_info)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + additional_data_component_info.length;
    }

}
