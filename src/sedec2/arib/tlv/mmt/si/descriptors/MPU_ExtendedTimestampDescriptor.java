package sedec2.arib.tlv.mmt.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class MPU_ExtendedTimestampDescriptor extends Descriptor {
    protected byte pts_offset_type;
    protected byte timescale_flag;
    protected int timescale;
    protected int default_pts_offset;
    protected List<Timestamp> timestamps = new ArrayList<>();
    
    class Timestamp {
        public int mpu_sequence_number;
        public byte mpu_presentation_time_leap_indicator;
        public int mpu_decoding_time_offset;
        public byte num_of_au;
        public List<AccessUnit> access_units = new ArrayList<>();
    }
    
    class AccessUnit {
        public int dts_pts_offset;
        public int pts_offset;
    }
    
    public MPU_ExtendedTimestampDescriptor(BitReadWriter brw) {
        super(brw);
        
        brw.skipOnBuffer(5);
        pts_offset_type = (byte) brw.readOnBuffer(2);
        timescale_flag = (byte) brw.readOnBuffer(1);
        
        if ( timescale_flag == 1 ) {
            timescale = brw.readOnBuffer(32);
        }
        
        if ( pts_offset_type == 1 ) {
            default_pts_offset = brw.readOnBuffer(16);
        }
        
        for ( int i=(descriptor_length-1-
                (timescale_flag==1 ? 4:0)-
                (pts_offset_type==1 ? 2:0)) ; i>0; ) {
            Timestamp timestamp = new Timestamp();
            timestamp.mpu_sequence_number = brw.readOnBuffer(32);
            timestamp.mpu_presentation_time_leap_indicator = (byte) brw.readOnBuffer(2);
            brw.skipOnBuffer(6);;
            timestamp.mpu_decoding_time_offset = brw.readOnBuffer(16);
            timestamp.num_of_au = (byte) brw.readOnBuffer(8);
            
            i-=8;
            
            for ( int j=0; j<timestamp.num_of_au; j++ ) {
                AccessUnit access_unit = new AccessUnit();
                access_unit.dts_pts_offset = brw.readOnBuffer(16);
                i-=2;
                
                if ( pts_offset_type == 2) {
                    access_unit.pts_offset = brw.readOnBuffer(16);
                    i-=2;
                }
                timestamp.access_units.add(access_unit);
            }
            timestamps.add(timestamp);
        }
    }

    @Override
    public void print() {
        super._print_();
        
        Logger.d(String.format("\t pts_offset_type : 0x%x \n", pts_offset_type));
        Logger.d(String.format("\t timescale_flag : 0x%x \n", timescale_flag));
        
        if ( timescale_flag == 1 ) {
            Logger.d(String.format("\t timescale : 0x%x \n", timescale));
        }
        
        if ( pts_offset_type == 1 ) {
            Logger.d(String.format("\t default_pts_offset : 0x%x \n", default_pts_offset));
        }
        
        for ( int i=0; i<timestamps.size(); i++ ) {
            Timestamp timestamp = timestamps.get(i);
            Logger.d(String.format("\t\t [%d] mpu_sequence_number : 0x%x \n", 
                    i, timestamp.mpu_sequence_number));
            Logger.d(String.format("\t\t [%d] mpu_decoding_time_offset : 0x%x \n", 
                    i, timestamp.mpu_decoding_time_offset));
            Logger.d(String.format("\t\t [%d] num_of_au : 0x%x \n", 
                    i, timestamp.num_of_au));
            
            for ( int j=0; j<timestamp.access_units.size(); j++ ) {
                AccessUnit access_unit = timestamp.access_units.get(j);
                Logger.d(String.format("\t\t\t [%d] dts_pts_offset : 0x%04x \n", 
                        j, access_unit.dts_pts_offset));
                if ( pts_offset_type == 2) {
                    Logger.d(String.format("\t\t\t [%d] pts_offset : 0x%04x \n", 
                            j, access_unit.pts_offset));
                }
            }
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;
        
        if ( timescale_flag == 1 ) {
            descriptor_length += 4;
        }
        
        if ( pts_offset_type == 1 ) {
            descriptor_length += 2;
        }
        
        for ( int i=0; i<timestamps.size(); i++ ) {
            Timestamp timestamp = timestamps.get(i);
            descriptor_length += 8;
            for ( int j=0; j<timestamp.access_units.size(); j++ ) {
                descriptor_length += 2;
                if ( pts_offset_type == 2) {
                    descriptor_length += 2;
                }
            }
        }
    }
}
