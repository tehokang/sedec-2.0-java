package com.sedec.arib.tlv.container.mmtp.mfu;

import com.sedec.util.BinaryLogger;
import com.sedec.util.CodecSpecificDataUtil;
import com.sedec.util.Logger;
import com.sedec.util.ParsableBitArray;

/**
 * Class to decode AudioSyncStream of LATM of ISO14496-3,
 * Decoding routine refers to AudioSyncStream of Table 1.23, 1.41 of ISO-14496
 */
public class MFU_AACLatm extends ParsableBitArray {
    protected int syncword;
    protected int audioMuxLengthBytes;

    protected int audioMuxVersionA;
    protected int numSubframes;
    protected boolean otherDataPresent;
    protected long otherDataLenBits;
    protected int frameLengthType;
    protected int sampleRate;
    protected int channelCount;
    protected byte[] initData;

    public MFU_AACLatm(byte[] buffer) throws ParserException {
        super(buffer);

        syncword = readBits(11);
        audioMuxLengthBytes = readBits(13);

        __parseAudioMuxElement__();
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public byte[] getInitData() {
        return initData;
    }

    protected void __parseAudioMuxElement__() throws ParserException {
        boolean useSameStreamMux = readBit();
        if (!useSameStreamMux) {
            __parseStreamMuxConfig__();
        }

        if (audioMuxVersionA == 0) {
            if (numSubframes != 0) {
                throw new ParserException();
            }
            int muxSlotLengthBytes = __parsePayloadLengthInfo__();
            __parsePayloadMux__(muxSlotLengthBytes);
            if (otherDataPresent) {
                skipBits((int) otherDataLenBits);
            }
        } else {
            throw new ParserException(); // Not defined by ISO/IEC 14496-3:2009.
        }
    }

    protected void __parseStreamMuxConfig__() throws ParserException {
        int audioMuxVersion = readBits(1);
        audioMuxVersionA = audioMuxVersion == 1 ? readBits(1) : 0;
        if (audioMuxVersionA == 0) {
          if (audioMuxVersion == 1) {
            latmGetValue(this); // Skip taraBufferFullness.
          }
          if (!readBit()) {
            throw new ParserException();
          }
          numSubframes = readBits(6);
          int numProgram = readBits(4);
          int numLayer = readBits(3);
          if (numProgram != 0 || numLayer != 0) {
            throw new ParserException();
          }
          if (audioMuxVersion == 0) {
            int startPosition = getPosition();
            int readBits = __parseAudioSpecificConfig__(this);
            setPosition(startPosition);
            initData = new byte[(readBits + 7) / 8];
            readBits(initData, 0, readBits);
          } else {
            int ascLen = (int) latmGetValue(this);
            int bitsRead = __parseAudioSpecificConfig__(this);
            skipBits(ascLen - bitsRead); // fillBits.
          }
          __parseFrameLength__(this);
          otherDataPresent = readBit();
          otherDataLenBits = 0;
          if (otherDataPresent) {
            if (audioMuxVersion == 1) {
              otherDataLenBits = latmGetValue(this);
            } else {
              boolean otherDataLenEsc;
              do {
                otherDataLenEsc = readBit();
                otherDataLenBits = (otherDataLenBits << 8) + readBits(8);
              } while (otherDataLenEsc);
            }
          }
          boolean crcCheckPresent = readBit();
          if (crcCheckPresent) {
            skipBits(8); // crcCheckSum.
          }
        } else {
          throw new ParserException(); // This is not defined by ISO/IEC 14496-3:2009.
        }
    }

    protected int __parseAudioSpecificConfig__(ParsableBitArray data) throws ParserException {
        int bitsLeft = data.bitsLeft();
        Object[] obj = CodecSpecificDataUtil.parseAacAudioSpecificConfig(data, true);
        sampleRate = (int) obj[0];
        channelCount = (int) obj[1];
        return bitsLeft - data.bitsLeft();
    }

    protected void __parseFrameLength__(ParsableBitArray data) {
        frameLengthType = data.readBits(3);
        switch (frameLengthType) {
            case 0:
                data.skipBits(8); // latmBufferFullness.
                break;
            case 1:
                data.skipBits(9); // frameLength.
                break;
            case 3:
            case 4:
            case 5:
                data.skipBits(6); // CELPframeLengthTableIndex.
                break;
            case 6:
            case 7:
                data.skipBits(1); // HVXCframeLengthTableIndex.
                break;
        }
    }

    protected int __parsePayloadLengthInfo__() throws ParserException {
        int muxSlotLengthBytes = 0;
        // Assuming single program and single layer.
        if (frameLengthType == 0) {
            int tmp;
            do {
                tmp = readBits(8);
                muxSlotLengthBytes += tmp;
            } while (tmp == 255);
            return muxSlotLengthBytes;
        } else {
            throw new ParserException();
        }
    }

    protected void __parsePayloadMux__(int muxSlotLengthBytes) {
        int bitPosition = getPosition();
        if ((bitPosition & 0x07) == 0) {

        } else {

        }
    }

    private static long latmGetValue(ParsableBitArray data) {
        int bytesForValue = data.readBits(2);
        return data.readBits((bytesForValue + 1) * 8);
    }

    public void print() {
        Logger.d(String.format("--- AAC(LATM) --- (%s)\n", getClass().getName()));
        Logger.d(String.format("sample rate : %d \n", sampleRate));
        Logger.d(String.format("channelCount : %d \n", channelCount));
        Logger.d(String.format("initData : \n"));
        BinaryLogger.print(initData);
    }

}
