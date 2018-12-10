package sedec2.dvb.ts.dsmcc.datacarousel.messages;

import java.util.ArrayList;
import java.util.List;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.dvb.ts.dsmcc.datacarousel.messages.descriptors.CompatibilityDescriptor;
import sedec2.util.BinaryLogger;
import sedec2.util.Logger;

public class GroupInfoIndication extends BitReadWriter {
    protected int numberOfGroups;
    protected List<GroupInfo> group_infos = new ArrayList<>();
    protected int privateDataLength;
    protected byte[] privateDataByte;

    public class GroupInfo {
        public int groupId;
        public int groupSize;
        public CompatibilityDescriptor groupCompatibility;
        public int groupInfoLength;
        public List<Descriptor> descriptors = new ArrayList<>();
    }

    public GroupInfoIndication(byte[] buffer) {
        super(buffer);

        numberOfGroups = readOnBuffer(16);
        for ( int i=0; i<numberOfGroups; i++ ) {
            GroupInfo group = new GroupInfo();
            group.groupId = readOnBuffer(32);
            group.groupSize = readOnBuffer(32);
            group.groupCompatibility = new CompatibilityDescriptor(this);
            group.groupInfoLength = readOnBuffer(16);

            for ( int k=group.groupInfoLength; k>0; ) {
                Descriptor desc = DescriptorFactory.createDescriptor(this);
                k-=desc.getDescriptorLength();
                group.descriptors.add(desc);
            }
            group_infos.add(group);
        }
        privateDataLength = readOnBuffer(16);
        privateDataByte = new byte[privateDataLength];
        for ( int i=0; i<privateDataByte.length; i++ ) {
            privateDataByte[i] = (byte) readOnBuffer(8);
        }
    }

    public void print() {
        Logger.d(String.format("- %s - \n", getClass().getName()));
        Logger.d(String.format("numberOfGroups : 0x%x \n", numberOfGroups));

        for ( int i=0; i<group_infos.size(); i++ ) {
            GroupInfo group = group_infos.get(i);
            Logger.d(String.format("[%d] GroupId : 0x%x \n", i, group.groupId));
            Logger.d(String.format("[%d] GroupSize : 0x%x \n", i, group.groupSize));
            Logger.d(String.format("[%d] GroupInfoLength : 0x%x \n",
                    i, group.groupInfoLength));

            for ( int k=0; k<group.descriptors.size(); k++ ) {
                group.descriptors.get(k).print();
            }
        }
        Logger.d(String.format("privateDataLength : 0x%x \n", privateDataLength));
        BinaryLogger.print(privateDataByte);
    }
}
