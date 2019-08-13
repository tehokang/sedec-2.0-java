package com.sedec.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MultilingualComponentDescriptor extends Descriptor {
    protected byte component_tag;
    protected List<ComponentDescription> component_descriptions = new ArrayList<>();

    public class ComponentDescription {
        public byte[] ISO_639_language_code = new byte[3];
        public byte text_description_length;
        public byte[] description;
    }
    public MultilingualComponentDescriptor(BitReadWriter brw) {
        super(brw);

        component_tag = (byte) brw.readOnBuffer(8);

        for ( int i=descriptor_length; i>0; ) {
            ComponentDescription desc = new ComponentDescription();
            desc.ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            desc.ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            desc.ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

            desc.text_description_length = (byte) brw.readOnBuffer(8);
            desc.description = new byte[desc.text_description_length];

            for ( int j=0; j<desc.description.length; j++ ) {
                desc.description[j] = (byte) brw.readOnBuffer(8);
            }

            i-= (4 + desc.description.length);
            component_descriptions.add(desc);
        }
    }

    public List<ComponentDescription> getComponentDescription() {
        return component_descriptions;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t component_tag : 0x%x \n", component_tag));

        for ( int i=0; i<component_descriptions.size(); i++ ) {
            ComponentDescription desc = component_descriptions.get(i);
            Logger.d(String.format("\t [%d] ISO_639_language_code : %s \n",
                    i, new String(desc.ISO_639_language_code)));
            Logger.d(String.format("\t [%d] network_name : %s \n",
                    i, new String(desc.description)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 1;

        for ( int i=0; i<component_descriptions.size(); i++ ) {
            ComponentDescription desc = component_descriptions.get(i);
            descriptor_length += 4;
            descriptor_length += desc.description.length;
        }
    }
}
