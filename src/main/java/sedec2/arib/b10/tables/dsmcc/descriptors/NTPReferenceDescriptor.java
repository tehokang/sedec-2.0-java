package sedec2.arib.b10.tables.dsmcc.descriptors;

import sedec2.base.BitReadWriter;
import sedec2.base.Descriptor;
import sedec2.util.Logger;

/**
 * Descriptor to describe Table 8-3 NPT Reference Descriptor of ISO 13818-6
 */
public class NTPReferenceDescriptor extends Descriptor {
    protected byte postDiscontinuityIndicator;
    protected byte contentId;
    protected int STC_Reference;
    protected int NPT_Reference;
    protected int scaleNumerator;
    protected int scaleDenominator;

    public NTPReferenceDescriptor(BitReadWriter brw) {
        super(brw);

        postDiscontinuityIndicator = (byte) brw.readOnBuffer(1);
        contentId = (byte) brw.readOnBuffer(7);
        brw.skipOnBuffer(7);
        STC_Reference = brw.readOnBuffer(33);
        brw.skipOnBuffer(31);
        NPT_Reference = brw.readOnBuffer(33);
        scaleNumerator = brw.readOnBuffer(16);
        scaleDenominator = brw.readOnBuffer(16);
    }

    public byte getPostDiscontinuityIndicator() {
        return postDiscontinuityIndicator;
    }

    public byte getContentId() {
        return contentId;
    }

    public int getSTCReference() {
        return STC_Reference;
    }

    public int getNPTReference() {
        return NPT_Reference;
    }

    public int getScaleNumerator() {
        return scaleNumerator;
    }

    public int getScaleDenominator() {
        return scaleDenominator;
    }

    @Override
    public void print() {
        super._print_();

        Logger.d(String.format("\t postDiscontinuityIndicator : 0x%x \n",
                postDiscontinuityIndicator));
        Logger.d(String.format("\t contentId : 0x%x \n", contentId));
        Logger.d(String.format("\t STC_Reference : 0x%x \n", STC_Reference));
        Logger.d(String.format("\t NPT_Reference : 0x%x \n", NPT_Reference));
        Logger.d(String.format("\t scaleNumerator : 0x%x \n", scaleNumerator));
        Logger.d(String.format("\t scaleDenominator : 0x%x \n", scaleDenominator));
    }

    @Override
    protected void updateDescriptorLength() {
        descriptor_length = 18;
    }
}
