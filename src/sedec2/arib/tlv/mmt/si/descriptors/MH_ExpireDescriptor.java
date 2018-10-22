package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_ExpireDescriptor extends Descriptor {
    private int time_mode;
    private long UTC_time;
    private int passed_seconds;
    
    public MH_ExpireDescriptor(BitReadWriter brw) {
        super(brw);
        
        time_mode = brw.ReadOnBuffer(8);
        
        if ( 0x01 == time_mode ) {
            UTC_time = brw.ReadOnBuffer(64);
        } else if ( 0x04 == time_mode ) {
            brw.SkipOnBuffer(8);
            passed_seconds = brw.ReadOnBuffer(32);
        }
    }

    public int GetTimeMode() {
        return time_mode;
    }
    
    public double GetUTCTime() {
        return UTC_time;
    }
    
    public int GetPassedSeconds() {
        return passed_seconds;
    }
    
    public void SetTimeMode(int value) {
        time_mode =  value;
    }
    
    public void SetUTCTime(long value) {
        UTC_time = value;
    }
    
    public void SetPassedSeconds(int value) {
        passed_seconds = value;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t time_mode : 0x%x \n", time_mode));
        if ( 0x01 == time_mode ) {
            Logger.d(String.format("\t UTC_time : %lf \n", UTC_time));
        } else if ( 0x04 == time_mode ) {
            Logger.d(String.format("\t passed_seconds : 0x%x (%d) \n", 
                    passed_seconds, passed_seconds));
        }
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        if ( 0x01 == time_mode ) {
            descriptor_length = 9;
        } else if ( 0x04 == time_mode ) {
            descriptor_length = 6;
        }
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if ( 0 < descriptor_length ) {
            brw.WriteOnBuffer(time_mode, 8);
            
            if ( 0x01 == time_mode ) {
                brw.WriteOnBuffer(UTC_time, 64);
            } else if ( 0x04 == time_mode ) {
                brw.WriteOnBuffer(0x0f, 8);
                brw.WriteOnBuffer(passed_seconds, 32);
            }
        }
    }
}
