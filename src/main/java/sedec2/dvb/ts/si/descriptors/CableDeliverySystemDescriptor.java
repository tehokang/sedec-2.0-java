package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class CableDeliverySystemDescriptor extends Descriptor {
    protected int frequency;
    protected byte FEC_outer;
    protected byte modulation;
    protected int symbol_rate;
    protected byte FEC_inner;

    public CableDeliverySystemDescriptor(BitReadWriter brw) {
        super(brw);

        frequency = brw.readOnBuffer(32);
        brw.skipOnBuffer(12);
        FEC_outer = (byte) brw.readOnBuffer(4);
        modulation = (byte) brw.readOnBuffer(8);
        symbol_rate = brw.readOnBuffer(28);
        FEC_inner = (byte) brw.readOnBuffer(4);
    }

    public int getFrequency() {
        return frequency;
    }

    public byte getFECOuter() {
        return FEC_outer;
    }

    public byte getModulation() {
        return modulation;
    }

    public int getSymbolRate() {
        return symbol_rate;
    }

    public byte getFECInner() {
        return FEC_inner;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t frequency : 0x%x \n", frequency));
        Logger.d(String.format("\t FEC_outer : 0x%x \n", FEC_outer));
        Logger.d(String.format("\t modulation : 0x%x \n", modulation));
        Logger.d(String.format("\t symbol_rate : 0x%x \n", symbol_rate));
        Logger.d(String.format("\t FEC_inner : 0x%x \n", FEC_inner));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 11;
    }

}
