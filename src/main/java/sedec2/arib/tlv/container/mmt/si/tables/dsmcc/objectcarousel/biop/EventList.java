package sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class EventList {
    protected int eventNames_count;
    protected List<EventName> event_names = new ArrayList<>();

    public class EventName {
        protected byte eventName_length;
        protected byte[] eventName_data_byte;
    }

    public EventList(BitReadWriter brw) {
        eventNames_count = brw.readOnBuffer(16);

        for ( int i=0; i<eventNames_count; i++ ) {
            EventName name = new EventName();
            name.eventName_length = (byte) brw.readOnBuffer(8);
            name.eventName_data_byte = new byte[name.eventName_length];

            for ( int k=0; k<name.eventName_data_byte.length; k++ ) {
                name.eventName_data_byte[k] = (byte) brw.readOnBuffer(8);
            }
            event_names.add(name);
        }
    }

    public int getLength() {
        int length = 2;

        for ( int i=0; i<event_names.size(); i++ ) {
            length += ( 1 + event_names.get(i).eventName_length );
        }
        return length;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t eventNames_count : 0x%x \n", eventNames_count));

        for ( int i=0; i<event_names.size(); i++ ) {
            EventName name = event_names.get(i);
            Logger.d(String.format("\t [%d] eventName_length : 0x%x \n",
                    i, name.eventName_length));
            Logger.d(String.format("\t [%d] eventName_data_byte : \n"));
            BinaryLogger.print(name.eventName_data_byte);
        }
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
