package sedec2.dvb.ts.si.tables.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class BIOPProfileBody extends TaggedProfile {
    protected byte profile_data_byte_order;
    protected byte liteComponents_count;
    protected ObjectLocation object_location;
    protected ConnBinder conn_binder;
    protected List<LiteComponent> lite_components = new ArrayList<>();

    public BIOPProfileBody(BitReadWriter brw) {
        super(brw);

        profile_data_byte_order = (byte) brw.readOnBuffer(8);
        liteComponents_count = (byte) brw.readOnBuffer(8);

        int temp = 2;
        // BIOP::ObjectLocation
        object_location = new ObjectLocation(brw);
        temp += object_location.getLength();

        // DSM::ConnBinder
        conn_binder = new ConnBinder(brw);
        temp += conn_binder.getLength();

        for ( int i=profile_data_length-temp; i>0; ) {
            LiteComponent component = new LiteComponent(brw);
            lite_components.add(component);
            i-= component.getLength();
        }
    }

    @Override
    public int getLength() {
        int header_length = super.getLength();
        int payload_length = 2;
        payload_length += ( object_location.getLength() + conn_binder.getLength() );
        for ( int i=0; i<lite_components.size(); i++ ) {
            payload_length += lite_components.get(i).getLength();
        }
        return header_length + payload_length;
    }

    public byte getProfileDataByteOrder() {
        return profile_data_byte_order;
    }

    public byte getLiteComponentsCount() {
        return liteComponents_count;
    }

    public ObjectLocation getObjectLocation() {
        return object_location;
    }

    public ConnBinder getConnBinder() {
        return conn_binder;
    }

    public List<LiteComponent> getLiteComponents() {
        return lite_components;
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("\t profile_data_byte_order : 0x%x \n",
                profile_data_byte_order));
        Logger.d(String.format("\t liteComponents_count : 0x%x \n",
                liteComponents_count));

        // BIOP::ObjectLocation
        if ( object_location != null ) object_location.print();

        // DSM::ConnBinder
        if ( conn_binder != null ) conn_binder.print();

        for ( int i=0; i<lite_components.size(); i++ ) {
            lite_components.get(i).print();
        }
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
