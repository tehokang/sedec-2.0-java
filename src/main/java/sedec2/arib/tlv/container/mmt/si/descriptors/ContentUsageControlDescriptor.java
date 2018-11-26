package sedec2.arib.tlv.container.mmt.si.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.util.Logger;

public class ContentUsageControlDescriptor extends Descriptor {
    protected byte remote_view_mode;
    protected byte copy_restriction_mode;
    protected byte image_constraint_token;
    protected byte retention_mode;
    protected byte retention_state;
    protected byte encryption_mode;
    protected int reserved_future_use_length;

    public ContentUsageControlDescriptor(BitReadWriter brw) {
        super(brw);

        remote_view_mode = (byte) brw.readOnBuffer(1);
        copy_restriction_mode = (byte) brw.readOnBuffer(1);
        image_constraint_token = (byte) brw.readOnBuffer(1);

        brw.skipOnBuffer(5);
        brw.skipOnBuffer(3);

        retention_mode = (byte) brw.readOnBuffer(1);
        retention_state = (byte) brw.readOnBuffer(3);
        encryption_mode = (byte) brw.readOnBuffer(1);

        reserved_future_use_length = descriptor_length-2;
        for ( int i=descriptor_length-2; i>0; ) {
            brw.skipOnBuffer(8);
            i-=1;
        }
    }

    public byte getRemoteViewMode() {
        return remote_view_mode;
    }

    public byte getCopyRestrictionMode() {
        return copy_restriction_mode;
    }

    public byte getImageConstraintToken() {
        return image_constraint_token;
    }

    public byte getRetentionMode() {
        return retention_mode;
    }

    public byte getRetentionState() {
        return retention_state;
    }

    public byte getEncryptionMode() {
        return encryption_mode;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t remote_view_mode : 0x%x \n", remote_view_mode));
        Logger.d(String.format("\t copy_restriction_mode : 0x%x \n", copy_restriction_mode));
        Logger.d(String.format("\t image_constraint_token : 0x%x \n",
                image_constraint_token));
        Logger.d(String.format("\t retention_mode : 0x%x \n", retention_mode));
        Logger.d(String.format("\t retention_state : 0x%x \n", retention_state));
        Logger.d(String.format("\t encryption_mode : 0x%x \n", encryption_mode));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 2 + reserved_future_use_length;
    }
}
