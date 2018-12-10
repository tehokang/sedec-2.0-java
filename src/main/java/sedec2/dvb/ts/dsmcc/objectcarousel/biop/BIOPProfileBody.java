package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;

public class BIOPProfileBody extends Profile {
    protected byte profile_data_byte_order;
    protected byte liteComponents_count;
    protected ObjectLocation object_location;
    protected ConnBinder conn_binder;
    protected LiteComponent list_component;

    public class ObjectLocation {
        public int componentid_tag;
        public byte component_data_length;
        public int carouselId;
        public int moduleId;
        public byte version_major;
        public byte version_minor;
        public byte objectKey_length;
        public byte[] objectKey_data_byte;
    }

    public class ConnBinder {
        public int componentId_tag;
        public byte component_data_length;
        public byte taps_count;
        public Tap1 tap1;
        public List<TapN> tap_others = new ArrayList<>();
    }

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

    public class LiteComponent {
        public int componentId_tag;
        public byte component_data_length;
        public byte[] component_data_byte;
    }

    public BIOPProfileBody(BitReadWriter brw) {
        super(brw);

        profile_data_byte_order = (byte) brw.readOnBuffer(8);
        liteComponents_count = (byte) brw.readOnBuffer(8);

        // BIOP::ObjectLocation
        object_location = new ObjectLocation();
        object_location.componentid_tag = brw.readOnBuffer(32);
        object_location.component_data_length = (byte) brw.readOnBuffer(8);
        object_location.carouselId = brw.readOnBuffer(32);
        object_location.moduleId = brw.readOnBuffer(16);
        object_location.version_major = (byte) brw.readOnBuffer(8);
        object_location.version_minor = (byte) brw.readOnBuffer(8);
        object_location.objectKey_length = (byte) brw.readOnBuffer(8);
        object_location.objectKey_data_byte = new byte[object_location.objectKey_length];

        for ( int i=0; i<object_location.objectKey_data_byte.length; i++ ) {
            object_location.objectKey_data_byte[i] = (byte) brw.readOnBuffer(8);
        }

        // DSM::ConnBinder
        conn_binder = new ConnBinder();
        conn_binder.componentId_tag = brw.readOnBuffer(32);
        conn_binder.component_data_length = (byte) brw.readOnBuffer(8);
        conn_binder.taps_count = (byte) brw.readOnBuffer(8);

        conn_binder.tap1 = new Tap1();
        conn_binder.tap1.id = brw.readOnBuffer(16);
        conn_binder.tap1.use = brw.readOnBuffer(16);
        conn_binder.tap1.association_tag = brw.readOnBuffer(16);
        conn_binder.tap1.selector_length = (byte) brw.readOnBuffer(8);
        conn_binder.tap1.selector_type = brw.readOnBuffer(16);
        conn_binder.tap1.transactionId = brw.readOnBuffer(32);
        conn_binder.tap1.timeout = brw.readOnBuffer(32);

        for ( int i=0; i<conn_binder.taps_count-1; i++ ) {
            TapN tap = new TapN();
            tap.id = brw.readOnBuffer(16);
            tap.use = brw.readOnBuffer(16);
            tap.association_tag = brw.readOnBuffer(16);
            tap.selector_length = (byte) brw.readOnBuffer(8);

        }

    }
}
