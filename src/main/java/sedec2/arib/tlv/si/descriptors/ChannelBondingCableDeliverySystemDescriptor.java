package sedec2.arib.tlv.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ChannelBondingCableDeliverySystemDescriptor extends Descriptor {
    protected List<ChannelBondingInfo> channel_bonding_infos = new ArrayList<>();

    class ChannelBondingInfo {
        public int frequency;
        public byte frame_type;
        public byte FEC_outer;
        public byte modulation;
        public int symbol_rate;
        public byte FEC_inner;
        public byte group_id;
    }

    public ChannelBondingCableDeliverySystemDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            ChannelBondingInfo ch = new ChannelBondingInfo();
            ch.frequency = brw.readOnBuffer(32);
            brw.skipOnBuffer(8);
            ch.frame_type = (byte) brw.readOnBuffer(4);
            ch.FEC_outer = (byte) brw.readOnBuffer(4);
            ch.modulation = (byte) brw.readOnBuffer(8);
            ch.symbol_rate = brw.readOnBuffer(28);
            ch.FEC_inner = (byte) brw.readOnBuffer(4);
            ch.group_id = (byte) brw.readOnBuffer(8);

            i-=12;
            channel_bonding_infos.add(ch);
        }
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<channel_bonding_infos.size(); i++ ) {
            ChannelBondingInfo ch = channel_bonding_infos.get(i);
            Logger.d(String.format("\t [%d] frequency : 0x%x \n", i, ch.frequency));
            Logger.d(String.format("\t [%d] frame_type : 0x%x \n", i, ch.frame_type));
            Logger.d(String.format("\t [%d] FEC_outer : 0x%x \n", i, ch.FEC_outer));
            Logger.d(String.format("\t [%d] modulation : 0x%x \n", i, ch.modulation));
            Logger.d(String.format("\t [%d] symbol_rate : 0x%x \n", i, ch.symbol_rate));
            Logger.d(String.format("\t [%d] FEC_inner : 0x%x \n", i, ch.FEC_inner));
            Logger.d(String.format("\t [%d] group_id : 0x%x \n", i, ch.group_id));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = channel_bonding_infos.size() * 12;
    }
}
