package sedec2.arib.tlv;

import java.util.ArrayList;
import java.util.List;

public class TLVExtractor {
    protected List<ITLVExtractorListener> listeners = new ArrayList<>();
    protected List<Byte> tlv_bytes = new ArrayList<>();
    
    public void addEventListener(ITLVExtractorListener listener) {
        listeners.add(listener);
    }
    
    public void removeEventListener(ITLVExtractorListener listener) {
        listeners.remove(listener);
    }
    
    public TLVExtractor() {
        
    }
}