package com.sedec.arib.b10.tables.dsmcc.datacarousel.messages;

import java.util.ArrayList;
import java.util.List;

import com.sedec.arib.b10.tables.dsmcc.datacarousel.messages.descriptors.CompatibilityDescriptor;
import com.sedec.base.Descriptor;
import com.sedec.util.BinaryLogger;
import com.sedec.util.Logger;

/**
 * Class to represent Table 7-6 DownloadInfoIndication of ISO 13818-6
 */
public class DownloadInfoIndication extends DownloadControlMessage {
    protected int downloadId;
    protected int blockSize;
    protected byte windowSize;
    protected byte ackPeriod;
    protected int tCDownloadWindow;
    protected int tCDownloadScenario;
    protected CompatibilityDescriptor compatibilityDescriptor;
    protected int numberOfModules;
    protected List<Module> modules = new ArrayList<>();
    protected int privateDataLength;
    protected byte[] privateDataByte;

    public class Module {
        public int moduleId;
        public int moduleSize;
        public byte moduleVersion;
        public byte moduleInfoLength;
        public List<Descriptor> descriptors = new ArrayList<>();
    }

    public DownloadInfoIndication(byte[] buffer) {
        super(buffer);

        downloadId = readOnBuffer(32);
        blockSize = readOnBuffer(16);
        windowSize = (byte) readOnBuffer(8);
        ackPeriod = (byte) readOnBuffer(8);
        tCDownloadWindow = readOnBuffer(32);
        tCDownloadScenario = readOnBuffer(32);

        compatibilityDescriptor = new CompatibilityDescriptor(this);
        numberOfModules = readOnBuffer(16);

        for ( int i=0; i<numberOfModules; i++ ) {
            Module module = new Module();
            module.moduleId = readOnBuffer(16);
            module.moduleSize = readOnBuffer(32);
            module.moduleVersion = (byte) readOnBuffer(8);
            module.moduleInfoLength = (byte) readOnBuffer(8);
            for ( int k=module.moduleInfoLength; k>0;  ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                k-=desc.getDescriptorLength();
                module.descriptors.add(desc);
            }

            modules.add(module);
        }

        privateDataLength = readOnBuffer(16);
        privateDataByte = new byte[privateDataLength];
        for ( int i=0; i<privateDataByte.length; i++ ) {
            privateDataByte[i] = (byte) readOnBuffer(8);
        }
    }

    @Override
    public int getDownloadId() {
        return downloadId;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public byte getWindowSize() {
        return windowSize;
    }

    public byte getAckPeriod() {
        return ackPeriod;
    }

    public int getTCDownloadWindow() {
        return tCDownloadWindow;
    }

    public int getTCDownloadScenario() {
        return tCDownloadScenario;
    }

    public CompatibilityDescriptor getCompatabilityDescriptor() {
        return compatibilityDescriptor;
    }

    public int getNumberOfModules() {
        return numberOfModules;
    }

    public List<Module> getModules() {
        return modules;
    }

    public byte[] getPrivateDataByte() {
        return privateDataByte;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("downloadId : 0x%x \n", downloadId));
        Logger.d(String.format("blockSize : 0x%x \n", blockSize));
        Logger.d(String.format("windowSize : 0x%x \n", windowSize));
        Logger.d(String.format("ackPeriod : 0x%x \n", ackPeriod));
        Logger.d(String.format("tCDownloadWindow : 0x%x \n", tCDownloadWindow));
        Logger.d(String.format("tCDownloadScenario : 0x%x \n", tCDownloadScenario));

        compatibilityDescriptor.print();
        Logger.d(String.format("numberOfModules : 0x%x \n", numberOfModules));

        for ( int i=0; i<modules.size(); i++ ) {
            Module module = modules.get(i);
            Logger.d(String.format("[%d] moduleId : 0x%x \n", i, module.moduleId));
            Logger.d(String.format("[%d] moduleSize : 0x%x \n", i, module.moduleSize));
            Logger.d(String.format("[%d] moduleVersion : 0x%x \n",
                    i, module.moduleVersion));
            Logger.d(String.format("[%d] moduleInfoLength : 0x%x \n",
                    i, module.moduleInfoLength));
            for ( i=0; i<module.descriptors.size(); i ++ ) {
                module.descriptors.get(i).print();
            }
        }

        if ( privateDataByte != null ) {
            Logger.d(String.format("privateDataByte.length : 0x%x \n", privateDataByte.length));
            BinaryLogger.print(privateDataByte);
        }
    }

    @Override
    public int getLength() {
        int header_length = super.getLength();
        int payload_length = 20 + compatibilityDescriptor.getLength() +
                privateDataByte.length;

        for ( int i=0; i<modules.size(); i++ ) {
            payload_length += 8;
            if ( privateDataByte != null ) {
                payload_length += privateDataByte.length;
            }
        }
        return header_length + payload_length;
    }
}
