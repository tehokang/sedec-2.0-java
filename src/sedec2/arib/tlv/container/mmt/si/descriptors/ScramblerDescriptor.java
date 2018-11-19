package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class ScramblerDescriptor extends Descriptor {
    protected byte layer_type;
    protected byte scrambler_system_id;
    protected byte[] private_data;
    
    public ScramblerDescriptor(BitReadWriter brw) {
        super(brw);
        
        layer_type = (byte) brw.readOnBuffer(2);
        brw.skipOnBuffer(6);
        scrambler_system_id = (byte) brw.readOnBuffer(8);
        
        private_data = new byte[descriptor_length-2];
        for ( int i=0; i<private_data.length; i++ ) {
            private_data[i] = (byte) brw.readOnBuffer(8);
        }
    }

    public byte getLayerType() {
        return layer_type;
    }
    
    public byte getScramblerSystemId() {
        return scrambler_system_id;
    }
    
    public byte[] getPrivateData() {
        return private_data;
    }
    
    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t layer_type : 0x%x \n", layer_type));
        Logger.d(String.format("\t scrambler_system_id : 0x%x \n", 
                scrambler_system_id));
        
        Logger.d("private_data : \n");
        BinaryLogger.print(private_data);
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + private_data.length;
    }

}
