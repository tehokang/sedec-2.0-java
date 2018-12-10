package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class ConnBinder {
    public int componentId_tag;
    public byte component_data_length;
    public byte taps_count;
    public Tap1 tap1;
    public List<TapN> tap_others = new ArrayList<>();

    public class Tap1 {
        public int id;
        public int use;
        public int association_tag;
        public byte selector_length;
        public int selector_type;
        public int transactionId;
        public int timeout;
    }

    public class TapN {
        public int id;
        public int use;
        public int association_tag;
        public byte selector_length;
        public byte[] selector_data_byte;
    }

    public ConnBinder(BitReadWriter brw) {
        componentId_tag = brw.readOnBuffer(32);
        component_data_length = (byte) brw.readOnBuffer(8);
        taps_count = (byte) brw.readOnBuffer(8);

        tap1 = new Tap1();
        tap1.id = brw.readOnBuffer(16);
        tap1.use = brw.readOnBuffer(16);
        tap1.association_tag = brw.readOnBuffer(16);
        tap1.selector_length = (byte) brw.readOnBuffer(8);
        tap1.selector_type = brw.readOnBuffer(16);
        tap1.transactionId = brw.readOnBuffer(32);
        tap1.timeout = brw.readOnBuffer(32);

        for ( int i=0; i<taps_count-1; i++ ) {
            TapN tap = new TapN();
            tap.id = brw.readOnBuffer(16);
            tap.use = brw.readOnBuffer(16);
            tap.association_tag = brw.readOnBuffer(16);
            tap.selector_length = (byte) brw.readOnBuffer(8);
            tap.selector_data_byte = new byte[tap.selector_length];
            for ( int k=0; k<tap.selector_data_byte.length; k++ ) {
                tap.selector_data_byte[k] = (byte) brw.readOnBuffer(8);
            }
            tap_others.add(tap);
        }
    }

    public int getComponentIdTag() {
        return componentId_tag;
    }

    public byte getComponentDataLength() {
        return component_data_length;
    }

    public byte getTapsCount() {
        return taps_count;
    }

    public Tap1 getTap() {
        return tap1;
    }

    public List<TapN> getTapOthers() {
        return tap_others;
    }

    public int getLength() {
        int length = 23;
        for ( int i=0; i<tap_others.size(); i++ ) {
            TapN tap = tap_others.get(i);
            length += (7 + tap.selector_data_byte.length);
        }
        return length;
    }

    public void print() {
        Logger.d(String.format("\t componentId_tag : 0x%x \n",
                componentId_tag));
        Logger.d(String.format("\t component_data_length : 0x%x \n",
                component_data_length));
        Logger.d(String.format("\t taps_count : 0x%x \n",
                taps_count));

        Logger.d(String.format("\t tap1.id : 0x%x \n", tap1.id));
        Logger.d(String.format("\t tap1.use : 0x%x \n", tap1.use));
        Logger.d(String.format("\t tap1.association_tag : 0x%x \n",
                tap1.association_tag));
        Logger.d(String.format("\t tap1.selector_length : 0x%x \n",
                tap1.selector_length));
        Logger.d(String.format("\t tap1.selector_type : 0x%x \n",
                tap1.selector_type));
        Logger.d(String.format("\t tap1.transactionId : 0x%x \n",
                tap1.transactionId));
        Logger.d(String.format("\t tap1.timeout : 0x%x \n",
                tap1.timeout));

        for ( int i=0; i<tap_others.size(); i++ ) {
            TapN tap = tap_others.get(i);
            Logger.d(String.format("\t [%d] tap.id : 0x%x \n", i, tap.id));
            Logger.d(String.format("\t [%d] tap.use : 0x%x \n", i, tap.use));
            Logger.d(String.format("\t [%d] tap.association_tag : 0x%x \n",
                    i, tap.association_tag));
            Logger.d(String.format("\t [%d] tap.selector_length : 0x%x \n",
                    i, tap.selector_length));
            Logger.d(String.format("\t [%d] tap.selector_data_byte : \n"));
            BinaryLogger.print(tap.selector_data_byte);
        }
    }
}
