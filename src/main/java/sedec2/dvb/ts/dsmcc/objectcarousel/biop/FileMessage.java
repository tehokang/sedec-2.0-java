package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import java.util.ArrayList;
import java.util.List;

import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class FileMessage extends BIOPMessage {
    protected long content_size;
    protected byte[] objectInfo_data_byte;
    protected byte serviceContextList_count;
    protected List<ServiceContext> service_contexts = new ArrayList<>();
    protected int messageBody_length;
    protected int content_length;
    protected byte[] content_data_byte;

    public FileMessage(byte[] buffer) {
        super(buffer);

        content_size = readOnBuffer(64);
        for ( int i=0; i<objectInfo_length; i++ ) {
            objectInfo_data_byte[i] = (byte) readOnBuffer(8);
        }

        serviceContextList_count = (byte) readOnBuffer(8);
        for ( int i=0; i<serviceContextList_count; i++ ) {
            ServiceContext ctx = new ServiceContext(this);
            service_contexts.add(ctx);
        }
        messageBody_length = readOnBuffer(32);
        content_length = readOnBuffer(32);
        content_data_byte = new byte[content_length];

        for ( int i=0; i<content_data_byte.length; i++ ) {
            content_data_byte[i] = (byte) readOnBuffer(8);
        }
    }

    public long getContentSize() {
        return content_size;
    }

    public byte[] getObjectInfoDataByte() {
        return objectInfo_data_byte;
    }

    public byte getServiceContextListCount() {
        return serviceContextList_count;
    }

    public List<ServiceContext> getServiceContext() {
        return service_contexts;
    }

    public int getMessageBodyLength() {
        return messageBody_length;
    }

    public int getContentLength() {
        return content_length;
    }

    public byte[] getContentDataByte() {
        return content_data_byte;
    }

    @Override
    public int getLength() {
        int header_length = super.getLength();
        int payload_length = 8 + objectInfo_data_byte.length;
        payload_length += 1;
        for ( int i=0; i<service_contexts.size(); i++ ) {
            payload_length += service_contexts.get(i).getLength();
        }
        payload_length += (8 + content_data_byte.length);
        return header_length + payload_length;
    }

    @Override
    public void print() {
        super.print();

        Logger.d(String.format("\t content_size : 0x%x \n", content_size));
        Logger.d(String.format("\t objectInfo_data_byte : \n"));
        BinaryLogger.print(objectInfo_data_byte);

        Logger.d(String.format("\t serviceContextList_count : 0x%x \n",
                serviceContextList_count));
        for ( int i=0; i<service_contexts.size(); i++ ) {
            service_contexts.get(i).print();
        }

        Logger.d(String.format("\t messageBody_length : 0x%x \n", messageBody_length));
        Logger.d(String.format("\t content_length : 0x%x \n", content_length));
        Logger.d(String.format("\t content_data_byte : \n"));
        BinaryLogger.print(content_data_byte);

        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
