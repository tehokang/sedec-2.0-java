package sedec2.dvb.ts.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class CellListDescriptor extends Descriptor {
    protected int cell_id;
    protected int cell_latitude;
    protected int cell_longitude;
    protected int cell_extent_of_latitude;
    protected int cell_extent_of_longitude;
    protected byte subcell_info_loop_length;

    protected byte cell_id_extension;
    protected int subcell_latitude;
    protected int subcell_longitude;
    protected int subcell_extent_of_latitude;
    protected int subcell_extent_of_longitude;

    public CellListDescriptor(BitReadWriter brw) {
        super(brw);

        cell_id = brw.readOnBuffer(16);
        cell_latitude = brw.readOnBuffer(16);
        cell_longitude = brw.readOnBuffer(16);
        cell_extent_of_latitude = brw.readOnBuffer(12);
        cell_extent_of_longitude = brw.readOnBuffer(12);
        subcell_info_loop_length = (byte) brw.readOnBuffer(8);

        for ( int i=subcell_info_loop_length; i>0; ) {
            cell_id_extension = (byte) brw.readOnBuffer(8);
            subcell_latitude = brw.readOnBuffer(16);
            subcell_longitude = brw.readOnBuffer(16);
            subcell_extent_of_latitude = brw.readOnBuffer(12);
            subcell_extent_of_longitude = brw.readOnBuffer(12);
            i-=8;
        }
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t cell_id : 0x%x \n", cell_id));
        Logger.d(String.format("\t cell_latitude : 0x%x \n", cell_latitude));
        Logger.d(String.format("\t cell_longitude : 0x%x \n", cell_longitude));
        Logger.d(String.format("\t cell_extent_of_latitude : 0x%x \n",
                cell_extent_of_latitude));
        Logger.d(String.format("\t cell_extent_of_longitude : 0x%x \n",
                cell_extent_of_longitude));

        for ( int i=subcell_info_loop_length; i>0; ) {
            Logger.d(String.format("\t cell_id_extension : 0x%x \n", cell_id_extension));
            Logger.d(String.format("\t subcell_latitude : 0x%x \n", subcell_latitude));
            Logger.d(String.format("\t subcell_longitude : 0x%x \n", subcell_longitude));
            Logger.d(String.format("\t subcell_extent_of_latitude : 0x%x \n",
                    subcell_extent_of_latitude));
            Logger.d(String.format("\t subcell_extent_of_longitude : 0x%x \n",
                    subcell_extent_of_longitude));
            i-=8;
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 10 + subcell_info_loop_length;
    }
}
