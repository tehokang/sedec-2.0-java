package arib.b10.descriptors;

import java.util.ArrayList;
import java.util.List;

import base.BitReadWriter;
import util.Logger;

public class DigitalCopyControlDescriptor extends Descriptor {
    protected byte digital_recording_control_data;
    protected byte maximum_bitrate_flag;
    protected byte component_control_flag;
    protected byte user_defined;
    protected byte maximum_bitrate;
    protected byte component_control_length;
    protected List<ComponentControl> component_controls = new ArrayList<>();
    
    class ComponentControl {
        public byte component_tag;
        public byte digital_recording_control_data;
        public byte maximum_bitrate_flag;
        public byte user_defined;
        public byte maximum_bitrate;
    }
    
    
    public DigitalCopyControlDescriptor(BitReadWriter brw) {
        super(brw);
        
        digital_recording_control_data = (byte) brw.ReadOnBuffer(2);
        maximum_bitrate_flag = (byte) brw.ReadOnBuffer(1);
        component_control_flag = (byte) brw.ReadOnBuffer(1);
        user_defined = (byte) brw.ReadOnBuffer(4);
        
        if ( maximum_bitrate_flag == 1 ) {
            maximum_bitrate = (byte) brw.ReadOnBuffer(8);
        }
        
        if ( component_control_flag == 1) {
            component_control_length = (byte) brw.ReadOnBuffer(8);
            
            for ( int i=0; i<component_control_length; i++ ) {
                ComponentControl component_control = new ComponentControl();
                component_control.component_tag = (byte) brw.ReadOnBuffer(8);
                component_control.digital_recording_control_data = (byte) brw.ReadOnBuffer(2);
                component_control.maximum_bitrate_flag = (byte) brw.ReadOnBuffer(1);
                brw.SkipOnBuffer(1);
                component_control.user_defined = (byte) brw.ReadOnBuffer(4);
                
                if ( component_control.maximum_bitrate_flag == 1 ) {
                    component_control.maximum_bitrate = (byte) brw.ReadOnBuffer(8);
                }
                component_controls.add(component_control);
            }
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t digital_recording_control_data : 0x%x \n", 
                digital_recording_control_data));
        Logger.d(String.format("\t maximum_bitrate_flag : 0x%x \n", maximum_bitrate_flag));
        Logger.d(String.format("\t component_control_flag : 0x%x \n", component_control_flag));
        Logger.d(String.format("\t user_defined : 0x%x \n", user_defined));
        
        if ( maximum_bitrate_flag == 1) {
            Logger.d(String.format("\t maximum_bitrate : 0x%x \n", maximum_bitrate));
        }
        
        if ( component_control_flag == 1 ) {
            Logger.d(String.format("\t component_control_length : 0x%x \n", 
                    component_control_length));
            
            for ( int i=0; i<component_controls.size(); i++ ) {
                ComponentControl component_control = component_controls.get(i);
                Logger.d(String.format("\t [%d] component_tag : 0x%x \n",  
                        i, component_control.component_tag));
                Logger.d(String.format("\t [%d] digital_recording_control_data : 0x%x \n", 
                        i, component_control.digital_recording_control_data));
                Logger.d(String.format("\t [%d] maximum_bitrate_flag : 0x%x \n", 
                        i, component_control.maximum_bitrate_flag));
                Logger.d(String.format("\t [%d] user_defined : 0x%x \n", 
                        i, component_control.user_defined));
                
                if ( component_control.maximum_bitrate_flag == 1 ) {
                    Logger.d(String.format("\t [%d] maximum_bitrate : 0x%x \n", 
                            i, component_control.maximum_bitrate));
                }
            }
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
        
        if ( maximum_bitrate_flag == 1 ) {
            descriptor_length += 1;
        }
        
        if ( component_control_flag == 1 ) {
            descriptor_length += 1;
            for ( int i=0; i<component_controls.size(); i++ ) {
                descriptor_length += 2;
                
                if ( component_controls.get(i).maximum_bitrate_flag == 1 ) {
                    descriptor_length +=1;
                }
            }
        }
    }
}
