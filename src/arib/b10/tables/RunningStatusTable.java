package arib.b10.tables;

import java.util.ArrayList;
import java.util.List;

import base.Table;
import util.Logger;

public class RunningStatusTable extends Table {
    protected List<StreamStatus> stream_statuses = new ArrayList<>();
    
    class StreamStatus {
        public int transport_stream_id;
        public int orignal_network_id;
        public int service_id;
        public int event_id;
        public byte running_status;
    }
    
    public RunningStatusTable(byte[] buffer) {
        super(buffer);
        
        __decode_table_body__();
    }

    public List<StreamStatus> GetStreamStatus() {
        return stream_statuses;
    }
    
    @Override
    protected void __decode_table_body__() {
        
        for ( int i=section_length; i>0; ) {
            StreamStatus stream_status = new StreamStatus();
            stream_status.transport_stream_id = ReadOnBuffer(16);
            stream_status.orignal_network_id = ReadOnBuffer(16);
            stream_status.service_id = ReadOnBuffer(16);
            stream_status.event_id = ReadOnBuffer(16);
            SkipOnBuffer(5);
            stream_status.running_status = (byte) ReadOnBuffer(3);
            i-=9;
        }
    }

    @Override
    public void PrintTable() {
        super.PrintTable();
        
        for ( int i=0; i<stream_statuses.size(); i++ ) {
            StreamStatus stream_status = stream_statuses.get(i);
            Logger.d(String.format("[%d] transport_stream_id : 0x%x \n", 
                    i, stream_status.transport_stream_id));
            Logger.d(String.format("[%d] original_network_id : 0x%x \n", 
                    i, stream_status.orignal_network_id));
            Logger.d(String.format("[%d] service_id : 0x%x \n", 
                    i, stream_status.service_id));
            Logger.d(String.format("[%d] event_id : 0x%x \n", i, stream_status.event_id));
            Logger.d(String.format("[%d] running_status : 0x%x \n", 
                    i, stream_status.running_status));
        }
    }
}
