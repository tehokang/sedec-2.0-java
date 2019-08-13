package com.sedec.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class ApplicationSignallingDescriptor extends Descriptor {
    protected List<SignallingApplication> signalling_applications = new ArrayList<>();
    public class SignallingApplication {
        public int application_type;
        public byte AIT_version_number;
    }

    public ApplicationSignallingDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            SignallingApplication app = new SignallingApplication();
            brw.skipOnBuffer(1);
            app.application_type = brw.readOnBuffer(15);
            brw.skipOnBuffer(3);
            app.AIT_version_number = (byte) brw.readOnBuffer(5);
            i-=3;
            signalling_applications.add(app);
        }
    }

    public List<SignallingApplication> getApplications() {
        return signalling_applications;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<signalling_applications.size(); i++ ) {
            SignallingApplication app = signalling_applications.get(i);
            Logger.d(String.format("\t [%d] application_type : 0x%x \n",
                    i, app.application_type));
            Logger.d(String.format("\t [%d] AIT_version_number : 0x%x \n",
                    i, app.AIT_version_number));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = signalling_applications.size()*3;
    }
}
