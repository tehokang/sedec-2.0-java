package com.sedec.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class CellListDescriptor extends Descriptor {
    protected List<Cell> cells = new ArrayList<>();

    public class Cell {
        public int cell_id;
        public int cell_latitude;
        public int cell_longitude;
        public int cell_extent_of_latitude;
        public int cell_extent_of_longitude;
        public byte subcell_info_loop_length;
        public List<SubCell> sub_cells = new ArrayList<>();
    }

    public class SubCell {
        public byte cell_id_extension;
        public int subcell_latitude;
        public int subcell_longitude;
        public int subcell_extent_of_latitude;
        public int subcell_extent_of_longitude;
    }

    public CellListDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            Cell cell = new Cell();
            cell.cell_id = brw.readOnBuffer(16);
            cell.cell_latitude = brw.readOnBuffer(16);
            cell.cell_longitude = brw.readOnBuffer(16);
            cell.cell_extent_of_latitude = brw.readOnBuffer(12);
            cell.cell_extent_of_longitude = brw.readOnBuffer(12);
            cell.subcell_info_loop_length = (byte) brw.readOnBuffer(8);

            for ( int k=cell.subcell_info_loop_length; k>0; ) {
                SubCell sub_cell = new SubCell();
                sub_cell.cell_id_extension = (byte) brw.readOnBuffer(8);
                sub_cell.subcell_latitude = brw.readOnBuffer(16);
                sub_cell.subcell_longitude = brw.readOnBuffer(16);
                sub_cell.subcell_extent_of_latitude = brw.readOnBuffer(12);
                sub_cell.subcell_extent_of_longitude = brw.readOnBuffer(12);
                k-=8;
                i-=8;
                cell.sub_cells.add(sub_cell);
            }
            i-=10;
            cells.add(cell);
        }
    }

    public List<Cell> getCells() {
        return cells;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<cells.size(); i++ ) {
            Cell cell = cells.get(i);
            Logger.d(String.format("\t [%d] cell_id : 0x%x \n", i, cell.cell_id));
            Logger.d(String.format("\t [%d] cell_latitude : 0x%x \n", i, cell.cell_latitude));
            Logger.d(String.format("\t [%d] cell_longitude : 0x%x \n", i, cell.cell_longitude));
            Logger.d(String.format("\t [%d] cell_extent_of_latitude : 0x%x \n",
                    i, cell.cell_extent_of_latitude));
            Logger.d(String.format("\t cell_extent_of_longitude : 0x%x \n",
                    i, cell.cell_extent_of_longitude));

            for ( int k=0; k<cell.sub_cells.size(); k++ ) {
                SubCell sub_cell = cell.sub_cells.get(k);
                Logger.d(String.format("\t   [%d] cell_id_extension : 0x%x \n",
                        k, sub_cell.cell_id_extension));
                Logger.d(String.format("\t   [%d] subcell_latitude : 0x%x \n",
                        k, sub_cell.subcell_latitude));
                Logger.d(String.format("\t   [%d] subcell_longitude : 0x%x \n",
                        k, sub_cell.subcell_longitude));
                Logger.d(String.format("\t   [%d] subcell_extent_of_latitude : 0x%x \n",
                        k, sub_cell.subcell_extent_of_latitude));
                Logger.d(String.format("\t   [%d] subcell_extent_of_longitude : 0x%x \n",
                        k, sub_cell.subcell_extent_of_longitude));
            }
        }
    }

    @Override
    protected void updateDescriptorLength() {
        for ( int i=0; i<cells.size(); i++ ) {
            Cell cell = cells.get(i);
            descriptor_length += 10;
            for ( int k=0; k<cell.sub_cells.size(); k++ ) {
                descriptor_length += 8;
            }
        }
    }
}
