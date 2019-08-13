package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class SateliteDeliverySystemDescriptor extends Descriptor {
    protected int frequency;
    protected int orbital_position;
    protected byte west_east_flag;
    protected byte polarization;
    protected byte roll_off;
    protected byte modulation_system;
    protected byte modulation_type;
    protected int symbol_rate;
    protected byte FEC_inner;

    public SateliteDeliverySystemDescriptor(BitReadWriter brw) {
        super(brw);

        frequency = brw.readOnBuffer(32);
        orbital_position = brw.readOnBuffer(16);
        west_east_flag = (byte) brw.readOnBuffer(1);
        polarization = (byte) brw.readOnBuffer(2);
        roll_off = (byte) brw.readOnBuffer(2);
        modulation_system = (byte) brw.readOnBuffer(1);
        modulation_type = (byte) brw.readOnBuffer(2);
        symbol_rate = (byte) brw.readOnBuffer(28);
        FEC_inner = (byte) brw.readOnBuffer(4);
    }

    public int getFrequency() {
        return frequency;
    }

    public int getOrbitalPosition() {
        return orbital_position;
    }

    public byte getWestEastFlag() {
        return west_east_flag;
    }

    public byte getPolarization() {
        return polarization;
    }

    public byte getRollOff() {
        return roll_off;
    }

    public byte getModulationSystem() {
        return modulation_system;
    }

    public byte getModulationType() {
        return modulation_type;
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

        Logger.d(String.format("\t frequency : 0x%x \n",  frequency));
        Logger.d(String.format("\t orbital_position : 0x%x \n", orbital_position));
        Logger.d(String.format("\t west_east_flag : 0x%x \n", west_east_flag));
        Logger.d(String.format("\t polarization : 0x%x \n", polarization));
        Logger.d(String.format("\t roll_off : 0x%x \n", roll_off));
        Logger.d(String.format("\t modulation_system : 0x%x \n", modulation_system));
        Logger.d(String.format("\t modulation_type : 0x%x \n", modulation_type));
        Logger.d(String.format("\t symbol_rate : 0x%x \n", symbol_rate));
        Logger.d(String.format("\t FEC_inner : 0x%x \n", FEC_inner));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 11;
    }

}
