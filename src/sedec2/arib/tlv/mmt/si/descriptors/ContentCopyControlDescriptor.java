package sedec2.arib.tlv.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ContentCopyControlDescriptor extends Descriptor {
    protected byte digital_recording_control_data;
    protected byte maximum_bit_rate_flag;
    protected byte component_control_flag;
    protected byte maximum_bitrate;
    protected byte component_control_length;
    protected List<ComponentControl> component_controls = new ArrayList<>();
    
    public class ComponentControl {
        public int component_tag;
        public byte digital_recording_control_data;
        public byte maximum_bitrate_flag;
        public byte maximum_bitrate;
    }
    
    public ContentCopyControlDescriptor(BitReadWriter brw) {
        super(brw);
        
        digital_recording_control_data = (byte) brw.readOnBuffer(2);
        maximum_bit_rate_flag = (byte) brw.readOnBuffer(1);
        component_control_flag = (byte) brw.readOnBuffer(1);
        
        brw.skipOnBuffer(4);
        brw.skipOnBuffer(8);
        
        if ( maximum_bit_rate_flag == 1 ) {
            maximum_bitrate = (byte) brw.readOnBuffer(8);
        }
        
        if ( component_control_flag == 1 ) {
            component_control_length = (byte) brw.readOnBuffer(8);
            
            for ( int i=component_control_length; i>0; ) {
                ComponentControl cc = new ComponentControl();
                cc.component_tag = brw.readOnBuffer(16);
                cc.digital_recording_control_data = (byte) brw.readOnBuffer(2);
                cc.maximum_bitrate_flag = (byte) brw.readOnBuffer(1);
                brw.skipOnBuffer(5);
                brw.skipOnBuffer(8);
                
                i-=4;
                if ( cc.maximum_bitrate_flag == 1) {
                    cc.maximum_bitrate = (byte) brw.readOnBuffer(8);
                    i-=1;
                }
                component_controls.add(cc);
            }
        }
    }
    
    public byte getDigitalRecordingControlData() {
        return digital_recording_control_data;
    }
    
    public byte getMaximumBitrateFlag() {
        return maximum_bit_rate_flag;
    }
    
    public byte getComponentControlFlag() {
        return component_control_flag;
    }
    
    public byte getMaximumBitrate() {
        return maximum_bitrate;
    }
    
    public byte getComponentControlLength() {
        return component_control_length;
    }
    
    public List<ComponentControl> getComponentControls() {
        return component_controls;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t digital_recording_control_data : 0x%x \n", 
                digital_recording_control_data));
        Logger.d(String.format("\t maximum_bit_rate_flag : 0x%x \n", maximum_bit_rate_flag));
        Logger.d(String.format("\t component_control_flag : 0x%x \n", component_control_flag));
        
        if ( maximum_bit_rate_flag == 1) {
            Logger.d(String.format("\t maximum_bitrate : 0x%x \n", maximum_bitrate));
        }
        
        if ( component_control_flag == 1 ) {
            Logger.d(String.format("\t component_control_length : 0x%x \n", 
                    component_control_length));
            
            for ( int i=0; i<component_controls.size(); i++ ) {
                ComponentControl cc = component_controls.get(i);
                Logger.d(String.format("\t [%d] component_tag : 0x%x \n", 
                        i, cc.component_tag));
                Logger.d(String.format("\t [%d] digital_recording_control_data : 0x%x \n",
                        i, cc.digital_recording_control_data));
                Logger.d(String.format("\t [%d] maximum_bitrate_flag : 0x%x \n", 
                        i, cc.maximum_bitrate_flag));
                
                if ( cc.maximum_bitrate_flag == 1) {
                    Logger.d(String.format("\t [%d] maximum_bitrate : 0x%x \n",
                            i, cc.maximum_bitrate));
                }
            }
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2;
        
        if ( maximum_bit_rate_flag == 1 ) {
            descriptor_length += 1;
        }
        
        if ( component_control_flag == 1) {
            descriptor_length += 1;
            for ( int i=0; i<component_controls.size(); i++ ) {
                ComponentControl cc = component_controls.get(i);
                descriptor_length += 4;
                
                if ( cc.maximum_bitrate_flag == 1 ) {
                    descriptor_length += 1;
                }
            }
        }
    }
}
