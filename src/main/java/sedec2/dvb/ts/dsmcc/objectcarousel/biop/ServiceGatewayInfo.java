package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class ServiceGatewayInfo extends BitReadWriter {
    protected InteroperableObjectReference ior;
    protected byte downloadTaps_count;
    protected byte[] taps;
    protected byte serviceContextList_count;
    protected List<ServiceContext> service_contexts = new ArrayList<>();
    protected int userInfoLength;
    protected byte[] userInfo_data_byte;

    public class ServiceContext {
        public int context_id;
        public int context_data_length;
        public byte[] context_data_byte;
    }

    public ServiceGatewayInfo(byte[] buffer) {
        super(buffer);

        ior = new InteroperableObjectReference(this);
        downloadTaps_count = (byte) readOnBuffer(8);
        taps = new byte[downloadTaps_count];
        for ( int i=0; i<taps.length; i++ ) {
            taps[i] = (byte) readOnBuffer(8);
        }
        serviceContextList_count = (byte) readOnBuffer(8);
        for ( int i=0; i<serviceContextList_count; i++ ) {
            ServiceContext context = new ServiceContext();
            context.context_id = readOnBuffer(32);
            context.context_data_length = readOnBuffer(16);
            context.context_data_byte = new byte[context.context_data_length];
            for ( int k=0; k<context.context_data_byte.length; k++ ) {
                context.context_data_byte[k] = (byte) readOnBuffer(8);
            }
            service_contexts.add(context);
        }

        userInfoLength = readOnBuffer(16);
        userInfo_data_byte = new byte[userInfoLength];
        for ( int i=0; i<userInfo_data_byte.length; i++ ) {
            userInfo_data_byte[i] = (byte) readOnBuffer(8);
        }
    }

    public InteroperableObjectReference getIOR() {
        return ior;
    }

    public byte getDownloadTapsCount() {
        return downloadTaps_count;
    }

    public byte[] getTaps() {
        return taps;
    }

    public byte getServiceContextListCount() {
        return serviceContextList_count;
    }

    public List<ServiceContext> getServiceContexts() {
        return service_contexts;
    }

    public int getUserInfoLength() {
        return userInfoLength;
    }

    public byte[] getUserInfoDataByte() {
        return userInfo_data_byte;
    }

    public void print() {
        Logger.d(String.format("\t - %s - \n", getClass().getName()));
        ior.print();

        Logger.d(String.format("\t downloadTaps_count : 0x%x \n", downloadTaps_count));
        Logger.d(String.format("\t taps : \n"));
        BinaryLogger.print(taps);

        Logger.d(String.format("\t serviceContextList_count : 0x%x \n", serviceContextList_count));
        for ( int i=0; i<service_contexts.size(); i++ ) {
            ServiceContext ctx = service_contexts.get(i);
            Logger.d(String.format("\t [%d] context_id : 0x%x \n", i, ctx.context_id));
            Logger.d(String.format("\t [%d] context_data_length : 0x%x \n",
                    i, ctx.context_data_length));
            BinaryLogger.print(ctx.context_data_byte);
        }

        Logger.d(String.format("\t userInfoLength : 0x%x \n", userInfoLength));
        BinaryLogger.print(userInfo_data_byte);
    }
}
