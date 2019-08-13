package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class S2SateliteDeliverySystemDescriptor extends Descriptor {
    protected byte scrambling_sequence_selector;
    protected byte multiple_input_stream_flag;
    protected byte backwards_compatibility_indicator;
    protected int scrambling_sequence_index;
    protected byte input_stream_identifier;

    public S2SateliteDeliverySystemDescriptor(BitReadWriter brw) {
        super(brw);

        scrambling_sequence_selector = (byte) brw.readOnBuffer(1);
        multiple_input_stream_flag = (byte) brw.readOnBuffer(1);
        backwards_compatibility_indicator = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(5);

        if ( scrambling_sequence_selector == 1 ) {
            brw.skipOnBuffer(6);
            scrambling_sequence_index = brw.readOnBuffer(18);
        }

        if ( multiple_input_stream_flag == 1 ) {
            input_stream_identifier = (byte) brw.readOnBuffer(8);
        }
    }

    public byte getScramblingSequenceSelector() {
        return scrambling_sequence_selector;
    }

    public byte getMultipleInputStreamFlag() {
        return multiple_input_stream_flag;
    }

    public byte getBackwardsCompatibilityIndicator() {
        return backwards_compatibility_indicator;
    }

    public int getScramblingSequenceIndex() {
        return scrambling_sequence_index;
    }

    public byte getInputStreamIndentifier() {
        return input_stream_identifier;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t scrambling_sequence_selector : 0x%x \n",
                scrambling_sequence_selector));
        Logger.d(String.format("\t multiple_input_stream_flag : 0x%x \n",
                multiple_input_stream_flag));
        Logger.d(String.format("\t backwards_compatibility_indicator : 0x%x \n",
                backwards_compatibility_indicator));
        Logger.d(String.format("\t scrambling_sequence_index : 0x%x \n",
                scrambling_sequence_index));
        Logger.d(String.format("\t input_stream_identifier : 0x%x \n",
                input_stream_identifier));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;

        if ( scrambling_sequence_selector == 1 ) {
            descriptor_length += 3;
        }

        if ( multiple_input_stream_flag == 1 ) {
            descriptor_length += 1;
        }
    }

}
