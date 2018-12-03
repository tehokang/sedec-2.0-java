package sedec2.dvb.ts.container.packets;

import sedec2.base.BitReadWriter;

public class TransportStream extends BitReadWriter {

    public TransportStream(byte[] buffer) {
        super(buffer);
    }
}
