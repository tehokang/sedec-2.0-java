package com.sedec.arib.b10.tables.dsmcc.objectcarousel.biop;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class BIOPMessage extends BitReadWriter {
    protected int magic;
    protected byte version_major;
    protected byte version_minor;
    protected byte byte_order;
    protected byte message_type;
    protected int message_size;
    protected byte objectKey_length;
    protected byte[] objectKey_data_byte;
    protected int objectKind_length;
    protected byte[] objectKind_data;
    protected int objectInfo_length;

    public BIOPMessage(byte[] buffer) {
        super(buffer);

        magic = readOnBuffer(32);
        version_major = (byte) readOnBuffer(8);
        version_minor = (byte) readOnBuffer(8);
        byte_order = (byte) readOnBuffer(8);
        message_type = (byte) readOnBuffer(8);
        message_size = (byte) readOnBuffer(32);
        objectKey_length = (byte) readOnBuffer(8);
        objectKey_data_byte = new byte[objectKey_length];
        for ( int i=0; i<objectKey_data_byte.length; i++ ) {
            objectKey_data_byte[i] = (byte) readOnBuffer(8);
        }
        objectKind_length = readOnBuffer(32);
        objectKind_data = new byte[objectKind_length];
        for ( int i=0; i<objectKind_data.length; i++ ) {
            objectKind_data[i] = (byte) readOnBuffer(8);
        }
        objectInfo_length = readOnBuffer(16);
    }

    public int getMagic() {
        return magic;
    }

    public byte getVersionMajor() {
        return version_major;
    }

    public byte getVersionMinor() {
        return version_minor;
    }

    public byte getByteOrder() {
        return byte_order;
    }

    public byte getMessageType() {
        return message_type;
    }

    public int getMessageSize() {
        return message_size;
    }

    public byte[] getObjectKeyDataByte() {
        return objectKey_data_byte;
    }

    public byte[] getObjectKindDataByte() {
        return objectKind_data;
    }

    public int getObjectInfoLength() {
        return objectInfo_length;
    }

    public int getLength() {
        return 19 + objectKey_data_byte.length + objectKind_data.length;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t magic : 0x%x \n", magic));
        Logger.d(String.format("\t version_major : 0x%x \n", version_major));
        Logger.d(String.format("\t version_minor : 0x%x \n", version_minor));
        Logger.d(String.format("\t byte_order : 0x%x \n", byte_order));
        Logger.d(String.format("\t message_type : 0x%x \n", message_type));
        Logger.d(String.format("\t message_size : 0x%x \n", message_size));
        Logger.d(String.format("\t objectKey_length : 0x%x \n", objectKey_length));
        Logger.d(String.format("\t objectKey_data_byte : \n"));
        BinaryLogger.print(objectKey_data_byte);

        Logger.d(String.format("\t objectKind_length : 0x%x \n", objectKind_length));
        Logger.d(String.format("\t objectKind_data : %s \n",
                new String(objectKind_data)));
        Logger.d(String.format("\t objectInfo_length : 0x%x \n", objectInfo_length));
    }
}
