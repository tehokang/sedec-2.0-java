package sedec2.arib.tlv.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class PUStructureDescriptor extends Descriptor {
    protected byte num_of_MPU;
    protected int[] MPU_sequence_number;
    
    public PUStructureDescriptor(BitReadWriter brw) {
        super(brw);
        
        num_of_MPU = (byte) brw.readOnBuffer(8);
        MPU_sequence_number = new int[num_of_MPU];
        for ( int i=0; i<num_of_MPU; i++ ) {
            MPU_sequence_number[i] = brw.readOnBuffer(32);
        }
    }

    public byte getNumOfMPU() {
        return num_of_MPU;
    }
    
    public int[] getMPUSequenceNumber() {
        return MPU_sequence_number;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t num_of_MPU : %d \n", num_of_MPU));
        
        for ( int i=0; i<MPU_sequence_number.length; i++ ) {
            Logger.d(String.format("\t [%d] MPU_sequence_number : 0x%x \n", 
                    i, MPU_sequence_number[i]));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1 + (num_of_MPU * 4);
    }
}
