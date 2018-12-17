package sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class LiteOptionsProfileBody extends TaggedProfile {
    protected byte profile_data_byte_order;
    protected byte component_count;
    protected ServiceLocation service_location;
    protected List<LiteOptionComponent> lite_opt_components = new ArrayList<>();

    public LiteOptionsProfileBody(BitReadWriter brw) {
        super(brw);

        profile_data_byte_order = (byte) brw.readOnBuffer(8);
        component_count = (byte) brw.readOnBuffer(8);

        service_location = new ServiceLocation(brw);
        int temp = service_location.getLength();

        for ( int i=profile_data_length-temp; i>0; ) {
            LiteOptionComponent component = new LiteOptionComponent(brw);
            i-=component.getLength();
            lite_opt_components.add(component);
        }
    }

    @Override
    public int getLength() {
        int header_length = super.getLength();
        int payload_length = 2 + service_location.getLength();

        for ( int i=0; i<lite_opt_components.size(); i++ ) {
            payload_length += lite_opt_components.get(i).getLength();
        }
        return header_length + payload_length;
    }

    public byte getProfileDataByteOrder() {
        return profile_data_byte_order;
    }

    public byte getComponentCount() {
        return component_count;
    }

    public ServiceLocation getServiceLocation() {
        return service_location;
    }

    public List<LiteOptionComponent> getLiteOptionComponents() {
        return lite_opt_components;
    }

    @Override
    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t profile_data_byte_order : 0x%x \n",
                profile_data_byte_order));
        Logger.d(String.format("\t component_count : 0x%x \n", component_count));

        if ( service_location != null ) service_location.print();

        for ( int i=0; i<lite_opt_components.size(); i++ ) {
            LiteOptionComponent component = lite_opt_components.get(i);
            component.print();
        }
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
