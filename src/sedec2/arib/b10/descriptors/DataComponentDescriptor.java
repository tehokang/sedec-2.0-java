package sedec2.arib.b10.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class DataComponentDescriptor extends Descriptor {
    protected int data_component_id;
    protected byte[] additional_dta_component_info;
    
    public DataComponentDescriptor(BitReadWriter brw) {
        super(brw);
        
        data_component_id = brw.readOnBuffer(16);
        additional_dta_component_info = new byte[descriptor_length-2];
        
        for ( int i=0; i<descriptor_length-2; i++ ) {
            additional_dta_component_info[i] = (byte) brw.readOnBuffer(8);
        }
    }

    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t data_component_id : 0x%x \n",  data_component_id));
        for ( int i=0; i<additional_dta_component_info.length; i ++ ) {
        Logger.d(String.format("\t additional_data_component_info[%d] : 0x%x \n", 
                i, additional_dta_component_info[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + additional_dta_component_info.length;
    }

}
