package sedec2.arib.b39.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class SateliteDeliverySystemDescriptor extends Descriptor {
    protected int frequency;
    protected int orbital_position;
    protected byte west_east_flag;
    protected byte polarisation;
    protected byte modulation;
    protected int symbol_rate;
    protected byte FEC_inner;
    
    public SateliteDeliverySystemDescriptor(BitReadWriter brw) {
        super();
        
        descriptor_tag = (byte) brw.ReadOnBuffer(8);
        descriptor_length = (byte) brw.ReadOnBuffer(8);
        
        frequency = brw.ReadOnBuffer(32);
        orbital_position = brw.ReadOnBuffer(16);
        west_east_flag = (byte) brw.ReadOnBuffer(1);
        polarisation = (byte) brw.ReadOnBuffer(2);
        modulation = (byte) brw.ReadOnBuffer(5);
        symbol_rate = (byte) brw.ReadOnBuffer(28);
        FEC_inner = (byte) brw.ReadOnBuffer(4);
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t frequency : 0x%x \n", frequency));
        Logger.d(String.format("\t orbital_position : 0x%x \n", orbital_position));
        Logger.d(String.format("\t west_east_flag : 0x%x \n", west_east_flag));
        Logger.d(String.format("\t polarisation : 0x%x \n", polarisation));
        Logger.d(String.format("\t modulation : 0x%x \n", modulation));
        Logger.d(String.format("\t symbol_rate : 0x%x \n", symbol_rate));
        Logger.d(String.format("\t FEC_inner : 0x%x \n", FEC_inner));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 11;
    }

}
