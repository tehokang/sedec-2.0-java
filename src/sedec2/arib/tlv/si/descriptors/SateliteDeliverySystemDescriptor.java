package sedec2.arib.tlv.si.descriptors;

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
        super(brw);
        
        frequency = brw.readOnBuffer(32);
        orbital_position = brw.readOnBuffer(16);
        west_east_flag = (byte) brw.readOnBuffer(1);
        polarisation = (byte) brw.readOnBuffer(2);
        modulation = (byte) brw.readOnBuffer(5);
        symbol_rate = (byte) brw.readOnBuffer(28);
        FEC_inner = (byte) brw.readOnBuffer(4);
    }

    @Override
    public void print() {
        super._print_();
        
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
