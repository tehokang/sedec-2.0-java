package sedec2.arib.extractor;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseExtractor {
    protected boolean m_enable_filter = false;
    protected List<Listener> m_listeners = new ArrayList<>();
    protected List<Integer> m_int_id_filter = new ArrayList<>();
    protected List<Byte> m_byte_id_filter = new ArrayList<>();
    
    public interface Listener {
        
    }
    
    public BaseExtractor() {
        
    }
    
    public abstract void destroy();
    
    public void addEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == false ) {
            m_listeners.add(listener);
        }
    }
    
    public void removeEventListener(BaseExtractor.Listener listener) {
        if ( m_listeners.contains(listener) == true ) {
            m_listeners.remove(listener);
        }
    }

    public abstract void putIn(byte[] tlv_raw) throws InterruptedException;
    
    protected abstract void putOut(Object obj) throws InterruptedException;
    
    public void addPidFilter(int id) { 
        if ( m_int_id_filter.contains(id) == false ) {
            m_int_id_filter.add(id);
        }
    }    
    
    public void addPidFilter(byte id) { 
        if ( m_byte_id_filter.contains(id) == false ) {
            m_byte_id_filter.add(id);
        }
    }    

    public void removePidFilter(int id) { 
        if ( m_int_id_filter.contains(id) == true ) {
            m_int_id_filter.remove(id);
        }
    }
    
    public void removePidFilter(byte id) { 
        if ( m_byte_id_filter.contains(id) == true ) {
            m_byte_id_filter.remove(id);
        }
    }
    
    public void enableFilter() {
        m_enable_filter = true;
    }
    
    public void disableFilter() {
        m_enable_filter = false;
    }
}
