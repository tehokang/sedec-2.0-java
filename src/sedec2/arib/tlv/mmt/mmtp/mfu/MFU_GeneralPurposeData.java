package sedec2.arib.tlv.mmt.mmtp.mfu;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MFU_GeneralPurposeData extends BitReadWriter {
    protected int component_tag;
    protected byte data_type_length;
    protected byte[] data_type_char;
    protected int additional_info_length;
    protected byte[] additional_info_byte;
    protected int data_length;
    protected byte[] data_byte;
    
    public MFU_GeneralPurposeData(byte[] buffer) {
        super(buffer);
        
        component_tag = readOnBuffer(16);
        data_type_length = (byte) readOnBuffer(8);
        data_type_char = new byte[data_type_length];
        
        for ( int i=0; i<data_type_char.length; i++ ) {
            data_type_char[i] = (byte) readOnBuffer(8);
        }
        
        additional_info_length = readOnBuffer(16);
        additional_info_byte = new byte[additional_info_length];
        
        for ( int i=0; i<additional_info_byte.length; i++ ) {
            additional_info_byte[i] = (byte) readOnBuffer(8);
        }
        
        data_length = readOnBuffer(32);
        data_byte = new byte[data_length];
        
        for ( int i=0; i<data_byte.length; i++ ) {
            data_byte[i] = (byte) readOnBuffer(8);
        }
    }
    
    public void print() {
        Logger.d(String.format("- MFU_data_byte (GeneralPurpose) (%s)\n", getClass().getName()));
        Logger.d(String.format("component_tag : 0x%x \n", component_tag));
        Logger.d(String.format("data_type_length : 0x%x (%d) \n", 
                data_type_length, data_type_length));
        BinaryLogger.print(data_type_char);

        Logger.d(String.format("additional_info_length : 0x%x (%d) \n", 
                additional_info_length, additional_info_length));
        
        BinaryLogger.print(additional_info_byte);
        
        Logger.d(String.format("data_length : 0x%x (%d) \n", data_length, data_length));
        BinaryLogger.print(data_byte);
    }
}
