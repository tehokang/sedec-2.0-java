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
        
        scheduled_recording_flag = brw.ReadOnBuffer(1);
        trick_mode_aware_flag = brw.ReadOnBuffer(1);
        time_shift_flag = brw.ReadOnBuffer(1);
        dynamic_flag = brw.ReadOnBuffer(1);
        av_synced_flag = brw.ReadOnBuffer(1);
        initiating_replay_flag = brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(2);;
        label_count = (byte) brw.ReadOnBuffer(8);
        
        for ( int i=0; i<label_count; i++ ) {
            label_length[i] = brw.ReadOnBuffer(8);
            for ( int j=0; j<label_length[i]; j++) {
                label_char[i][j] = (byte) brw.ReadOnBuffer(8);
            }
            storage_properties[i] = (byte) brw.ReadOnBuffer(2);
            brw.SkipOnBuffer(6);;
        }
        
        component_tag_list_length = (byte) brw.ReadOnBuffer(8);
        for ( int i=0; i<component_tag_list_length; i++ ) {
            component_tag[i] = (byte) brw.ReadOnBuffer(8);
        }
        private_length = (byte) brw.ReadOnBuffer(8);
        for ( int i=0; i<private_length; i++) {
            __private__[i] = (byte)brw.ReadOnBuffer(8);
        }
    }
    
    public int GetScheduledRecordingFlag() {
        return scheduled_recording_flag;
    }
    
    public int GetTrickModeAwareFlag() {
        return trick_mode_aware_flag;
    }
    
    public int GetTimeShiftFlag() {
        return time_shift_flag;
    }
    
    public int GetDynamicFlag() {
        return dynamic_flag;
    }

    public int GetAVSyncedFlag() {
        return av_synced_flag;
    }
    
    public int GetInitiatingReplayFlag() {
        return initiating_replay_flag;
    }
    
    public void SetScheduledRecordingFlag(int value) {
        scheduled_recording_flag = value;
    }
    
    public void SetTrickModeAwareFlag(int value) {
        trick_mode_aware_flag = value;
    }
    
    public void SetTimeShiftFlag(int value) {
        time_shift_flag = value;
    }
    
    public void SetDynamicFlag(int value) {
        dynamic_flag = value;
    }
    
    public void SetAVSyncedFlag(int value) {
        av_synced_flag = value;
    }
    
    public void SetInitiatingReplayFlag(int value) {
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
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(scheduled_recording_flag, 1);
        brw.WriteOnBuffer(trick_mode_aware_flag, 1);
        brw.WriteOnBuffer(time_shift_flag, 1);
        brw.WriteOnBuffer(dynamic_flag, 1);
        brw.WriteOnBuffer(av_synced_flag, 1);
        brw.WriteOnBuffer(initiating_replay_flag, 1);
        brw.WriteOnBuffer(0x0, 2);
        brw.WriteOnBuffer(label_count, 8);

        for(int i=0; i<label_count;i++) {
            brw.WriteOnBuffer(label_length[i], 8);
            for(int j=0;j<label_length[i];j++) {
                brw.WriteOnBuffer(label_char[i][j], 8);
            }
            brw.WriteOnBuffer(storage_properties[i], 2);
            brw.WriteOnBuffer(0x0, 6);
        }
        brw.WriteOnBuffer(component_tag_list_length, 8);
        for(int i=0; i<component_tag_list_length;i++) {
            brw.WriteOnBuffer(component_tag[i], 8);
        }
        brw.WriteOnBuffer(private_length, 8);
        for(int i=0;i<private_length;i++) {
            brw.WriteOnBuffer(__private__[i], 8);
        }
    }

    @Override
    public void PrintDescriptor() {
       
        super._PrintDescriptorHeader_();

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
