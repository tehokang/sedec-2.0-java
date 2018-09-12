package arib.b39.descriptors;

import java.util.ArrayList;
import java.util.List;

import base.BitReadWriter;
import util.Logger;

public class MPU_TimestampDescriptor extends Descriptor {
    protected List<MPU> mpus = new ArrayList<>();

    class MPU {
        public int mpu_sequence_number;
        public long mpu_presentation_time;
    }

    public MPU_TimestampDescriptor(BitReadWriter brw) {
        super(brw);
        
        for ( int i=descriptor_length; i>0; ) {
            MPU mpu = new MPU();
            mpu.mpu_sequence_number = brw.ReadOnBuffer(32);
            mpu.mpu_presentation_time = brw.ReadOnBuffer(64);
            mpus.add(mpu);
            i-=(4 + 8);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        for ( int i=0; i<mpus.size(); i++ ) {
            MPU mpu = mpus.get(i);
            Logger.d(String.format("\t[%d] mpu_sequence_number : 0x%x \n", 
                    i, mpu.mpu_sequence_number));
            Logger.d(String.format("\t[%d] mpu_sequence_number : 0x%x \n", 
                    i, mpu.mpu_presentation_time));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = mpus.size() * (4 + 8);
    }
}
