package sedec2.arib.b39;

import sedec2.arib.b39.messages.CAMessage;
import sedec2.arib.b39.messages.DataTransmissionMessage;
import sedec2.arib.b39.messages.M2SectionMessage;
import sedec2.arib.b39.messages.M2ShortSectionMessage;
import sedec2.arib.b39.messages.Message;
import sedec2.arib.b39.messages.PAMessage;

public class MessageFactory {
    /** PA */
    public final static int PACKAGE_ACCESS_MESSAGE = 0x0000;
    
    /** 
     * @note M2 SECTION could have
     *  - ECM
     *  - EMM
     *  - DCM
     *  - DMM
     *  - MH-EIT
     *  - MH-AIT
     *  - MH-BIT
     *  - MH-SDTT
     *  - MH-SDT
     *  - MH-CDT 
     **/
    public final static int M2_SECTION_MESSAGE = 0x8000;
    
    /** CA */
    public final static int CONDITIONAL_ACCESS_MESSAGE = 0x8001;
    
    /** 
     * @note M2 SHORT SECTION could have
     *  - MH-TOT 
     **/
    public final static int M2_SHORT_SECTION_MESSAGE = 0x8002;
    
    /** DT */
    public final static int DATA_TRANSMISSION_MESSAGE = 0x8003;
    
    public static Message CreateMessage(byte[] buffer) {
        int message_id = ((buffer[0] & 0xff) << 8) | buffer[1] & 0xff;
        
        switch ( message_id ) {
            case PACKAGE_ACCESS_MESSAGE:
                return new PAMessage(buffer);
            case CONDITIONAL_ACCESS_MESSAGE:
                return new CAMessage(buffer);
            case M2_SECTION_MESSAGE:
                return new M2SectionMessage(buffer);
            case M2_SHORT_SECTION_MESSAGE:
                return new M2ShortSectionMessage(buffer);
            case DATA_TRANSMISSION_MESSAGE:
                return new DataTransmissionMessage(buffer);
            default:
                return null;
        }
    }
    
    private MessageFactory() {
        
    }
}
