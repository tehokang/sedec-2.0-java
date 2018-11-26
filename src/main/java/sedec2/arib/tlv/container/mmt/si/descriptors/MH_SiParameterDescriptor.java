package sedec2.arib.tlv.container.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MH_SiParameterDescriptor extends Descriptor {
    protected byte parameter_version;
    protected int update_time;
    protected List<SiParameter> si_parameters = new ArrayList<>();

    public class SiParameter {
        public byte table_id;
        public byte table_description_length;
        public byte[] table_description_byte;
    }

    public MH_SiParameterDescriptor(BitReadWriter brw) {
        super(brw);

        parameter_version = (byte) brw.readOnBuffer(8);
        update_time = brw.readOnBuffer(16);

        for ( int i=descriptor_length-3; i>0; ) {
            SiParameter si_param = new SiParameter();
            si_param.table_id = (byte) brw.readOnBuffer(8);
            si_param.table_description_length = (byte) brw.readOnBuffer(8);

            si_param.table_description_byte = new byte[si_param.table_description_length];

            for ( int j=0; j<si_param.table_description_byte.length; j++ ) {
                si_param.table_description_byte[j] = (byte) brw.readOnBuffer(8);
            }

            si_parameters.add(si_param);
            i-=(2 + si_param.table_description_length);
        }
    }

    public byte getParameterVersion() {
        return parameter_version;
    }

    public int getUpdateTime() {
        return update_time;
    }

    public List<SiParameter> getSiParameters() {
        return si_parameters;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t parameter_version : 0x%x \n", parameter_version));
        Logger.d(String.format("\t update_time : 0x%x \n", update_time));

        for ( int i=0; i<si_parameters.size(); i++ ) {
            SiParameter si_param = si_parameters.get(i);

            Logger.d(String.format("\t [%d] table_id : 0x%x \n",
                    i, si_param.table_id));
            Logger.d(String.format("\t [%d] table_description_length : 0x%x \n",
                    i, si_param.table_description_length));
            Logger.d(String.format("\t [%d] table_description_byte : \n", i));

            BinaryLogger.print(si_param.table_description_byte);
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3;

        for ( int i=0; i<si_parameters.size(); i++ ) {
            descriptor_length += ( 2 + si_parameters.get(i).table_description_length );
        }
    }
}
