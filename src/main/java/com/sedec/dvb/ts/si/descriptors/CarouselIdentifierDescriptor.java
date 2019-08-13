package com.sedec.dvb.ts.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

public class CarouselIdentifierDescriptor extends Descriptor {
    protected int carousel_id;
    protected byte FormatID;

    protected byte ModuleVersion;
    protected int ModuleId;
    protected int BlockSize;
    protected int ModuleSize;
    protected byte CompressionMethod;
    protected int OriginalSize;
    protected byte TimeOut;
    protected byte ObjectKeyLength;
    protected byte[] ObjectKeyData;

    protected byte[] private_data_byte;

    public CarouselIdentifierDescriptor(BitReadWriter brw) {
        super(brw);

        carousel_id = brw.readOnBuffer(32);
        FormatID = (byte) brw.readOnBuffer(8);

        if ( FormatID == 0x00 ) {
            private_data_byte = new byte[descriptor_length-5];
            for ( int i=0; i<private_data_byte.length; i++ ) {
                private_data_byte[i] = (byte) brw.readOnBuffer(8);
            }
        } else if ( FormatID == 0x01 ) {
            ModuleVersion = (byte) brw.readOnBuffer(8);
            ModuleId = brw.readOnBuffer(16);
            BlockSize = brw.readOnBuffer(16);
            ModuleSize = brw.readOnBuffer(32);
            CompressionMethod = (byte) brw.readOnBuffer(8);
            OriginalSize = brw.readOnBuffer(32);
            TimeOut = (byte) brw.readOnBuffer(8);
            ObjectKeyLength = (byte) brw.readOnBuffer(8);
            ObjectKeyData = new byte[descriptor_length-5-16];
            for ( int i=0; i<ObjectKeyData.length; i++ ) {
                ObjectKeyData[i] = (byte) brw.readOnBuffer(8);
            }

            private_data_byte = new byte[descriptor_length-5-16-ObjectKeyData.length];
            for ( int i=0; i<private_data_byte.length; i++ ) {
                private_data_byte[i] = (byte) brw.readOnBuffer(8);
            }
        }
    }

    public int getCarouselId() {
        return carousel_id;
    }

    public byte getFormatID() {
        return FormatID;
    }

    public byte getModuleVersion() {
        return ModuleVersion;
    }

    public int getModuleId() {
        return ModuleId;
    }

    public int getBlockSize() {
        return BlockSize;
    }

    public int ModuleSize() {
        return ModuleSize;
    }

    public byte getCompressionMethod() {
        return CompressionMethod;
    }

    public int getOriginalSize() {
        return OriginalSize;
    }

    public byte getTimeOut() {
        return TimeOut;
    }

    public byte getObjectKeyLength() {
        return ObjectKeyLength;
    }

    public byte[] getObjectKeyData() {
        return ObjectKeyData;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t carousel_id : 0x%x \n",  carousel_id));
        Logger.d(String.format("\t FormatID : 0x%d \n", FormatID));

        if ( FormatID == 0x00 ) {
            Logger.d(String.format("\t private_data_byte : \n"));
            BinaryLogger.print(private_data_byte);
        } else if ( FormatID == 0x01 ) {
            Logger.d(String.format("\t ModuleVersion : 0x%x \n", ModuleVersion));
            Logger.d(String.format("\t ModuleId : 0x%x \n", ModuleId));
            Logger.d(String.format("\t BlockSize : 0x%x \n", BlockSize));
            Logger.d(String.format("\t ModuleSize : 0x%x \n", ModuleSize));
            Logger.d(String.format("\t CompressionMethod : 0x%x \n", CompressionMethod));
            Logger.d(String.format("\t OriginalSize : 0x%x \n", OriginalSize));
            Logger.d(String.format("\t TimeOut : 0x%x \n", TimeOut));
            Logger.d(String.format("\t ObjectKeyLength : 0x%x \n", ObjectKeyLength));
            Logger.d(String.format("\t ObjectKeyData : \n"));
            BinaryLogger.print(ObjectKeyData);
            Logger.d(String.format("\t private_data_byte : \n"));
            BinaryLogger.print(private_data_byte);
        }
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 5;

        if ( FormatID == 0x00 ) {
            if ( private_data_byte != null ) {
                descriptor_length += private_data_byte.length;
            }
        } else if ( FormatID == 0x01 ) {
            descriptor_length += (16 + ObjectKeyData.length);

            if ( private_data_byte != null ) {
                descriptor_length += private_data_byte.length;
            }
        }
    }

}
