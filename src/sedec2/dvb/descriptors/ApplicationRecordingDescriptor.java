package sedec2.dvb.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * @brief ApplicationRecordingDescriptor
 * @note Not Verified Yet
 */
public class ApplicationRecordingDescriptor extends Descriptor {
    private int scheduled_recording_flag;
    private int trick_mode_aware_flag;
    private int time_shift_flag;
    private int dynamic_flag;
    private int av_synced_flag;
    private int initiating_replay_flag;
    
    private byte label_count;
    private int[] label_length = new int[256];
    private byte[][] label_char = new byte[256][256];
    private byte[] storage_properties = new byte[256];
    
    private byte component_tag_list_length;
    private byte[] component_tag = new byte[256];
    
    private byte private_length;
    private byte[] __private__ = new byte[256];
    
    public ApplicationRecordingDescriptor(BitReadWriter brw) {
        super(brw);
        
        scheduled_recording_flag = brw.readOnBuffer(1);
        trick_mode_aware_flag = brw.readOnBuffer(1);
        time_shift_flag = brw.readOnBuffer(1);
        dynamic_flag = brw.readOnBuffer(1);
        av_synced_flag = brw.readOnBuffer(1);
        initiating_replay_flag = brw.readOnBuffer(1);
        brw.skipOnBuffer(2);;
        label_count = (byte) brw.readOnBuffer(8);
        
        for ( int i=0; i<label_count; i++ ) {
            label_length[i] = brw.readOnBuffer(8);
            for ( int j=0; j<label_length[i]; j++) {
                label_char[i][j] = (byte) brw.readOnBuffer(8);
            }
            storage_properties[i] = (byte) brw.readOnBuffer(2);
            brw.skipOnBuffer(6);;
        }
        
        component_tag_list_length = (byte) brw.readOnBuffer(8);
        for ( int i=0; i<component_tag_list_length; i++ ) {
            component_tag[i] = (byte) brw.readOnBuffer(8);
        }
        private_length = (byte) brw.readOnBuffer(8);
        for ( int i=0; i<private_length; i++) {
            __private__[i] = (byte)brw.readOnBuffer(8);
        }
    }
    
    public int getScheduledRecordingFlag() {
        return scheduled_recording_flag;
    }
    
    public int getTrickModeAwareFlag() {
        return trick_mode_aware_flag;
    }
    
    public int getTimeShiftFlag() {
        return time_shift_flag;
    }
    
    public int getDynamicFlag() {
        return dynamic_flag;
    }

    public int getAVSyncedFlag() {
        return av_synced_flag;
    }
    
    public int getInitiatingReplayFlag() {
        return initiating_replay_flag;
    }
    
    public void setScheduledRecordingFlag(int value) {
        scheduled_recording_flag = value;
    }
    
    public void setTrickModeAwareFlag(int value) {
        trick_mode_aware_flag = value;
    }
    
    public void setTimeShiftFlag(int value) {
        time_shift_flag = value;
    }
    
    public void setDynamicFlag(int value) {
        dynamic_flag = value;
    }
    
    public void setAVSyncedFlag(int value) {
        av_synced_flag = value;
    }
    
    public void setInitiatingReplayFlag(int value) {
        initiating_replay_flag = value;
    }
    
    @Override
    protected void updateDescriptorLength() {
        byte temp_length = 0;
        for(int i=0;i<label_count;i++) {
            temp_length +=(2+label_length[i]);
        }
        temp_length += component_tag_list_length;
        temp_length += private_length;
        descriptor_length = 4 + temp_length;
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);
        
        brw.writeOnBuffer(scheduled_recording_flag, 1);
        brw.writeOnBuffer(trick_mode_aware_flag, 1);
        brw.writeOnBuffer(time_shift_flag, 1);
        brw.writeOnBuffer(dynamic_flag, 1);
        brw.writeOnBuffer(av_synced_flag, 1);
        brw.writeOnBuffer(initiating_replay_flag, 1);
        brw.writeOnBuffer(0x0, 2);
        brw.writeOnBuffer(label_count, 8);

        for(int i=0; i<label_count;i++) {
            brw.writeOnBuffer(label_length[i], 8);
            for(int j=0;j<label_length[i];j++) {
                brw.writeOnBuffer(label_char[i][j], 8);
            }
            brw.writeOnBuffer(storage_properties[i], 2);
            brw.writeOnBuffer(0x0, 6);
        }
        brw.writeOnBuffer(component_tag_list_length, 8);
        for(int i=0; i<component_tag_list_length;i++) {
            brw.writeOnBuffer(component_tag[i], 8);
        }
        brw.writeOnBuffer(private_length, 8);
        for(int i=0;i<private_length;i++) {
            brw.writeOnBuffer(__private__[i], 8);
        }
    }

    @Override
    public void print() {
       
        super._print_();

        Logger.d(String.format("\t scheduled_recording_flag : 0x%x \n", 
                scheduled_recording_flag));
        Logger.d(String.format("\t trick_mode_aware_flag : 0x%x \n", 
                trick_mode_aware_flag));
        Logger.d(String.format("\t time_shift_flag : 0x%x \n", 
                time_shift_flag));
        Logger.d(String.format("\t dynamic_flag : 0x%x \n", 
                dynamic_flag));
        Logger.d(String.format("\t av_synced_flag : 0x%x \n", 
                av_synced_flag));
        Logger.d(String.format("\t initiating_replay_flag : 0x%x \n", 
                initiating_replay_flag));
        Logger.d(String.format("\t label_count : 0x%x \n", 
                label_count));
        for(int i=0; i<label_count;i++) {
            Logger.d(String.format("\t   label_length[%d] : 0x%x \n", 
                    label_length[i]));
            Logger.d(String.format("\t   label_char : %s \n", 
                    new String(label_char[i])));
            Logger.d(String.format("\t   storage_properties[%d] : 0x%x \n", 
                    storage_properties[i]));
        }

        Logger.d(String.format("\t component_tag_list_length : 0x%x \n", 
                component_tag_list_length));
        for(int i=0; i<component_tag_list_length;i++) {
            Logger.d(String.format("\t component_tag : 0x%x \n", component_tag[i]));
        }
    }
    
    
    
    
    
    
    
    
    
    
    
}
