package com.sedec.dvb.ts.si.descriptors;

import java.util.ArrayList;
import java.util.List;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class MultilingualBouquetNameDescriptor extends Descriptor {
    protected List<BouquetkName> bouquet_names = new ArrayList<>();

    public class BouquetkName {
        public byte[] ISO_639_language_code = new byte[3];
        public byte bouquet_name_length;
        public byte[] bouquet_name;
    }
    public MultilingualBouquetNameDescriptor(BitReadWriter brw) {
        super(brw);

        for ( int i=descriptor_length; i>0; ) {
            BouquetkName name = new BouquetkName();
            name.ISO_639_language_code[0] = (byte) brw.readOnBuffer(8);
            name.ISO_639_language_code[1] = (byte) brw.readOnBuffer(8);
            name.ISO_639_language_code[2] = (byte) brw.readOnBuffer(8);

            name.bouquet_name_length = (byte) brw.readOnBuffer(8);
            name.bouquet_name = new byte[name.bouquet_name_length];

            for ( int j=0; j<name.bouquet_name.length; j++ ) {
                name.bouquet_name[j] = (byte) brw.readOnBuffer(8);
            }

            i-= (4 + name.bouquet_name.length);
            bouquet_names.add(name);
        }
    }

    public List<BouquetkName> getBouquetNames() {
        return bouquet_names;
    }

    @Override
    public void print() {
        super._print_();

        for ( int i=0; i<bouquet_names.size(); i++ ) {
            BouquetkName name = bouquet_names.get(i);
            Logger.d(String.format("\t [%d] ISO_639_language_code : %s \n",
                    i, new String(name.ISO_639_language_code)));
            Logger.d(String.format("\t [%d] bouquet_name : %s \n",
                    i, new String(name.bouquet_name)));
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 0;

        for ( int i=0; i<bouquet_names.size(); i++ ) {
            BouquetkName name = bouquet_names.get(i);
            descriptor_length += 4;
            descriptor_length += name.bouquet_name.length;
        }
    }
}
