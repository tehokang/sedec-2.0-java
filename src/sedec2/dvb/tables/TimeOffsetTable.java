package sedec2.dvb.tables;

import java.util.ArrayList;
import java.util.List;

import sedec2.arib.b10.DescriptorFactory;
import sedec2.arib.b10.descriptors.Descriptor;
import sedec2.base.Table;
import sedec2.util.JSTTime;
import sedec2.util.Logger;

public class TimeOffsetTable extends Table {
    protected long JST_time_bits;
    protected JSTTime JST_time;
    
    protected long MJD;
    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;
    
    protected int descriptors_loop_length;
    protected List<Descriptor> descriptors = new ArrayList<>();

    public TimeOffsetTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public long getJSTTime() {
        return JST_time.getJSTTime();
    }
    
    public long getMJD() {
        return JST_time.getMJD();
    }
    
    public int getYear() {
        return JST_time.getYear();
    }
    
    public int getMonth() {
        return JST_time.getMonth();
    }
    
    public int getDay() {
        return JST_time.getDay();
    }
    
    public int getHour() {
        return JST_time.getHour();
    }
    
    public int getMinute() {
        return JST_time.getMinute();
    }
    
    public int getSecond() {
        return JST_time.getSecond();
    }
    
    public List<Descriptor> getDescriptors() {
        return descriptors;
    }
    
    @Override
    protected void __decode_table_body__() {
        JST_time_bits = readOnBuffer(40);
        JST_time = new JSTTime(JST_time_bits);
        
        skipOnBuffer(4);
        descriptors_loop_length = readOnBuffer(12);
        
        for ( int i=descriptors_loop_length; i>0; ) {
            Descriptor desc = (Descriptor) DescriptorFactory.createDescriptor(this);
            i-=desc.getDescriptorLength();
            descriptors.add(desc);
        }
        
        checksum_CRC32 = readOnBuffer(32);
    }

    @Override
    public void print() {
        super.print();
        
        Logger.d(String.format("JST_time : %d/%d/%d %d:%d:%d \n",
                JST_time.getYear(), JST_time.getMonth(), JST_time.getDay(), 
                JST_time.getHour(), JST_time.getMinute(), JST_time.getSecond()));
        
        for ( int i=0; i<descriptors.size(); i++ ) {
            descriptors.get(i).print();
        }
        
        Logger.d(String.format("checksum_CRC32 : 0x%x%x%x%x \n", 
                ((checksum_CRC32 >> 24) & 0xff), 
                ((checksum_CRC32 >> 16) & 0xff), 
                ((checksum_CRC32 >> 8) & 0xff), 
                (checksum_CRC32 & 0xff)));
    }

}
