package sedec2.arib.tlv.container.mmt.si.info;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class AdditionalAribSubtitleInfo {
    protected byte subtitle_tag;
    protected byte subtitle_info_version;
    protected byte start_mpu_sequence_number_flag;
    protected byte[] ISO_639_language_code = new byte[3];
    protected byte type;
    protected byte subtitle_format;
    protected byte OPM;
    protected byte TMD;
    protected byte DMF;
    protected byte resolution;
    protected byte compression_type;
    protected int start_mpu_sequence_number;
    protected long reference_start_time;
    protected byte reference_start_time_leap_indicator;
    
    public AdditionalAribSubtitleInfo(BitReadWriter brw) {
        subtitle_tag = (byte) brw.readOnBuffer(8);
        subtitle_info_version = (byte) brw.readOnBuffer(4);
        start_mpu_sequence_number_flag = (byte) brw.readOnBuffer(1);
        brw.skipOnBuffer(3);
        ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
        ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);
        
        type = (byte) brw.readOnBuffer(2);
        subtitle_format = (byte) brw.readOnBuffer(4);
        
        OPM = (byte) brw.readOnBuffer(2);
        TMD = (byte) brw.readOnBuffer(4);
        DMF = (byte) brw.readOnBuffer(4);
        resolution = (byte) brw.readOnBuffer(4);
        compression_type = (byte) brw.readOnBuffer(4);
        
        if ( start_mpu_sequence_number_flag == 1) {
            start_mpu_sequence_number = brw.readOnBuffer(32);
        }
        
        if ( TMD == 0x02 ) {
            reference_start_time = brw.readOnBuffer(64);
            reference_start_time_leap_indicator = (byte) brw.readOnBuffer(2);
            brw.skipOnBuffer(6);
        }
    }

    public byte GetSubtitleTag() {
        return subtitle_tag;
    }
    
    public byte GetSubtitleInfoVersion() {
        return subtitle_info_version;
    }
    
    public byte GetStartMpuSequenceNumberFlag() {
        return start_mpu_sequence_number_flag;
    }
    
    public byte[] GetISO639LanguageCode() {
        return ISO_639_language_code;
    }
    
    public byte GetType() {
        return type;
    }
    
    public byte GetSubtitleFormat() {
        return subtitle_format;
    }
    
    public byte GetOPM() {
        return OPM;
    }
    
    public byte GetTMD() {
        return TMD;
    }
    
    public byte GetDMF() {
        return DMF;
    }
    
    public byte GetResolution() {
        return resolution;
    }
    
    public byte GetCompressionType() {
        return compression_type;
    }
    
    public int GetStartMpuSequenceNumber() {
        return start_mpu_sequence_number;
    }
    
    public long GetReferenceStartTime() {
        return reference_start_time;
    }
    
    public byte GetReferenceStartTimeLeapIndicator() {
        return reference_start_time_leap_indicator;
    }
    
    public int getLength() {
        int length = 8;
        
        if ( start_mpu_sequence_number_flag == 1 ) {
            length += 4;
        }
        
        if ( TMD == 0x02 ) {
            length += 9;
        }
        return length;
    }
    
    public void print() {
        Logger.d(String.format("\t - Additional Arib Subtitle Info - (%s)\n", 
                getClass().getName()));
        Logger.d(String.format("\t subtitle_tag : 0x%x \n", subtitle_tag));
        Logger.d(String.format("\t subtitle_info_version : 0x%x \n", subtitle_info_version));
        Logger.d(String.format("\t start_mpu_sequence_number_flag : 0x%x \n", 
                start_mpu_sequence_number_flag));
        Logger.d(String.format("\t ISO_639_language_code : %s \n", 
                new String(ISO_639_language_code)));
        Logger.d(String.format("\t type : 0x%x \n", type));
        Logger.d(String.format("\t subtitle_format : 0x%x \n", subtitle_format));
        Logger.d(String.format("\t OPM : 0x%x \n", OPM));
        Logger.d(String.format("\t TMD : 0x%x \n",  TMD));
        Logger.d(String.format("\t DMF : 0x%x \n", DMF));
        Logger.d(String.format("\t resolution : 0x%x \n", resolution));
        Logger.d(String.format("\t compression_type : 0x%x \n", compression_type));
        
        if ( start_mpu_sequence_number_flag == 1 ) {
            Logger.d(String.format("start_mpu_sequence_number : 0x%x \n", 
                    start_mpu_sequence_number));
        }
        
        if ( TMD == 0010 ) {
            Logger.d(String.format("reference_start_time : 0x%x \n", reference_start_time));
            Logger.d(String.format("reference_start_time_leap_indicator : 0x%x \n", 
                    reference_start_time_leap_indicator));
        }
    }
}
