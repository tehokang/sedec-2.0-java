package sedec2.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MPU_TimestampDescriptor extends Descriptor {
    protected List<MPU> mpus = new ArrayList<>();

    public class MPU {
        public int mpu_sequence_number;
        public long mpu_presentation_time;
    }

    public MPU_TimestampDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            MPU mpu = new MPU();
            mpu.mpu_sequence_number = brw.readOnBuffer(32);
            mpu.mpu_presentation_time = brw.readOnBuffer(64);
            mpus.add(mpu);
            i-=(4 + 8);
        }
    }

    public List<MPU> getMpus() {
        return mpus;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<mpus.size(); i++ ) {
            MPU mpu = mpus.get(i);
            Logger.d(String.format("\t [%d] mpu_sequence_number : 0x%04x \n",
                    i, mpu.mpu_sequence_number));
            Logger.d(String.format("\t [%d] mpu_presentation_time : 0x%08x \n",
                    i, mpu.mpu_presentation_time));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = mpus.size() * (4 + 8);
    }
}
