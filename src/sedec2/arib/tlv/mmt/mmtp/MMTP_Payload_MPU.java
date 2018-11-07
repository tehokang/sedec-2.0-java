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
    protected List<MFU> mfus = new ArrayList<>();
        
    public class MFU {
        public int data_unit_length;
        public int movie_fragment_sequence_number;
        public int sample_number;
        public int offset;
        public byte priority;
        public byte dependency_counter;
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
            if ( timed_flag == 0x01 ) {
                if ( aggregation_flag == 0x00 ) {
                    MFU mfu = new MFU();
                    mfu.movie_fragment_sequence_number = brw.readOnBuffer(32);
                    mfu.sample_number = brw.readOnBuffer(32);
                    mfu.offset = brw.readOnBuffer(32);
                    mfu.priority = (byte) brw.readOnBuffer(8);
                    mfu.dependency_counter = (byte) brw.readOnBuffer(8);
                    mfu.MFU_data_byte = new byte[payload_length - 20];
                    
                    for ( int i=0; i<mfu.MFU_data_byte.length; i++ ) {
                        mfu.MFU_data_byte[i] = (byte) brw.readOnBuffer(8);
                    }
                    mfus.add(mfu);
                    
                } else {
                    for ( int k=payload_length-6; k>0; ) {
                        MFU mfu = new MFU();
                        mfu.data_unit_length = brw.readOnBuffer(16);
                        mfu.movie_fragment_sequence_number = brw.readOnBuffer(32);
                        mfu.sample_number = brw.readOnBuffer(32);
                        mfu.offset = brw.readOnBuffer(32);
                        mfu.priority = (byte) brw.readOnBuffer(8);
                        mfu.dependency_counter = (byte) brw.readOnBuffer(8);
                        mfu.MFU_data_byte = new byte[mfu.data_unit_length-14];
                        
                        for ( int i=0; i<mfu.MFU_data_byte.length; i++ ) {
                            mfu.MFU_data_byte[i] = (byte) brw.readOnBuffer(8);
                        }
                        k-= (2 + mfu.data_unit_length);
                        mfus.add(mfu);
                    }
                }
                
            } else {
                if ( aggregation_flag == 0x00 ) {
                    MFU mfu = new MFU();
                    mfu.item_id = brw.readOnBuffer(32);
                    mfu.MFU_data_byte = new byte[payload_length - 10];
                    for ( int i=0; i<mfu.MFU_data_byte.length; i++ ) {
                        mfu.MFU_data_byte[i] = (byte) brw.readOnBuffer(8);
                    }
                    mfus.add(mfu);
                    
                } else {
                    for ( int k=payload_length-6; k>0; ) {
                        MFU mfu = new MFU();
                        mfu.data_unit_length = brw.readOnBuffer(16);
                        mfu.item_id = brw.readOnBuffer(32);
                        mfu.MFU_data_byte = new byte[mfu.data_unit_length-4];
                        
                        for ( int i=0; i<mfu.MFU_data_byte.length; i++ ) {
                            mfu.MFU_data_byte[i] = (byte) brw.readOnBuffer(8);
                        }
                        k-= (2 + mfu.data_unit_length);
                        mfus.add(mfu);
                    }
                }
            }
        }
    }
    
    public int getPayloadLength() {
        return payload_length;
    }
    
    public byte getFragmentType() {
        return fragment_type;
    }
    
    public byte getTimedFlag() {
        return timed_flag;
    }
    
    public byte getFragmentationIndicator() {
        return fragmentation_indicator;
    }
    
    public byte getAggregationFlag() {
        return aggregation_flag;
    }
    
    public byte getFragmentCounter() {
        return fragment_counter;
    }
    
    public int getMPUSequenceNumber() {
        return MPU_sequence_number;
    }
    
    public List<MFU> getMFUList() {
        return mfus;
    }
    
    public void print() {
        Logger.d(String.format("------- MMTP Payload ------- (%s)\n", getClass().getName()));
        Logger.d(String.format("payload_length : 0x%x (%d) \n", 
                payload_length, payload_length));
        Logger.d(String.format("fragment_type : 0x%x \n", fragment_type));
        Logger.d(String.format("timed_flag : 0x%x \n", timed_flag));
        Logger.d(String.format("fragmentation_indicator : 0x%x \n", fragmentation_indicator));
        Logger.d(String.format("aggregation_flag : 0x%x \n", aggregation_flag));
        Logger.d(String.format("fragment_counter : 0x%x \n", fragment_counter));
        Logger.d(String.format("MPU_sequence_number : 0x%x \n", MPU_sequence_number));
        
        if ( fragment_type == 0x02 ) {
            if ( timed_flag == 0x01 ) {
                if ( aggregation_flag == 0x00 ) {
                    
                    for ( int i=0; i<mfus.size();i ++) {
                        MFU mfu = mfus.get(i);
                        Logger.d(String.format("[%d] movie_fragment_sequence_number : 0x%x \n", 
                                i, mfu.movie_fragment_sequence_number));
                        Logger.d(String.format("[%d] sample_number : 0x%x \n", 
                                i, mfu.sample_number));
                        Logger.d(String.format("[%d] offset : 0x%x \n", 
                                i, mfu.offset));
                        Logger.d(String.format("[%d] priority : 0x%x \n", 
                                i, mfu.priority));
                        Logger.d(String.format("[%d] dependency_counter : 0x%x \n", 
                                i, mfu.dependency_counter));
                        Logger.d(String.format("[%d] MFU_data_byte length : 0x%x (%d) \n", 
                                i, mfu.MFU_data_byte.length, mfu.MFU_data_byte.length));
                        Logger.d(String.format("[%d] MFU_data_byte : \n", i));
                        
                        BinaryLogger.print(mfu.MFU_data_byte);
                    }
                    
                } else {
                    for ( int i=0; i<mfus.size();i ++) {
                        MFU mfu = mfus.get(i);
                        Logger.d(String.format("[%d] data_unit_length : 0x%x \n", 
                               i, mfu.data_unit_length));
                        Logger.d(String.format("[%d] movie_fragment_sequence_number : 0x%x \n", 
                                i, mfu.movie_fragment_sequence_number));
                        Logger.d(String.format("[%d] sample_number : 0x%x \n", 
                                i, mfu.sample_number));
                        Logger.d(String.format("[%d] offset : 0x%x \n", 
                                i, mfu.offset));
                        Logger.d(String.format("[%d] priority : 0x%x \n", 
                                i, mfu.priority));
                        Logger.d(String.format("[%d] dependency_counter : 0x%x \n", 
                                i, mfu.dependency_counter));
                        Logger.d(String.format("[%d] MFU_data_byte length : 0x%x (%d) \n", 
                                i, mfu.MFU_data_byte.length, mfu.MFU_data_byte.length));
                        Logger.d(String.format("[%d] MFU_data_byte : \n", i));
                        
                        BinaryLogger.print(mfu.MFU_data_byte);
                    }
                }
                
            } else {
                if ( aggregation_flag == 0x00 ) {
                    for ( int i=0; i<mfus.size();i ++) {
                        MFU mfu = mfus.get(i);
                        Logger.d(String.format("[%d] item_id : 0x%x \n", 
                                i, mfu.item_id));
                        Logger.d(String.format("[%d] MFU_data_byte length : 0x%x (%d) \n", 
                                i, mfu.MFU_data_byte.length, mfu.MFU_data_byte.length));
                        Logger.d(String.format("[%d] MFU_data_byte : \n", i));
                        
                        BinaryLogger.print(mfu.MFU_data_byte);
                    }
                    
                } else {
                    for ( int i=0; i<mfus.size();i ++) {
                        MFU mfu = mfus.get(i);
                        Logger.d(String.format("[%d] data_unit_length : 0x%x \n", 
                                i, mfu.data_unit_length));
                        Logger.d(String.format("[%d] item_id : 0x%x \n", 
                                i, mfu.item_id));
                        Logger.d(String.format("[%d] MFU_data_byte length : 0x%x (%d) \n", 
                                i, mfu.MFU_data_byte.length, mfu.MFU_data_byte.length));
                        Logger.d(String.format("[%d] MFU_data_byte : \n",i ));
                        
                        BinaryLogger.print(mfu.MFU_data_byte);
                    }
                }
            }
        }
    }
}
