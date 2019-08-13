package com.sedec.dvb.ts.si.tables.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class StreamEventMessage extends BIOPMessage {
    protected StreamInfo stream_info;
    protected EventList event_list;
    protected byte[] objectInfo_byte;
    protected byte serviceContextList_count;
    protected List<ServiceContext> service_contexts = new ArrayList<>();
    protected int messageBody_length;
    protected byte taps_count;
    protected List<Tap> taps = new ArrayList<>();
    protected byte eventIds_count;
    protected int[] eventId;

    public class Tap {
        public int id;
        public int use;
        public int association_tag;
        public byte selector_length;
    }

    public StreamEventMessage(byte[] buffer) {
        super(buffer);

        stream_info = new StreamInfo(this);
        event_list = new EventList(this);
        objectInfo_byte =
                new byte[objectInfo_length-stream_info.getLength()-event_list.getLength()];
        for ( int i=0; i<objectInfo_byte.length; i++ ) {
            objectInfo_byte[i] = (byte) readOnBuffer(8);
        }

        serviceContextList_count = (byte) readOnBuffer(8);
        for ( int i=0; i<serviceContextList_count; i++ ) {
            ServiceContext ctx = new ServiceContext(this);
            service_contexts.add(ctx);
        }
        messageBody_length = readOnBuffer(32);
        taps_count = (byte) readOnBuffer(8);

        for ( int i=0; i<taps_count; i++ ) {
            Tap tap = new Tap();
            tap.id = readOnBuffer(16);
            tap.use = readOnBuffer(16);
            tap.association_tag = readOnBuffer(16);
            tap.selector_length = (byte) readOnBuffer(8);
            taps.add(tap);
        }
        eventIds_count = (byte) readOnBuffer(8);
        eventId = new int[eventIds_count];
        for ( int i=0; i<eventId.length; i++ ) {
            eventId[i] = readOnBuffer(16);
        }
    }

    @Override
    public int getLength() {
        int header_length = super.getLength();
        int payload_length = stream_info.getLength() + event_list.getLength();
        payload_length += objectInfo_byte.length;
        payload_length += 1;
        for ( int i=0; i<service_contexts.size(); i++ ) {
            payload_length += service_contexts.get(i).getLength();
        }
        payload_length += 5;
        payload_length += (taps.size()*7);
        payload_length += (1 + eventId.length*2);
        return header_length + payload_length;
    }

    @Override
    public void print() {
        if ( stream_info != null ) stream_info.print();
        if ( event_list != null ) event_list.print();
        Logger.d(String.format("\t objectInfo_byte.length : %d \n",
                objectInfo_byte.length));
        BinaryLogger.print(objectInfo_byte);

        Logger.d(String.format("\t serviceContextList_count : 0x%x \n",
                serviceContextList_count));
        for ( int i=0; i<service_contexts.size(); i++ ) {
            service_contexts.get(i).print();
        }

        Logger.d(String.format("\t messageBody_length : 0x%x \n", messageBody_length));
        Logger.d(String.format("\t taps_count : 0x%x \n", taps_count));

        for ( int i=0; i<taps.size(); i++ ) {
            Tap tap = taps.get(i);
            Logger.d(String.format("\t [%d] tap.id : 0x%x \n", i, tap.id));
            Logger.d(String.format("\t [%d] tap.use : 0x%x \n", i, tap.use));
            Logger.d(String.format("\t [%d] tap.association_tag : 0x%x \n",
                    i, tap.association_tag));
            Logger.d(String.format("\t [%d] tap.selector_length : 0x%x \n",
                    i, tap.selector_length));
        }
        Logger.d(String.format("\t eventIds_count : 0x%x \n", eventIds_count));
        for ( int i=0; i<eventId.length; i++ ) {
            Logger.d(String.format("\t [%d] eventId : 0x%x \n", i, eventId[i]));
        }

        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
