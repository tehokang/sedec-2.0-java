package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class TerrestrialDeliverySystemDescriptor extends Descriptor {
    protected int centre_frequency;
    protected byte bandwidth;
    protected byte priority;
    protected byte Time_Slicing_indicator;
    protected byte MPE_FEC_indicator;
    protected byte constellation;
    protected byte hierachy_information;
    protected byte code_rate_HP_stream;
    protected byte code_rate_LP_stream;
    protected byte guard_interval;
    protected byte transmission_code;
    protected byte other_frquency_flag;

    public TerrestrialDeliverySystemDescriptor(BitReadWriter brw) {
        super(brw);

        centre_frequency = brw.readOnBuffer(32);
        bandwidth = (byte) brw.readOnBuffer(3);
        priority = (byte) brw.readOnBuffer(1);
        Time_Slicing_indicator = (byte) brw.readOnBuffer(1);
        MPE_FEC_indicator = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(2);
        constellation = (byte) brw.readOnBuffer(2);
        hierachy_information = (byte) brw.readOnBuffer(3);
        code_rate_HP_stream = (byte) brw.readOnBuffer(3);
        code_rate_LP_stream = (byte) brw.readOnBuffer(3);
        guard_interval = (byte) brw.readOnBuffer(2);
        transmission_code = (byte) brw.readOnBuffer(2);
        other_frquency_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(32);
    }

    public int getCentreFrequency() {
        return centre_frequency;
    }

    public byte getBandwidth() {
        return bandwidth;
    }

    public byte getPriority() {
        return priority;
    }

    public byte getTimeSlicingIndicator() {
        return Time_Slicing_indicator;
    }

    public byte getMPEFECIndicator() {
        return MPE_FEC_indicator;
    }

    public byte getConstellation() {
        return constellation;
    }

    public byte getHierachyInformation() {
        return hierachy_information;
    }

    public byte getCodeRateHpStream() {
        return code_rate_HP_stream;
    }

    public byte getCodeRateLpStream() {
        return code_rate_LP_stream;
    }

    public byte getGuardInterval() {
        return guard_interval;
    }

    public byte getTransmissionCode() {
        return transmission_code;
    }

    public byte getOtherFrquencyFlag() {
        return other_frquency_flag;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t centre_frequency : 0x%x \n", centre_frequency));
        Logger.d(String.format("\t bandwidth : 0x%x \n", bandwidth));
        Logger.d(String.format("\t priority : 0x%x \n", priority));
        Logger.d(String.format("\t Time_Slicing_indicator : 0x%x \n", Time_Slicing_indicator));
        Logger.d(String.format("\t MPE_FEC_indicator : 0x%x \n", MPE_FEC_indicator));
        Logger.d(String.format("\t constellation : 0x%x \n", constellation));
        Logger.d(String.format("\t hierachy_information : 0x%x \n", hierachy_information));
        Logger.d(String.format("\t code_rate_HP_stream : 0x%x \n", code_rate_HP_stream));
        Logger.d(String.format("\t code_rate_LP_stream : 0x%x \n", code_rate_LP_stream));
        Logger.d(String.format("\t guard_interval : 0x%x \n", guard_interval));
        Logger.d(String.format("\t transmission_code : 0x%x \n", transmission_code));
        Logger.d(String.format("\t other_frquency_flag : 0x%x \n", other_frquency_flag));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + 3 + 4;
    }
}
