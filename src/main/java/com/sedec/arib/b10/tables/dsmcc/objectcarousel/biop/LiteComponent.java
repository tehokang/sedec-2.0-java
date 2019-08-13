package com.sedec.arib.b10.tables.dsmcc.objectcarousel.biop;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class LiteComponent {
    protected int componentId_tag;
    protected byte component_data_length;
    protected byte[] component_data_byte;

    public LiteComponent(BitReadWriter brw) {
        componentId_tag = brw.readOnBuffer(32);
        component_data_length = (byte) brw.readOnBuffer(8);
        component_data_byte = new byte[component_data_length];

        for ( int k=0; k<component_data_byte.length; k++ ) {
            component_data_byte[k] = (byte) brw.readOnBuffer(8);
        }
    }

    public int getComponentIdTag() {
        return componentId_tag;
    }

    public byte getComponeneDataLength() {
        return component_data_length;
    }

    public byte[] getComponentDataByte() {
        return component_data_byte;
    }

    public int getLength() {
        return 4 + 1 + component_data_byte.length;
    }

    public void print() {
        Logger.d(String.format("\t - Begin of %s - \n", getClass().getName()));
        Logger.d(String.format("\t componentId_tag : 0x%x \n",
                componentId_tag));
        Logger.d(String.format("\t component_data_length : 0x%x \n",
                component_data_length));
        Logger.d(String.format("\t component_data_byte : \n"));
        BinaryLogger.print(component_data_byte);
        Logger.d(String.format("\t - End of %s - \n", getClass().getName()));
    }
}
