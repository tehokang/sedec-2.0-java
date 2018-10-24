package sedec2.arib.tlv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import sedec2.arib.tlv.container.packets.TypeLengthValue;
import sedec2.util.Logger;

public class TLVExtractor implements Runnable {
    protected final String TAG = "TLVExtractor";
    
    protected Thread m_thread;
    protected boolean m_is_running = true;
    protected List<ITLVExtractorListener> m_listeners = new ArrayList<>();
    private BlockingQueue<byte[]> m_tlv_packets = 
            new ArrayBlockingQueue<byte[]>(100);
            
    public void addEventListener(ITLVExtractorListener listener) {
        m_listeners.add(listener);
    }
    
    public void removeEventListener(ITLVExtractorListener listener) {
        m_listeners.remove(listener);
    }
    
    public TLVExtractor() {
        m_thread = new Thread(this);
        m_thread.start();
    }
    
    public void destroy() {
        m_is_running = false;
        m_thread.interrupt();
        m_thread = null;
        
        m_listeners.clear();
        m_listeners = null;
        
        m_tlv_packets.clear();
        m_tlv_packets = null;
    }
    
    public synchronized boolean put(byte[] tlv) {
        try 
        {
            if ( m_tlv_packets != null && tlv != null) 
            {
                m_tlv_packets.put(tlv);
            }
        }
        catch (InterruptedException e) 
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        long sample_counter = 0;
        
        while ( m_is_running ) 
        {
            try 
            {
                Thread.sleep(10);
                if ( null != m_tlv_packets ) 
                {
                    Logger.p(String.format("[%d] TLV \n", sample_counter++));
                    byte[] tlv_raw = (byte[])m_tlv_packets.take();
                    TypeLengthValue tlv = 
                            sedec2.arib.tlv.container.PacketFactory.createPacket(tlv_raw);
                    
                    tlv.print();
                }
            } 
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }        
    }
}