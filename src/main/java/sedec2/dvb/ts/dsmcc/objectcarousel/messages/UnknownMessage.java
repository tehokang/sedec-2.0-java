package sedec2.dvb.ts.dsmcc.objectcarousel.messages;

import sedec2.base.BitReadWriter;

public class UnknownMessage extends Message {

    public UnknownMessage(BitReadWriter brw) {
        super(brw);

        brw.skipOnBuffer(messageLength*8);
    }

    @Override
    public void print() {
        super._print_();
    }
}
