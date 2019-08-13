package com.sedec.arib.tlv.container.mmt.si.descriptors;

import com.sedec.base.BitReadWriter;
import com.sedec.util.Logger;

public class AudioSpecificConfig {
    protected byte audioObjectType;
    protected byte samplingFrequencyIndex;
    protected int samplingFrequency;
    protected byte channelConfiguration;
    protected byte extensionSamplingFrequencyIndex;
    protected int extensionSamplingFrequency;
    protected byte epConfig;
    protected byte directMapping;
    protected int syncExtensionType;
    protected byte extensionAudioObjectType;
    protected byte sbrPresentFlag;

    protected byte bits_length = 0;

    public byte getLength() {
        return (byte) (bits_length/8);
    }

    public void PrintInfo() {
        Logger.d(String.format("\t samplingFrequencyIndex : 0x%x \n",
                samplingFrequencyIndex));
        Logger.d(String.format("\t samplingFrequency : 0x%x \n", samplingFrequency));
        Logger.d(String.format("\t channelConfiguration : 0x%x \n",
                channelConfiguration));
        Logger.d(String.format("\t extensionSamplingFrequencyIndex : 0x%x \n",
                extensionSamplingFrequencyIndex));
        Logger.d(String.format("\t extensionSamplingFrequency : 0x%x \n",
                extensionSamplingFrequency));
        Logger.d(String.format("\t extensionAudioObjectType : 0x%x \n",
                extensionAudioObjectType));
        Logger.d(String.format("\t epConfig : 0x%x \n",
                epConfig));
        Logger.d(String.format("\t syncExtensionType : 0x%x \n",
                syncExtensionType));
        Logger.d(String.format("\t extensionAudioObjectType : 0x%x \n",
                extensionAudioObjectType));
        Logger.d(String.format("\t sbrPresentFlag : 0x%x \n", sbrPresentFlag));
        Logger.d(String.format("\t extensionSamplingFrequencyIndex : 0x%x \n",
                extensionSamplingFrequencyIndex));
        Logger.d(String.format("\t extensionSamplingFrequency : 0x%x \n",
                extensionSamplingFrequency));
    }

    AudioSpecificConfig(BitReadWriter brw) {
        bits_length = 0;
        audioObjectType = (byte) brw.readOnBuffer(5);
        samplingFrequencyIndex = (byte) brw.readOnBuffer(4);

        bits_length += 9;
        if ( samplingFrequencyIndex == 0x0f ) {
            samplingFrequency = brw.readOnBuffer(24);
            bits_length += 24;

        }
        channelConfiguration = (byte) brw.readOnBuffer(4);
        bits_length += 4;

        if ( audioObjectType == 5 ) {
            extensionAudioObjectType = audioObjectType;
            extensionSamplingFrequencyIndex = (byte) brw.readOnBuffer(4);
            bits_length += 4;
            if ( extensionSamplingFrequencyIndex == 0x0f ) {
                extensionSamplingFrequency = brw.readOnBuffer(24);
                bits_length += 24;
            }
            audioObjectType = (byte) brw.readOnBuffer(5);
            bits_length += 5;
        } else {
            extensionAudioObjectType = 0;
        }

        if (    audioObjectType == 17 || audioObjectType == 19 ||
                audioObjectType == 20 || audioObjectType == 21 ||
                audioObjectType == 22 || audioObjectType == 23 ||
                audioObjectType == 24 || audioObjectType == 25 ||
                audioObjectType == 26 || audioObjectType == 27 ) {
            epConfig = (byte) brw.readOnBuffer(2);
            bits_length += 2;
        }

        if ( epConfig == 3 ) {
            directMapping = (byte) brw.readOnBuffer(1);
            bits_length += 1;
        }

        if ( extensionAudioObjectType != 5 ) {
            syncExtensionType = brw.readOnBuffer(11);
            bits_length += 11;
            if (syncExtensionType == 0x2b7) {
                extensionAudioObjectType = (byte) brw.readOnBuffer(5);
                bits_length += 5;
                if ( extensionAudioObjectType == 5 ) {
                    sbrPresentFlag = (byte) brw.readOnBuffer(1);
                    bits_length += 1;
                    if (sbrPresentFlag == 1) {
                        extensionSamplingFrequencyIndex = (byte) brw.readOnBuffer(4);
                        bits_length += 4;
                        if ( extensionSamplingFrequencyIndex == 0xf )
                            extensionSamplingFrequency = brw.readOnBuffer(24);
                        bits_length += 24;
                    }
                }
            }
        }
    }

    public byte getAudioObjectType() {
        return audioObjectType;
    }

    public byte getSamplingFrequenceIndex() {
        return samplingFrequencyIndex;
    }

    public int getSamplingFrequency() {
        return samplingFrequency;
    }

    public byte getChannelConfiguration() {
        return channelConfiguration;
    }

    public byte getExtensionSamplingFrequencyIndex() {
        return extensionSamplingFrequencyIndex;
    }

    public int getExtensionSamplingFrequency() {
        return extensionSamplingFrequency;
    }

    public byte getEpConfig() {
        return epConfig;
    }

    public byte getDirectMapping() {
        return directMapping;
    }

    public int getSyncExtensionType() {
        return syncExtensionType;
    }

    public byte getExtensionAudioObjectType() {
        return extensionAudioObjectType;
    }

    public byte getSBRPresentFlag() {
        return sbrPresentFlag;
    }
}
