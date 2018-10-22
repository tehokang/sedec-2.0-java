package sedec2.arib.tlv.mmt.mmtp;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;

public class MMTP_Payload_Type00 {
    protected int payload_length;
    protected byte fragment_type;
    protected byte timed_flag;
    protected byte fragmentation_indicator;
    protected byte aggregation_flag;
    protected byte fragment_counter;
    protected int MPU_sequence_number;
    
    class MFU {
        public List<TimedData> timed_data = new ArrayList<>();
        public List<NonTimedData> non_timed_data = new ArrayList<>();
    }
    
    class TimedData {
        public int data_unit_length;
        public int movie_fragment_sequence_number;
        public int sample_number;
        public int offset;
        public byte priority;
        public byte dependency_counter;
        public byte[] MFU_data_byte;
    }
    
    class NonTimedData {
        public int data_unit_length;
        public int item_id;
        public byte[] MFU_data_byte;
    }
    
    public MMTP_Payload_Type00(BitReadWriter brw) {
        payload_length = brw.ReadOnBuffer(16);
        fragment_type = (byte) brw.ReadOnBuffer(4);
        timed_flag = (byte) brw.ReadOnBuffer(1);
        fragmentation_indicator = (byte) brw.ReadOnBuffer(2);
        aggregation_flag = (byte) brw.ReadOnBuffer(1);
        fragment_counter = (byte) brw.ReadOnBuffer(8);
        MPU_sequence_number = brw.ReadOnBuffer(32);
        
        if ( fragment_type == 0x02 ) {
            MFU mfu = new MFU();
            
            if ( timed_flag == 0x01 ) {
                if ( aggregation_flag == 0x00 ) {
                    TimedData td = new TimedData();
                    td.movie_fragment_sequence_number = brw.ReadOnBuffer(32);
                    td.sample_number = brw.ReadOnBuffer(32);
                    td.offset = brw.ReadOnBuffer(32);
                    td.priority = (byte) brw.ReadOnBuffer(8);
                    td.dependency_counter = (byte) brw.ReadOnBuffer(8);
                    td.MFU_data_byte = new byte[payload_length - 20];
                    
                    for ( int i=0; i<td.MFU_data_byte.length; i++ ) {
                        td.MFU_data_byte[i] = (byte) brw.ReadOnBuffer(8);
                    }
                    mfu.timed_data.add(td);
                    
                } else {
                    for ( int k=payload_length-6; k>0; ) {
                        TimedData td = new TimedData();
                        td.data_unit_length = brw.ReadOnBuffer(16);
                        td.movie_fragment_sequence_number = brw.ReadOnBuffer(32);
                        td.sample_number = brw.ReadOnBuffer(32);
                        td.offset = brw.ReadOnBuffer(32);
                        td.priority = (byte) brw.ReadOnBuffer(8);
                        td.dependency_counter = (byte) brw.ReadOnBuffer(8);
                        td.MFU_data_byte = new byte[td.data_unit_length-14];
                        
                        for ( int i=0; i<td.MFU_data_byte.length; i++ ) {
                            td.MFU_data_byte[i] = (byte) brw.ReadOnBuffer(8);
                        }
                        k-= (2 + td.data_unit_length);
                        mfu.timed_data.add(td);
                    }
                }
                
            } else {
                if ( aggregation_flag == 0x00 ) {
                    NonTimedData ntd = new NonTimedData();
                    ntd.item_id = brw.ReadOnBuffer(32);
                    ntd.MFU_data_byte = new byte[payload_length - 10];
                    
                    for ( int i=0; i<ntd.MFU_data_byte.length; i++ ) {
                        ntd.MFU_data_byte[i] = (byte) brw.ReadOnBuffer(8);
                    }
                    
                } else {
                    for ( int k=payload_length-6; k>0; ) {
                        NonTimedData ntd = new NonTimedData();
                        ntd.data_unit_length = brw.ReadOnBuffer(16);
                        ntd.item_id = brw.ReadOnBuffer(32);
                        ntd.MFU_data_byte = new byte[payload_length-4];
                        
                        for ( int i=0; i<ntd.MFU_data_byte.length; i++ ) {
                            ntd.MFU_data_byte[i] = (byte) brw.ReadOnBuffer(8);
                        }
                        mfu.non_timed_data.add(ntd);
                        k-= (2 + ntd.data_unit_length);
                    }
                }
            }
        }
    }
}
