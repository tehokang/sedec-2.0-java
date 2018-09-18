package sedec2.arib.b24.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

/**
 * @brief ApplicationNameDescriptor
 * @note Verified
 */
public class ApplicationNameDescriptor extends Descriptor {
    private byte[] ISO_639_language_code = new byte[3];
    private int application_name_length;
    private byte[] application_name;
    
    public ApplicationNameDescriptor(BitReadWriter brw) {
        super(brw);
        
        for ( int i=descriptor_length; i>0 ; ) {
            ISO_639_language_code[0] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code[1] = (byte) brw.ReadOnBuffer(8);
            ISO_639_language_code[2] = (byte) brw.ReadOnBuffer(8);
            application_name_length = brw.ReadOnBuffer(8);
            application_name = new byte[application_name_length];
            for ( int j=0; j<application_name_length; j++) {
                application_name[j] = (byte) brw.ReadOnBuffer(8);
            }
            i-=(4+application_name_length);
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 4 + application_name.length;
    }

    
    @Override
    public void WriteDescriptor(BitReadWriter brw) {
        super.WriteDescriptor(brw);
        
        if( 0 < descriptor_length ) {
            brw.WriteOnBuffer(ISO_639_language_code[0], 8);
            brw.WriteOnBuffer(ISO_639_language_code[1], 8);
            brw.WriteOnBuffer(ISO_639_language_code[2], 8);
            brw.WriteOnBuffer(application_name_length, 8);

            for ( int i=0;i<application_name_length;i++ ) {
                brw.WriteOnBuffer(application_name[i], 8);
            }
        }
    }

    @Override
    public void PrintDescriptor() {
        super._PrintDescriptorHeader_();
        for ( int i=descriptor_length;i>0; )
        {
            Logger.d(String.format("\t ISO_639_language_code : %c%c%c \n", 
                    ISO_639_language_code[0], 
                    ISO_639_language_code[1],
                    ISO_639_language_code[2]));
                
            Logger.d(String.format("\t application_name_length : 0x%x \n", 
                    application_name_length));
            Logger.d(String.format("\t application_name : %s \n", new String(application_name)));
            i-=(4+application_name_length);
        }
        Logger.d("\n");
    }

    public byte[] GetLanguageCode() {
        return ISO_639_language_code;
    }
    
    public int GetApplicationNameLength() {
        return application_name_length;
    }
    
    public byte[] GetApplicationName() {
        return application_name;
    }
    
    public void SetApplicationName(String name) {
        application_name = null;
        application_name = new byte[name.length()];
        System.arraycopy(name, 0, application_name, 0, name.length());
    }
    
    public void SetLanguageCode(String code) {
        ISO_639_language_code = null;
        ISO_639_language_code = new byte[code.length()];
        System.arraycopy(code, 0, ISO_639_language_code, 0, code.length());
    }
    
}
