package sedec2.dvb.ts.dsmcc.objectcarousel.biop;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class LiteOptionComponent {
    protected int componentId_tag;
    protected byte component_data_length;
    protected byte[] component_data_byte;

    public LiteOptionComponent(BitReadWriter brw) {
        componentId_tag = brw.readOnBuffer(32);
        component_data_length = (byte) brw.readOnBuffer(8);
        component_data_byte = new byte[component_data_length];

        for ( int i=0; i<component_data_byte.length; i++ ) {
            component_data_byte[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getLength() {
        return 5 + component_data_byte.length;
    }

    public int getComponentIdTag() {
        return componentId_tag;
    }

    public byte getComponentDataLength() {
        return component_data_length;
    }

    public byte[] getComponentDataByte() {
        return component_data_byte;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t componentId_tag : 0x%x \n", componentId_tag));
        Logger.d(String.format("\t component_data_length : 0x%x \n", component_data_length));
        Logger.d(String.format("\t component_data_byte : \n"));
        BinaryLogger.print(component_data_byte);
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
