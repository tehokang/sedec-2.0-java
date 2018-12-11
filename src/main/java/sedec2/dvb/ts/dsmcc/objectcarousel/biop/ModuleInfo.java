package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.dvb.ts.dsmcc.objectcarousel.messages.DescriptorFactory;
import sedec2.util.Logger;

public class ModuleInfo {
    protected int moduleTimeOut;
    protected int blockTimeOut;
    protected int minBlockTime;
    protected byte taps_count;
    protected List<Tap> taps = new ArrayList<>();
    protected byte userInfoLength;
    protected List<Descriptor> descriptors = new ArrayList<>();

    public class Tap {
        public int id;
        public int use;
        public int association_tag;
        public int selector_length;
    }
    public ModuleInfo(BitReadWriter brw) {
        moduleTimeOut = brw.readOnBuffer(32);
        blockTimeOut = brw.readOnBuffer(32);
        minBlockTime = brw.readOnBuffer(32);
        taps_count = (byte) brw.readOnBuffer(8);

        for ( int i=0; i<taps_count; i++ ) {
            Tap tap = new Tap();
            tap.id = brw.readOnBuffer(16);
            tap.use = brw.readOnBuffer(16);
            tap.association_tag = brw.readOnBuffer(16);
            tap.selector_length = (byte) brw.readOnBuffer(8);
            taps.add(tap);
        }

        userInfoLength = (byte) brw.readOnBuffer(8);
        for ( int i=userInfoLength; i>0; ) {
            Descriptor desc = DescriptorFactory.createDescriptor(brw);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }
    }

    public int getModuleTimeOut() {
        return moduleTimeOut;
    }

    public int getBlockTimeOut() {
        return blockTimeOut;
    }

    public int getMinBlockTime() {
        return minBlockTime;
    }

    public byte getTapsCount() {
        return taps_count;
    }

    public List<Tap> getTaps() {
        return taps;
    }

    public byte getUserInfoLength() {
        return userInfoLength;
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    public int getLength() {
        int length = 13;
        for ( int i=0; i<taps.size(); i++ ) {
            length += 7;
        }
        length += 1;

        for ( int i=0; i<descriptors.size(); i++ ) {
            length += descriptors.get(i).getDescriptorLength();
        }

        return length;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t moduleTimeOut : 0x%x \n", moduleTimeOut));
        Logger.d(String.format("\t blockTimeOut : 0x%x \n", blockTimeOut));
        Logger.d(String.format("\t minBlockTime : 0x%x \n", minBlockTime));
        Logger.d(String.format("\t taps_count : 0x%x \n", taps_count));

        for ( int i=0; i<taps.size(); i++ ) {
            Tap tap = taps.get(i);
            Logger.d(String.format("\t [%d] id : 0x%x \n", i, tap.id));
            Logger.d(String.format("\t [%d] use : 0x%x \n", i, tap.use));
            Logger.d(String.format("\t [%d] association_tag : 0x%x \n",
                    i, tap.association_tag));
            Logger.d(String.format("\t [%d] selector_length : 0x%x \n",
                    i, tap.selector_length));
        }

        Logger.d(String.format("\t userInfoLength : 0x%x \n", userInfoLength));
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
