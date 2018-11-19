package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.arib.tlv.container.mmt.si.info.AdditionalAribSubtitleInfo;
import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_DataComponentDescriptor extends Descriptor {
    protected int data_component_id;
    protected byte[] additional_data_component_info;
    protected AdditionalAribSubtitleInfo additional_arib_subtitle_info;
    
    public MH_DataComponentDescriptor(BitReadWriter brw) {
        super(brw);
        
        data_component_id = brw.readOnBuffer(16);

        if ( data_component_id == 0x0020 ) {
            additional_arib_subtitle_info = new AdditionalAribSubtitleInfo(brw);
        } else if ( data_component_id == 0x0021 ) {
            additional_data_component_info = new byte[descriptor_length-2];
            for ( int i=0; i<additional_data_component_info.length; i++ ) {
                additional_data_component_info[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public int getDataComponentId() {
        return data_component_id;
    }
    
    public byte[] getAdditionalDataComponentInfo() {
        return additional_data_component_info;
    }
    
    public AdditionalAribSubtitleInfo getAdditionalAribSubtitleInfo() {
        return additional_arib_subtitle_info;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t data_component_id : 0x%x \n", data_component_id));
        
        if ( data_component_id == 0x0020 ) {
            additional_arib_subtitle_info.print();
        } else if ( data_component_id == 0x0021 ) {
            Logger.d(String.format("\t addtional_data_component_info : %s \n", 
                    new String(additional_data_component_info)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
        
        if ( data_component_id == 0x0020 ) {
            descriptor_length += additional_arib_subtitle_info.getLength();
        } else if ( data_component_id == 0x0021 ) {
            descriptor_length += additional_data_component_info.length;
        }
    }
}
