package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class TelephoneDescriptor extends Descriptor {
    protected byte foreign_availability;
    protected byte connection_type;
    protected byte country_prefix_length;
    protected byte international_area_code_length;
    protected byte operator_code_length;
    protected byte national_area_code_length;
    protected byte core_number_length;
    protected byte[] country_prefix_char;
    protected byte[] international_area_code_char;
    protected byte[] operator_code_char;
    protected byte[] national_area_code_char;
    protected byte[] core_number_char;

    public TelephoneDescriptor(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(2);
        foreign_availability = (byte) brw.readOnBuffer(1);
        connection_type = (byte) brw.readOnBuffer(5);
        brw.skipOnBuffer(1);
        country_prefix_length = (byte) brw.readOnBuffer(2);
        international_area_code_length = (byte) brw.readOnBuffer(3);
        operator_code_length = (byte) brw.readOnBuffer(2);
        brw.skipOnBuffer(1);
        national_area_code_length = (byte) brw.readOnBuffer(3);
        core_number_length = (byte) brw.readOnBuffer(4);

        country_prefix_char = new byte[country_prefix_length];
        for ( int i=0; i<country_prefix_char.length; i++ ) {
            country_prefix_char[i] = (byte) brw.readOnBuffer(8);
        }

        international_area_code_char = new byte[international_area_code_length];
        for ( int i=0; i<international_area_code_char.length; i++ ) {
            international_area_code_char[i] = (byte) brw.readOnBuffer(8);
        }

        operator_code_char = new byte[operator_code_length];
        for ( int i=0; i<operator_code_char.length; i++ ) {
            operator_code_char[i] = (byte) brw.readOnBuffer(8);
        }

        national_area_code_char = new byte[national_area_code_length];
        for ( int i=0; i<national_area_code_char.length; i++ ) {
            national_area_code_char[i] = (byte) brw.readOnBuffer(8);
        }

        core_number_char = new byte[core_number_length];
        for ( int i=0; i<core_number_char.length; i++ ) {
            core_number_char[i] = (byte) brw.readOnBuffer(8);
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t foreign_availability : 0x%x \n", foreign_availability));
        Logger.d(String.format("\t connection_type : 0x%x \n", connection_type));
        Logger.d(String.format("\t country_prefix_length : 0x%x \n", country_prefix_length));
        Logger.d(String.format("\t international_area_code_length : 0x%x \n",
                international_area_code_length));
        Logger.d(String.format("\t operator_code_length : 0x%x \n", operator_code_length));
        Logger.d(String.format("\t national_area_code_length : 0x%x \n",
                national_area_code_length));
        Logger.d(String.format("\t core_number_length : 0x%x \n", core_number_length));

        Logger.d(String.format("\t country_prefix_char : %s \n",
                new String(country_prefix_char)));
        Logger.d(String.format("\t international_area_code_char : %s \n",
                new String(international_area_code_char)));
        Logger.d(String.format("\t operator_code_char : %s \n",
                new String(operator_code_char)));
        Logger.d(String.format("\t national_area_code_char : %s \n",
                new String(national_area_code_char)));
        Logger.d(String.format("\t core_number_char : %s \n", new String(core_number_char)));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 3 + country_prefix_char.length +
                international_area_code_char.length + operator_code_char.length +
                national_area_code_char.length + core_number_char.length;
    }
}
