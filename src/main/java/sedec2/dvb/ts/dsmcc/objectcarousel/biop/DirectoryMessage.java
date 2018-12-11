package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class DirectoryMessage extends BIOPMessage {
    protected byte[] objectInfo_data_byte;
    protected byte serviceContextList_count;
    protected List<ServiceContext> service_contexts = new ArrayList<>();
    protected int messageBody_length;
    protected int bindings_count;
    protected List<BIOPName> biop_names = new ArrayList<>();
    protected byte bindingType;
    protected InteroperableObjectReference ior;
    protected int objectInfo_length;
    protected byte[] objectInfo_byte;

    public DirectoryMessage(byte[] buffer) {
        super(buffer);

        objectInfo_data_byte = new byte[super.objectInfo_length];
        for ( int i=0; i<super.objectInfo_length; i++ ) {
            objectInfo_data_byte[i] = (byte) readOnBuffer(8);
        }
        serviceContextList_count = (byte) readOnBuffer(8);
        for ( int i=0; i<serviceContextList_count; i++ ) {
            ServiceContext ctx = new ServiceContext(this);
            service_contexts.add(ctx);
        }

        messageBody_length = readOnBuffer(32);
        bindings_count = readOnBuffer(16);

        for ( int i=0; i<bindings_count; i++ ) {
            BIOPName biop_name = new BIOPName(this);
            biop_names.add(biop_name);
        }

        bindingType = (byte) readOnBuffer(8);
        ior = new InteroperableObjectReference(this);
        objectInfo_length = readOnBuffer(16);
        objectInfo_byte = new byte[objectInfo_length];
        for ( int i=0; i<objectInfo_byte.length; i++ ) {
            objectInfo_byte[i] = (byte) readOnBuffer(8);
        }
    }

    @Override
    public int getLength() {
        int header_length = super.getLength();
        int payload_length = objectInfo_data_byte.length;

        payload_length += 1;

        for ( int i=0; i<service_contexts.size(); i++ ) {
            ServiceContext ctx = service_contexts.get(i);
            payload_length += ( 6 + ctx.getLength() );
        }
        payload_length += 6;

        for ( int i=0; i<biop_names.size(); i++ ) {
            BIOPName biop_name = biop_names.get(i);
            payload_length += biop_name.getLength();
        }

        payload_length += ( 1 + ior.getLength() );
        payload_length += ( 2 + objectInfo_byte.length);
        return header_length + payload_length;
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("\t objectInfo_data_byte : 0x%x \n", objectInfo_data_byte));
        Logger.d(String.format("\t serviceContextList_count : 0x%x \n",
                serviceContextList_count));
        for ( int i=0; i<service_contexts.size(); i++ ) {
            service_contexts.get(i).print();
        }

        Logger.d(String.format("\t messageBody_length : 0x%x \n", messageBody_length));
        Logger.d(String.format("\t bindings_count : 0x%x \n", bindings_count));

        for ( int i=0; i<biop_names.size(); i++ ) {
            biop_names.get(i).print();
        }

        Logger.d(String.format("\t bindingType : 0x%x \n", bindingType));
        ior.print();
        Logger.d(String.format("\t objectInfo_length : 0x%x \n", objectInfo_length));
        Logger.d(String.format("\t objectInfo_byte : \n", objectInfo_byte));
        BinaryLogger.print(objectInfo_byte);

        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
