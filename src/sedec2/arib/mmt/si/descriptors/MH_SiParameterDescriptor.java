package sedec2.arib.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MH_SiParameterDescriptor extends Descriptor {
    protected byte parameter_version;
    protected int update_time;
    protected List<SiParameter> si_parameters = new ArrayList<>();
    
    class SiParameter {
        public byte table_id;
        public byte table_description_length;
        public byte[] table_description_byte;
    }
        
    public MH_SiParameterDescriptor(BitReadWriter brw) {
        super(brw);
        
        parameter_version = (byte) brw.ReadOnBuffer(8);
        update_time = brw.ReadOnBuffer(16);
        
        for ( int i=descriptor_length-3; i>0; ) {
            SiParameter si_param = new SiParameter();
            si_param.table_id = (byte) brw.ReadOnBuffer(8);
            si_param.table_description_length = (byte) brw.ReadOnBuffer(8);
            
            si_param.table_description_byte = new byte[si_param.table_description_length];
            
            for ( int j=0; j<si_param.table_description_byte.length; j++ ) {
                si_param.table_description_byte[j] = (byte) brw.ReadOnBuffer(8);
            }
            
            si_parameters.add(si_param);
            i-=(2 + si_param.table_description_length);
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        
        Logger.d(String.format("\t parameter_version : 0x%x \n", parameter_version));
        Logger.d(String.format("\t parameter_version : 0x%x \n", parameter_version));
        
        for ( int i=0; i<si_parameters.size(); i++ ) {
            SiParameter si_param = si_parameters.get(i);
            
            Logger.d(String.format("\t [%d] table_id : 0x%x \n", 
                    i, si_param.table_id));
            Logger.d(String.format("\t [%d] parameter_version : 0x%x \n", 
                    i, si_param.table_description_length));
            Logger.d(String.format("\t [%d] parameter_version : %s \n", 
                    i, new String(si_param.table_description_byte)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        // TODO Auto-generated method stub

    }

}