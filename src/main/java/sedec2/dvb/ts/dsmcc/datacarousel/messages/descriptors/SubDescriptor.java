package sedec2.dvb.ts.dsmcc.datacarousel.messages.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class SubDescriptor {
    protected byte subDescriptorType;
    protected byte subDescriptorLength;
    protected byte[] additionalInformation;

    public SubDescriptor(BitReadWriter brw) {
        subDescriptorType = (byte) brw.readOnBuffer(8);
        subDescriptorLength = (byte) brw.readOnBuffer(8);

        if ( subDescriptorLength > 0 ) {
            additionalInformation = new byte[subDescriptorLength];
            for ( int i=0; i<additionalInformation.length; i++ ) {
                additionalInformation[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public byte getSubDescriptorType() {
        return subDescriptorType;
    }

    public byte getSubDescriptorLength() {
        return subDescriptorLength;
    }

    public byte[] getAdditionalInformation() {
        return additionalInformation;
    }

    public void print() {
        Logger.d(String.format("\t subDescriptorType : 0x%x \n", subDescriptorType));
        Logger.d(String.format("\t subDescriptorLength : 0x%x \n", subDescriptorLength));
        BinaryLogger.print(additionalInformation);
    }

    public int getLength() {
        return 2 + additionalInformation.length;
    }
}