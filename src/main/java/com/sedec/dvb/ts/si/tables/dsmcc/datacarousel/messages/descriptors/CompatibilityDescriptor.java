package com.sedec.dvb.ts.si.tables.dsmcc.datacarousel.messages.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class CompatibilityDescriptor {
    protected int compatibilityDescriptorLength;
    protected int descriptorCount;
    protected List<Descriptor> descriptors = new ArrayList<>();

    public class Descriptor {
        public byte descriptorType;
        public byte descriptorLength;
        public byte specifierType;
        public int specifierData;
        public int model;
        public int version;
        public byte subDescriptorCount;
        public List<SubDescriptor> sub_descriptors = new ArrayList<>();
    }

    public CompatibilityDescriptor(BitReadWriter brw) {
        compatibilityDescriptorLength = brw.readOnBuffer(16);

        if ( compatibilityDescriptorLength > 0 ) {
            descriptorCount = brw.readOnBuffer(16);

            for ( int p=compatibilityDescriptorLength-2; p>0; ) {
                Descriptor desc = new Descriptor();
                desc.descriptorType = (byte) brw.readOnBuffer(8);
                desc.descriptorLength = (byte) brw.readOnBuffer(8);
                desc.specifierType = (byte) brw.readOnBuffer(8);
                desc.specifierData = brw.readOnBuffer(24);
                desc.model = brw.readOnBuffer(16);
                desc.version = brw.readOnBuffer(16);
                desc.subDescriptorCount = (byte) brw.readOnBuffer(8);
                for ( int k=0; k<desc.subDescriptorCount; k++ ) {
                    SubDescriptor sub_desc = new SubDescriptor(brw);
                    desc.sub_descriptors.add(sub_desc);
                    p-=sub_desc.getLength();
                }
                p-= 11;
                descriptors.add(desc);
            }
        }
    }

    public int getCompatibilityDescriptorLength() {
        return compatibilityDescriptorLength;
    }

    public int getDescriptorCount() {
        return descriptorCount;
    }

    public List<Descriptor> getDescriptors() {
        return descriptors;
    }

    public void print() {
        Logger.d(String.format("\t --------------------------- (%s)\n", getClass().getName()));
        Logger.d(String.format("\t compatibilityDescriptorLength : 0x%x \n",
                compatibilityDescriptorLength));
        Logger.d(String.format("\t descriptorCount : 0x%x \n", descriptorCount));

        for ( int i=0; i<descriptors.size(); i++ ) {
            Descriptor desc = descriptors.get(i);

            String descriptorType = "none";
            if ( desc.descriptorType == 0x00 ) {
                descriptorType = "Pad Descriptor";
            } else if (desc.descriptorType == 0x01 ) {
                descriptorType = "System Hardware Descriptor";
            } else if (desc.descriptorType == 0x02 ) {
                descriptorType = "System Software Descriptor";
            }

            Logger.d(String.format("\t [%d] descriptorType : 0x%x (%s) \n",
                    i, desc.descriptorType, descriptorType));
            Logger.d(String.format("\t [%d] descriptorLength : 0x%x \n",
                    i, desc.descriptorLength));
            Logger.d(String.format("\t [%d] specifierType : 0x%x (%s) \n",
                    i, desc.specifierType, desc.specifierType==0x01 ? "IEEE OUI" : "reserved" ));
            Logger.d(String.format("\t [%d] specifierData : 0x%x \n",
                    i, desc.specifierData));
            Logger.d(String.format("\t [%d] model : 0x%x \n",
                    i, desc.model));
            Logger.d(String.format("\t [%d] version : 0x%x \n",
                    i, desc.version));

            for ( int k=0; k<desc.sub_descriptors.size(); k++ ) {
                desc.sub_descriptors.get(k).print();
            }
        }
        Logger.d(String.format("\t --------------------------- \n"));
    }

    public int getLength() {
        return compatibilityDescriptorLength+2;
    }
}
