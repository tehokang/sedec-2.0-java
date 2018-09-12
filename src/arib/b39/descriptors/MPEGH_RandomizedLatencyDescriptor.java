package arib.b39.descriptors;

import base.BitReadWriter;
import util.Logger;

public class MPEGH_RandomizedLatencyDescriptor extends Descriptor {
    private int range;
    private int rate;
    private byte randomization_end_time_flag;
    private long randomization_end_time;
    
    public MPEGH_RandomizedLatencyDescriptor(BitReadWriter brw) {
        super(brw);
        
        range = brw.ReadOnBuffer(16);
        rate = brw.ReadOnBuffer(8);
        randomization_end_time_flag = (byte) brw.ReadOnBuffer(1);
        brw.SkipOnBuffer(7);
        if ( 0 != randomization_end_time_flag ) {
            randomization_end_time = brw.ReadOnBuffer(40);
        }
    }

    public int GetRange() {
        return range;
    }
    
    public int GetRate() {
        return rate;
    }
    
    public byte GetRandomizationEndTimeFlag() {
        return randomization_end_time_flag;
    }
    
    public long GetRandomizationEndTime() {
        return randomization_end_time;
    }
    
    public void SetRange(int value) {
        range = value;
    }
    
    public void SetRate(int value) {
        rate = value;
    }
    
    public void SetRandomizationEndTimeFlag(byte value) {
        randomization_end_time_flag = value;
    }
    
    public void SetRandomizationEndTime(long value) {
        randomization_end_time = value;
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t range : 0x%x \n", range));
        Logger.d(String.format("\t rate : 0x%x \n", rate));
        Logger.d(String.format("\t randomization_end_time_flag : 0x%x \n",  randomization_end_time_flag));
        if ( 0 != randomization_end_time_flag ) {
            Logger.d(String.format("\t randomization_end_time : 0x%lf \n", randomization_end_time));
        }
        Logger.d("\n");
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + (randomization_end_time_flag == 0 ? 0 : 5 );
    }

    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        brw.WriteOnBuffer(range, 16);
        brw.WriteOnBuffer(rate, 8);
        brw.WriteOnBuffer(randomization_end_time_flag, 1);
        brw.WriteOnBuffer(0x0f, 7);
        if ( 0 != randomization_end_time_flag )
        {
            brw.WriteOnBuffer(randomization_end_time, 40);
        }
    }

    
}
