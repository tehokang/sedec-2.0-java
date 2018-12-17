package sedec2.dvb.ts.si.tables.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class ServiceLocation {
    protected int componentId_tag;
    protected int component_data_length;
    protected byte serviceDomain_length;
    protected DVBCarouselNSAPAddress serviceDomain_data;
    protected CosNaming cos_naming;

    public class CosNaming {
        public int nameComponents_count;
        public List<Name> names = new ArrayList<>();
        public int initialContext_length;
        public byte[] initialContext_data_byte;
    }

    public class Name {
        public int id_length;
        public byte[] id_data_byte;
        public int kind_length;
        public byte[] kind_data_byte;
    }

    public ServiceLocation(BitReadWriter brw) {
        componentId_tag = brw.readOnBuffer(32);
        component_data_length = brw.readOnBuffer(32);
        serviceDomain_length = (byte) brw.readOnBuffer(8);

        serviceDomain_data = new DVBCarouselNSAPAddress(brw);

        cos_naming = new CosNaming();
        cos_naming.nameComponents_count = brw.readOnBuffer(32);
        for ( int i=0; i<cos_naming.nameComponents_count; i++ ) {
            Name name = new Name();
            name.id_length = brw.readOnBuffer(32);
            name.id_data_byte = new byte[name.id_length];
            for ( int k=0; k<name.id_data_byte.length; k++ ) {
                name.id_data_byte[k] = (byte) brw.readOnBuffer(8);
            }
            name.kind_length = brw.readOnBuffer(32);
            name.kind_data_byte = new byte[name.kind_length];
            for ( int k=0; k<name.kind_data_byte.length; k++ ) {
                name.kind_data_byte[k] = (byte) brw.readOnBuffer(8);
            }
            cos_naming.names.add(name);
        }
        cos_naming.initialContext_length = brw.readOnBuffer(32);
        cos_naming.initialContext_data_byte = new byte[cos_naming.initialContext_length];
        for ( int i=0; i<cos_naming.initialContext_data_byte.length; i++ ) {
            cos_naming.initialContext_data_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getLength() {
        int length = 9 + serviceDomain_data.getLength() + 4;

        for ( int i=0; i<cos_naming.names.size(); i++ ) {
            Name name = cos_naming.names.get(i);
            length += (4 + name.id_data_byte.length + 4 + name.kind_data_byte.length);
        }

        length += (4 + cos_naming.initialContext_data_byte.length);
        return length;
    }

    public int getComponentIdTag() {
        return componentId_tag;
    }

    public int getComponentDataLength() {
        return component_data_length;
    }

    public byte getServiceDomainLength() {
        return serviceDomain_length;
    }

    public DVBCarouselNSAPAddress getServiceDomainData() {
        return serviceDomain_data;
    }

    public CosNaming getCosNaming() {
        return cos_naming;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t componentId_tag : 0x%x \n", componentId_tag));
        Logger.d(String.format("\t component_data_length : 0x%x \n", component_data_length));
        Logger.d(String.format("\t serviceDomain_length : 0x%x \n", serviceDomain_length));

        if ( serviceDomain_data != null ) serviceDomain_data.print();

        if ( cos_naming != null ) {
            Logger.d(String.format("\t cos_naming.nameComponents_count : 0x%x \n",
                    cos_naming.nameComponents_count));
            for ( int i=0; i<cos_naming.names.size(); i++ ) {
                Name name = cos_naming.names.get(i);
                Logger.d(String.format("\t [%d] name.id_length : 0x%x \n", i, name.id_length));
                Logger.d(String.format("\t [%d] name.id_data_byte : 0x%x \n", i, name.id_data_byte));
                Logger.d(String.format("\t [%d] name.kind_length : 0x%x \n", i, name.kind_length));
                Logger.d(String.format("\t [%d] name.kind_data_byte : %s \n",
                        i, new String(name.kind_data_byte)));
            }
            Logger.d(String.format("\t cos_naming.initialContext_length : 0x%x \n",
                    cos_naming.initialContext_length));
            Logger.d(String.format("\t cos_naming.initialContext_data_byte : \n"));
            BinaryLogger.print(cos_naming.initialContext_data_byte);
            Logger.d(String.format("\t - Endif of %s - \n", getClass().getName()));
        }
    }
}
