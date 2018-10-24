package sedec2.arib.b10.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class RandomizedLatencyDescriptor extends Descriptor {
    private int range;
    private int rate;
    private byte randomization_end_time_flag;
    private long randomization_end_time;
    
    public RandomizedLatencyDescriptor(BitReadWriter brw) {
        super(brw);
        
        range = brw.readOnBuffer(16);
        rate = brw.readOnBuffer(8);
        randomization_end_time_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(7);
        if ( 0 != randomization_end_time_flag ) {
            randomization_end_time = brw.readOnBuffer(40);
        }
    }

    public int getRange() {
        return range;
    }
    
    public int getRate() {
        return rate;
    }
    
    public byte getRandomizationEndTimeFlag() {
        return randomization_end_time_flag;
    }
    
    public long getRandomizationEndTime() {
        return randomization_end_time;
    }
    
    public void setRange(int value) {
        range = value;
    }
    
    public void setRate(int value) {
        rate = value;
    }
    
    public void setRandomizationEndTimeFlag(byte value) {
        randomization_end_time_flag = value;
    }
    
    public void setRandomizationEndTime(long value) {
        randomization_end_time = value;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t range : 0x%x \n", range));
        Logger.d(String.format("\t rate : 0x%x \n", rate));
        Logger.d(String.format("\t randomization_end_time_flag : 0x%x \n",  randomization_end_time_flag));
        if ( 0 != randomization_end_time_flag ) {
            Logger.d(String.format("\t randomization_end_time : 0x%lf \n", randomization_end_time));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + (randomization_end_time_flag == 0 ? 0 : 5 );
    }

    @Override
    public void writeDescriptor(BitReadWriter brw) {
        super.writeDescriptor(brw);
        
        brw.writeOnBuffer(range, 16);
        brw.writeOnBuffer(rate, 8);
        brw.writeOnBuffer(randomization_end_time_flag, 1);
        brw.writeOnBuffer(0x0f, 7);
        if ( 0 != randomization_end_time_flag )
        {
            brw.writeOnBuffer(randomization_end_time, 40);
        }
    }

    
}
