package sedec2.arib.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class PUStructureDescriptor extends Descriptor {
    protected byte num_of_MPU;
    protected int[] MPU_sequence_number;
    
    public PUStructureDescriptor(BitReadWriter brw) {
        super(brw);
        
        num_of_MPU = (byte) brw.ReadOnBuffer(8);
        MPU_sequence_number = new int[num_of_MPU];
        for ( int i=0; i<num_of_MPU; i++ ) {
            MPU_sequence_number[i] = brw.ReadOnBuffer(32);
        }
    }
    
    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("num_of_MPU : %d \n", num_of_MPU));
        
        for ( int i=0; i<MPU_sequence_number.length; i++ ) {
            Logger.d(String.format("[%d] MPU_sequence_number : 0x%x \n", 
                    i, MPU_sequence_number[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + (num_of_MPU * 4);
    }
}
