package sedec2.arib.b39.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ApplicationServiceDescriptor extends Descriptor {
    protected byte application_format_map;
    protected byte default_AIT_flag;
    protected byte DT_message_flag;
    protected byte EMT_num;
    
    protected MMTGeneralLocationInfo AIT_location_info;
    protected MMTGeneralLocationInfo DT_message_location_info;
    protected List<MMTGeneralLocationInfo> EMT_location_infos = new ArrayList<>();
    protected byte[] private_data;
    
    public ApplicationServiceDescriptor(BitReadWriter brw) {
        super(brw);
        
        application_format_map = (byte) brw.ReadOnBuffer(8);
        default_AIT_flag = (byte) brw.ReadOnBuffer(1);
        DT_message_flag = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(2);
        EMT_num = (byte) brw.ReadOnBuffer(4);
        
        AIT_location_info = new MMTGeneralLocationInfo(brw);
        
        if ( DT_message_flag == 1 ) {
            DT_message_location_info = new MMTGeneralLocationInfo(brw);
        }
        
        for ( int i=0; i<EMT_num; i++ ) {
            MMTGeneralLocationInfo EMT_location_info = new MMTGeneralLocationInfo(brw);
            EMT_location_infos.add(EMT_location_info);
        }
        
        int private_data_length = descriptor_length;
        private_data_length -= AIT_location_info.GetLength();
        private_data_length -= (DT_message_flag == 1 ? DT_message_location_info.GetLength():0);
           
        for ( int i=0; i<EMT_location_infos.size(); i++ ) {
            MMTGeneralLocationInfo EMT_location_info = EMT_location_infos.get(i);
            private_data_length -= EMT_location_info.GetLength();
        }
        private_data = new byte[private_data_length];
        for ( int i=0; i<private_data_length; i++ ) {
            private_data[i] = (byte) brw.ReadOnBuffer(8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t application_format_map : 0x%x \n", application_format_map));
        Logger.d(String.format("\t default_AIT_flag : 0x%x \n", default_AIT_flag));
        Logger.d(String.format("\t DT_message_flag : 0x%x \n", DT_message_flag));
        Logger.d(String.format("\t EMT_num : 0x%x \n", EMT_num));
     
        AIT_location_info.PrintInfo();
        
        if ( DT_message_flag == 1 ) {
            DT_message_location_info.PrintInfo();
        }
        
        for ( int i=0; i<EMT_location_infos.size(); i++ ) {
            EMT_location_infos.get(i).PrintInfo();
        }
    }

    @Override
    protected void updateDescriptorLength() {
       descriptor_length = 2;
       descriptor_length += AIT_location_info.GetLength();

       if ( DT_message_flag == 1 ) {
           descriptor_length += DT_message_location_info.GetLength();
       }
       
       for ( int i=0; i<EMT_location_infos.size(); i++ ) {
           descriptor_length += EMT_location_infos.get(i).GetLength();
       }
       
       descriptor_length += private_data.length;
    }
}
