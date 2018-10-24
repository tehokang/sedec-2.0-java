package sedec2.arib.tlv.mmt.mmtp;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class MMTP_Payload_MPU {
    protected int payload_length;
    protected byte fragment_type;
    protected byte timed_flag;
    protected byte fragmentation_indicator;
    protected byte aggregation_flag;
    protected byte fragment_counter;
    protected int MPU_sequence_number;
    protected MFU mfu = null;
    
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
    
    public MMTP_Payload_MPU(BitReadWriter brw) {
        payload_length = brw.readOnBuffer(16);
        fragment_type = (byte) brw.readOnBuffer(4);
        timed_flag = (byte) brw.readOnBuffer(1);
        fragmentation_indicator = (byte) brw.readOnBuffer(2);
        aggregation_flag = (byte) brw.readOnBuffer(1);
        fragment_counter = (byte) brw.readOnBuffer(8);
        MPU_sequence_number = brw.readOnBuffer(32);
        
        if ( fragment_type == 0x02 ) {
            mfu = new MFU();
            
            if ( timed_flag == 0x01 ) {
                if ( aggregation_flag == 0x00 ) {
                    TimedData td = new TimedData();
                    td.movie_fragment_sequence_number = brw.readOnBuffer(32);
                    td.sample_number = brw.readOnBuffer(32);
                    td.offset = brw.readOnBuffer(32);
                    td.priority = (byte) brw.readOnBuffer(8);
                    td.dependency_counter = (byte) brw.readOnBuffer(8);
                    td.MFU_data_byte = new byte[payload_length - 20];
                    
                    for ( int i=0; i<td.MFU_data_byte.length; i++ ) {
                        td.MFU_data_byte[i] = (byte) brw.readOnBuffer(8);
                    }
                    mfu.timed_data.add(td);
                    
                } else {
                    for ( int k=payload_length-6; k>0; ) {
                        TimedData td = new TimedData();
                        td.data_unit_length = brw.readOnBuffer(16);
                        td.movie_fragment_sequence_number = brw.readOnBuffer(32);
                        td.sample_number = brw.readOnBuffer(32);
                        td.offset = brw.readOnBuffer(32);
                        td.priority = (byte) brw.readOnBuffer(8);
                        td.dependency_counter = (byte) brw.readOnBuffer(8);
                        td.MFU_data_byte = new byte[td.data_unit_length-14];
                        
                        for ( int i=0; i<td.MFU_data_byte.length; i++ ) {
                            td.MFU_data_byte[i] = (byte) brw.readOnBuffer(8);
                        }
                        k-= (2 + td.data_unit_length);
                        mfu.timed_data.add(td);
                    }
                }
                
            } else {
                if ( aggregation_flag == 0x00 ) {
                    NonTimedData ntd = new NonTimedData();
                    ntd.item_id = brw.readOnBuffer(32);
                    ntd.MFU_data_byte = new byte[payload_length - 10];
                    
                    for ( int i=0; i<ntd.MFU_data_byte.length; i++ ) {
                        ntd.MFU_data_byte[i] = (byte) brw.readOnBuffer(8);
                    }
                    mfu.non_timed_data.add(ntd);
                    
                } else {
                    for ( int k=payload_length-6; k>0; ) {
                        NonTimedData ntd = new NonTimedData();
                        ntd.data_unit_length = brw.readOnBuffer(16);
                        ntd.item_id = brw.readOnBuffer(32);
                        ntd.MFU_data_byte = new byte[payload_length-4];
                        
                        for ( int i=0; i<ntd.MFU_data_byte.length; i++ ) {
                            ntd.MFU_data_byte[i] = (byte) brw.readOnBuffer(8);
                        }
                        mfu.non_timed_data.add(ntd);
                        k-= (2 + ntd.data_unit_length);
                    }
                }
            }
        }
    }
    
    public void print() {
        Logger.d(String.format("------- MMTP Payload ------- (%s)\n", getClass().getName()));
        Logger.d(String.format("payload_length : 0x%x \n", payload_length));
        Logger.d(String.format("fragment_type : 0x%x \n", fragment_type));
        Logger.d(String.format("timed_flag : 0x%x \n", timed_flag));
        Logger.d(String.format("fragmentation_indicator : 0x%x \n", fragmentation_indicator));
        Logger.d(String.format("aggregation_flag : 0x%x \n", aggregation_flag));
        Logger.d(String.format("fragment_counter : 0x%x \n", fragment_counter));
        Logger.d(String.format("MPU_sequence_number : 0x%x \n", MPU_sequence_number));
        
        if ( fragment_type == 0x02 ) {
            if ( timed_flag == 0x01 ) {
                if ( aggregation_flag == 0x00 ) {
                    
                    for ( int i=0; i<mfu.timed_data.size();i ++) {
                        TimedData td = mfu.timed_data.get(i);
                        Logger.d(String.format("[%d] movie_fragment_sequence_number : 0x%x \n", 
                                i, td.movie_fragment_sequence_number));
                        Logger.d(String.format("[%d] sample_number : 0x%x \n", 
                                i, td.sample_number));
                        Logger.d(String.format("[%d] offset : 0x%x \n", 
                                i, td.offset));
                        Logger.d(String.format("[%d] priority : 0x%x \n", 
                                i, td.priority));
                        Logger.d(String.format("[%d] dependency_counter : 0x%x \n", 
                                i, td.dependency_counter));
                        Logger.d(String.format("[%d] MFU_data_byte length : 0x%x (%d) \n", 
                                i, td.MFU_data_byte.length, td.MFU_data_byte.length));
                        Logger.d(String.format("[%d] MFU_data_byte : \n", i));
                        
                        BinaryLogger.print(td.MFU_data_byte);
                    }
                    
                } else {
                    for ( int i=0; i<mfu.timed_data.size();i ++) {
                        TimedData td = mfu.timed_data.get(i);
                        Logger.d(String.format("[%d] data_unit_length : 0x%x \n", 
                               i, td.data_unit_length));
                        Logger.d(String.format("[%d] movie_fragment_sequence_number : 0x%x \n", 
                                i, td.movie_fragment_sequence_number));
                        Logger.d(String.format("[%d] sample_number : 0x%x \n", 
                                i, td.sample_number));
                        Logger.d(String.format("[%d] offset : 0x%x \n", 
                                i, td.offset));
                        Logger.d(String.format("[%d] priority : 0x%x \n", 
                                i, td.priority));
                        Logger.d(String.format("[%d] dependency_counter : 0x%x \n", 
                                i, td.dependency_counter));
                        Logger.d(String.format("[%d] MFU_data_byte length : 0x%x (%d) \n", 
                                i, td.MFU_data_byte.length, td.MFU_data_byte.length));
                        Logger.d(String.format("[%d] MFU_data_byte : \n", i));
                        
                        BinaryLogger.print(td.MFU_data_byte);
                    }
                }
                
            } else {
                if ( aggregation_flag == 0x00 ) {
                    for ( int i=0; i<mfu.non_timed_data.size(); i++ ) {
                        NonTimedData ntd = mfu.non_timed_data.get(i);
                        Logger.d(String.format("[%d] item_id : 0x%x \n", 
                                i, ntd.item_id));
                        Logger.d(String.format("[%d] MFU_data_byte length : 0x%x (%d) \n", 
                                i, ntd.MFU_data_byte.length, ntd.MFU_data_byte.length));
                        Logger.d(String.format("[%d] MFU_data_byte : \n", i));
                        
                        BinaryLogger.print(ntd.MFU_data_byte);
                    }
                    
                } else {
                    for ( int i=0; i<mfu.non_timed_data.size(); i++ ) {
                        NonTimedData ntd = mfu.non_timed_data.get(i);
                        Logger.d(String.format("[%d] data_unit_length : 0x%x \n", 
                                i, ntd.data_unit_length));
                        Logger.d(String.format("[%d] item_id : 0x%x \n", 
                                i, ntd.item_id));
                        Logger.d(String.format("[%d] MFU_data_byte length : 0x%x (%d) \n", 
                                i, ntd.MFU_data_byte.length, ntd.MFU_data_byte.length));
                        Logger.d(String.format("[%d] MFU_data_byte : \n",i ));
                        
                        BinaryLogger.print(ntd.MFU_data_byte);
                    }
                }
            }
        }
    }
}
