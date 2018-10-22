package sedec2.arib.tlv.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class EmergencyInformationDescriptor extends Descriptor {
    List<EmergencyInfo> emergency_infos = new ArrayList<>();
    
    class EmergencyInfo {
        public int service_id;
        public byte start_end_flag;
        public byte signal_level;
        public byte area_code_length;
        public int[] area_code;
    }
    
    public EmergencyInformationDescriptor(BitReadWriter brw) {
        super(brw);
        
        for ( int i=descriptor_length; i>0; ) {
            EmergencyInfo emergency = new EmergencyInfo();
            emergency.service_id = brw.ReadOnBuffer(16);
            emergency.start_end_flag = (byte) brw.ReadOnBuffer(1);
            emergency.signal_level = (byte) brw.ReadOnBuffer(1);
            brw.SkipOnBuffer(6);
            emergency.area_code_length = (byte) brw.ReadOnBuffer(8);
            emergency.area_code = new int[emergency.area_code_length];
            
            for ( int j=0; j<emergency.area_code_length; j++ ) {
                emergency.area_code[j] = brw.ReadOnBuffer(12);
                brw.SkipOnBuffer(4);
            }
            
            emergency_infos.add(emergency);
            i-=(4 + emergency.area_code_length);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        for ( int i=0; i<emergency_infos.size(); i++ ) {
            EmergencyInfo emergency = emergency_infos.get(i);
            
            Logger.d(String.format("\t [%d] service_id : 0x%x \n", 
                    i, emergency.service_id));
            Logger.d(String.format("\t [%d] start_end_flag : 0x%x \n", 
                    i, emergency.start_end_flag));
            Logger.d(String.format("\t [%d] signal_level : 0x%x \n", 
                    i, emergency.signal_level));
            Logger.d(String.format("\t [%d] area_code_length : 0x%x \n", 
                    i, emergency.area_code_length));
            
            Logger.d(String.format("\t [%d] area_code : ", i));
            for ( int j=0; j<emergency.area_code_length; j++ ) {
                Logger.p(String.format("%x ", emergency.area_code[j]));
            }
            Logger.d("\n");
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 0;
        for ( int i=0; i<emergency_infos.size(); i++ ) {
            descriptor_length +=(4 + emergency_infos.get(i).area_code_length);
        }
    }
}
