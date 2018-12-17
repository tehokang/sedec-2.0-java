package sedec2.arib.tlv.container.mmt.si.tables.dsmcc.objectcarousel.biop;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class ObjectLocation {
    protected int componentId_tag;
    protected byte component_data_length;
    protected int carouselId;
    protected int moduleId;
    protected byte version_major;
    protected byte version_minor;
    protected byte objectKey_length;
    protected byte[] objectKey_data_byte;

    public ObjectLocation(BitReadWriter brw) {
        componentId_tag = brw.readOnBuffer(32);
        component_data_length = (byte) brw.readOnBuffer(8);
        carouselId = brw.readOnBuffer(32);
        moduleId = brw.readOnBuffer(16);
        version_major = (byte) brw.readOnBuffer(8);
        version_minor = (byte) brw.readOnBuffer(8);
        objectKey_length = (byte) brw.readOnBuffer(8);
        objectKey_data_byte = new byte[objectKey_length];

        for ( int i=0; i<objectKey_data_byte.length; i++ ) {
            objectKey_data_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getLength() {
        return 14 + objectKey_data_byte.length;
    }

    public int getComponentIdTag() {
        return componentId_tag;
    }

    public byte getComponentDataLength() {
        return component_data_length;
    }

    public int getCarouselId() {
        return carouselId;
    }
    public int getModuleId() {
        return moduleId;
    }

    public byte getVersionMajor() {
        return version_major;
    }

    public byte getVersionMinor() {
        return version_minor;
    }

    public byte getObjectKeyLength() {
        return objectKey_length;
    }

    public byte[] getObjectKeyDataByte() {
        return objectKey_data_byte;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t componentId_tag : 0x%x \n", componentId_tag));
        Logger.d(String.format("\t component_data_length : 0x%x \n", component_data_length));
        Logger.d(String.format("\t carouselId : 0x%x \n", carouselId));
        Logger.d(String.format("\t moduleId : 0x%x \n", moduleId));
        Logger.d(String.format("\t version_major : 0x%x \n", version_major));
        Logger.d(String.format("\t version_minor : 0x%x \n", version_minor));
        Logger.d(String.format("\t objectKey_length : 0x%x \n", objectKey_length));
        Logger.d(String.format("\t objectKey_data_byte : %s \n",
                new String(objectKey_data_byte)));
        BinaryLogger.print(objectKey_data_byte);
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
