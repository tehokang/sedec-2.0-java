package arib.b39.tables;

import java.util.ArrayList;
import java.util.List;

import arib.b10.DescriptorFactory;
import arib.b10.descriptors.Descriptor;
import base.Table;
import util.Logger;

public class MPEGH_TimeOffsetTable extends Table {
    protected long JST_time;
    
    protected long MJD;
    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;
    
    protected int descriptors_loop_length;
    protected List<Descriptor> descriptors = new ArrayList<>();

    public MPEGH_TimeOffsetTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public long GetJSTTime() {
        return JST_time;
    }
    
    public long GetMJD() {
        return MJD;
    }
    
    public int GetYear() {
        return year;
    }
    
    public int GetMonth() {
        return month;
    }
    
    public int GetDay() {
        return day;
    }
    
    public int GetHour() {
        return hour;
    }
    
    public int GetMinute() {
        return minute;
    }
    
    public int GetSecond() {
        return second;
    }
    
    public List<Descriptor> GetDescriptors() {
        return descriptors;
    }
    
    @Override
    protected void __decode_table_body__() {
        JST_time = ReadOnBuffer(40);
        SkipOnBuffer(4);
        descriptors_loop_length = ReadOnBuffer(12);
        
        for ( int i=descriptors_loop_length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.CreateDescriptor(this);
            i-=desc.GetDescriptorLength();
            descriptors.add(desc);
        }
        
        checksum_CRC32 = ReadOnBuffer(32);
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        Logger.d(String.format("JST_time : %d/%d/%d %d:%d:%d \n",
                year, month, day, hour, minute, second));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).PrintDescriptor();
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%x%x%x%x \n", 
                ((checksum_CRC32 >> 24) & 0xff), 
                ((checksum_CRC32 >> 16) & 0xff), 
                ((checksum_CRC32 >> 8) & 0xff), 
                (checksum_CRC32 & 0xff)));
    }

}
